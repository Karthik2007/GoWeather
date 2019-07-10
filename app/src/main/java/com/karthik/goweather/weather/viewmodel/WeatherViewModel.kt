package com.karthik.goweather.weather.viewmodel

import android.annotation.SuppressLint
import android.location.Geocoder
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.karthik.goweather.base.network.BaseResponse
import com.karthik.goweather.base.util.CoroutineDispatcherProvider
import com.karthik.goweather.base.util.Failure
import com.karthik.goweather.weather.data.ForecastResponse
import com.karthik.goweather.weather.data.repo.WeatherRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 * created by Karthik A on 2019-07-09
 */
class WeatherViewModel(
    private val weatherRepository: WeatherRepository,
    private val dispatcherProvider: CoroutineDispatcherProvider,
    private val locationProvider: FusedLocationProviderClient,
    private val geocoder: Geocoder
) : ViewModel() {

    private val viewModelJob = Job()
    private var uiScope: CoroutineScope

    private val _forecastResponse = MutableLiveData<ForecastResponse>()
    val forecastResponse: LiveData<ForecastResponse>
        get() = _forecastResponse

    private val _noInternetOrError = MutableLiveData<Boolean>()
    val noInternetOrError: LiveData<Boolean>
        get() = _noInternetOrError

    private val _progressLoading = MutableLiveData<Boolean>()
    val progressLoading: LiveData<Boolean>
        get() = _progressLoading

    init {
        uiScope = CoroutineScope(dispatcherProvider.Main + viewModelJob)
    }

    @SuppressLint("MissingPermission")
    fun fetchLocation() {
        _progressLoading.value = true
        locationProvider.lastLocation.addOnSuccessListener {

            if(it == null)
            {
               // if location is null due to one of the reasons mentioned here
                // https://developer.android.com/training/location/retrieve-current#last-known
                // try requesting location update once to retrieve last location

                receiveLocationUpdate()
            }else
            {
                fetchLocationAddress(it)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun receiveLocationUpdate()
    {
        val mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationProvider.removeLocationUpdates(this)
                if (locationResult != null && locationResult.locations.isNotEmpty()) {
                    val newLocation = locationResult.locations[0]
                    fetchLocationAddress(newLocation)
                } else {
                    _noInternetOrError.value = true
                }
            }
        }

        locationProvider.requestLocationUpdates(
            LocationRequest(),
            mLocationCallback, null)
    }

    fun fetchLocationAddress(location: Location) {

        uiScope.launch {

            withContext(dispatcherProvider.IO){
                val addresses = geocoder.getFromLocation(location.latitude,location.longitude,1)

                if(addresses == null || addresses.isEmpty())
                {
                    _progressLoading.value = false
                    _noInternetOrError.value = true
                }else {

                    val address = addresses[0]

                    fetchWeatherForecast(address.locality)
                }


            }



        }

    }


    fun fetchWeatherForecast(region: String)
    {
        uiScope.launch{
            val response =  weatherRepository.getForecast(FORECAST_DAYS,region)
            response.either(::handleFetchForecastFailure, ::handleFetchForecastSuccess)
            _progressLoading.value = false
        }

    }

    fun retryClicked()
    {
        _noInternetOrError.value = false
        fetchLocation()
    }

    /**
     * success handler for product list api response
     */
    private fun handleFetchForecastSuccess(forecastResponse: BaseResponse<ForecastResponse>) {

        _forecastResponse.value = forecastResponse.data
        _noInternetOrError.value = false
    }


    /**
     * failure handler for product list api response
     */
    private fun handleFetchForecastFailure(failure: Failure) {

        _noInternetOrError.value = true
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }



    companion object{
        const val FORECAST_DAYS = 5
    }
}
package com.karthik.goweather.weather.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.karthik.goweather.base.network.BaseResponse
import com.karthik.goweather.base.util.CoroutineDispatcherProvider
import com.karthik.goweather.base.util.Failure
import com.karthik.goweather.weather.data.ForecastResponse
import com.karthik.goweather.weather.data.repo.WeatherRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


/**
 * created by Karthik A on 2019-07-09
 */
class WeatherViewModel(
    private val weatherRepository: WeatherRepository,
    dispatcherProvider: CoroutineDispatcherProvider
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

        fetchWeatherForecast()
    }


    private fun fetchWeatherForecast()
    {
        _progressLoading.value = true
        uiScope.launch{
            val response =  weatherRepository.getForecast(FORECAST_DAYS)
            response.either(::handleFetchForecastFailure, ::handleFetchForecastSuccess)
            _progressLoading.value = false
        }

    }

    fun retryClicked()
    {
        _noInternetOrError.value = false
        fetchWeatherForecast()
    }

    /**
     * success handler for product list api response
     */
    private fun handleFetchForecastSuccess(productResponse: BaseResponse<ForecastResponse>) {

        _forecastResponse.value = productResponse.data
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
        const val FORECAST_DAYS = 10
    }
}
package com.karthik.goweather.weather.viewmodel

import android.location.Geocoder
import android.location.Location
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.android.gms.location.FusedLocationProviderClient
import com.karthik.goweather.base.network.BaseResponse
import com.karthik.goweather.base.network.ConnectionHandler
import com.karthik.goweather.base.util.DataSource
import com.karthik.goweather.base.util.Either
import com.karthik.goweather.base.util.Failure
import com.karthik.goweather.util.TestCoroutineDispachterProvider
import com.karthik.goweather.weather.data.ForecastResponse
import com.karthik.goweather.weather.data.repo.WeatherRepository
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations


/**
 * created by Karthik A on 2019-07-10
 */
class WeatherViewModelTest {

    @get: Rule
    val rule: TestRule = InstantTaskExecutorRule()

    private lateinit var weatherViewModel: WeatherViewModel

    @Mock private lateinit var mockWeatherRepository: WeatherRepository
    @Mock private lateinit var mockLocationProvider: FusedLocationProviderClient
    @Mock private lateinit var mockGeocoder: Geocoder
    @Mock private lateinit var mockResponse: ForecastResponse
    @Mock private lateinit var mockLocation: Location


    @Mock lateinit var mockForescastObserver: Observer<ForecastResponse>
    @Mock lateinit var mockNoInternetOrError: Observer<Boolean>


    @Before
    fun setup()
    {
        MockitoAnnotations.initMocks(this)

        weatherViewModel = WeatherViewModel(mockWeatherRepository,TestCoroutineDispachterProvider(),
            mockLocationProvider,mockGeocoder)
    }


    @Test
    fun fetchForecast_success() = runBlocking{

        var testResponseFromNetwork = BaseResponse( mockResponse, DataSource.FROM_NETWORK)
        whenever(mockWeatherRepository.getForecast(4,"paris")).thenReturn(Either.Success(testResponseFromNetwork))

        weatherViewModel.forecastResponse.observeForever(mockForescastObserver)

        weatherViewModel.fetchWeatherForecast("paris")
        Mockito.verify(mockForescastObserver).onChanged(mockResponse)

    }


    @Test
    fun fetchForecast_serverError() = runBlocking {


        whenever(mockWeatherRepository.getForecast(4,"paris")).thenReturn(Either.Error(Failure.ServerError))

        weatherViewModel.noInternetOrError.observeForever(mockNoInternetOrError)

        weatherViewModel.fetchWeatherForecast("paris")

        Mockito.verify(mockNoInternetOrError).onChanged(true)
    }

    @Test
    fun fetchForecast_noInternet() = runBlocking {

        whenever(mockWeatherRepository.getForecast(4,"paris")).thenReturn(Either.Error(Failure.NoNetworkConnection))

        weatherViewModel.noInternetOrError.observeForever(mockNoInternetOrError)

        weatherViewModel.fetchWeatherForecast("paris")

        Mockito.verify(mockNoInternetOrError).onChanged(true)

    }


    @Test
    fun fetchLocationAddress_emptyAddress() = runBlocking{

        whenever(mockGeocoder.getFromLocation(mockLocation.latitude,mockLocation.longitude,1)).thenReturn(emptyList())

        weatherViewModel.noInternetOrError.observeForever(mockNoInternetOrError)


        weatherViewModel.fetchLocationAddress(mockLocation)

        Mockito.verify(mockNoInternetOrError).onChanged(true)

    }


    @Test
    fun fetchLocationAddress_nullAddress() = runBlocking{

        whenever(mockGeocoder.getFromLocation(mockLocation.latitude,mockLocation.longitude,1)).thenReturn(null)

        weatherViewModel.noInternetOrError.observeForever(mockNoInternetOrError)

        weatherViewModel.fetchLocationAddress(mockLocation)


        Mockito.verify(mockNoInternetOrError).onChanged(true)

    }



}
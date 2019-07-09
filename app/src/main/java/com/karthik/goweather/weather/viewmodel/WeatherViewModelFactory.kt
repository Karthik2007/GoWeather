package com.karthik.goweather.weather.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.karthik.goweather.base.util.CoroutineDispatcherProvider
import com.karthik.goweather.weather.data.repo.WeatherRepository
import javax.inject.Inject


/**
 * created by Karthik A on 2019-07-09
 */
class WeatherViewModelFactory
@Inject constructor(private val weatherRepository: WeatherRepository,
                    private val dispatcher: CoroutineDispatcherProvider): ViewModelProvider.Factory{

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return WeatherViewModel(
            weatherRepository,
            dispatcher
        ) as T
    }
}
package com.karthik.goweather.weather.di

import android.content.Context
import android.location.Geocoder
import android.location.LocationProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.karthik.goweather.base.data.api.ApiClient
import com.karthik.goweather.base.network.ConnectionHandler
import com.karthik.goweather.base.util.CoroutineDispatcherProvider
import com.karthik.goweather.weather.data.api.WeatherApi
import com.karthik.goweather.weather.data.repo.WeatherRepository
import dagger.Module
import dagger.Provides
import java.util.*


/**
 * created by Karthik A on 2019-07-09
 */
@Module
class WeatherModule{

    @Provides
    @WeatherScope
    fun providesWeatherApi(apiClient: ApiClient): WeatherApi
    {
        return apiClient.getApiInstance(WeatherApi::class.java)
    }

    @Provides
    @WeatherScope
    fun providesWeatherRepository(weatherApi: WeatherApi, connectionHandler: ConnectionHandler,
                                  dispachterProvider: CoroutineDispatcherProvider): WeatherRepository
    {
        return WeatherRepository(weatherApi, connectionHandler, dispachterProvider)
    }

    @Provides
    @WeatherScope
    fun providesFusedLocationListener(context: Context): FusedLocationProviderClient
    {
        return LocationServices.getFusedLocationProviderClient(context)
    }

    @Provides
    @WeatherScope
    fun providesGeocoder(context: Context): Geocoder
    {
        return Geocoder(context, Locale.getDefault())
    }
}
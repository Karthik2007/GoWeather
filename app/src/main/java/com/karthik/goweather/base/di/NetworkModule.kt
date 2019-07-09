package com.karthik.goweather.base.di

import ccom.karthik.goweather.base.data.api.interceptor.BaseInterceptor
import com.karthik.goweather.base.GoWeatherApp
import com.karthik.goweather.base.data.api.ApiClient
import com.karthik.goweather.base.data.api.interceptor.CacheInterceptor
import com.karthik.goweather.base.network.ConnectionHandler
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


/**
 *
 * Module to provide all Api client and network related  dependency objects
 *
 * created by Karthik A on 2019-07-08
 */
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun providesConnectionHandler(baseApplication: GoWeatherApp): ConnectionHandler {
        return ConnectionHandler(baseApplication.applicationContext)
    }

    @Singleton
    @Provides
    fun providesBaseInterceptor() = BaseInterceptor()


    @Singleton
    @Provides
    fun providesCacheInterceptor(connectionHandler: ConnectionHandler) = CacheInterceptor(connectionHandler)

    @Singleton
    @Provides
    fun providesApiClient(
        baseApplication: GoWeatherApp,
        baseInterceptor: BaseInterceptor,
        cacheInterceptor: CacheInterceptor
    ): ApiClient {
        return ApiClient(baseApplication.applicationContext, baseInterceptor, cacheInterceptor)
    }
}
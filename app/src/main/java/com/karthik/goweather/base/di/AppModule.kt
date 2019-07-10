package com.karthik.goweather.base.di

import android.app.Application
import android.content.Context
import com.karthik.goweather.base.GoWeatherApp
import com.karthik.goweather.base.navigation.AppNavigator
import com.karthik.goweather.base.util.CoroutineDispatcherProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


/**
 *
 * App level module which supplies root level dependencies
 *
 * created by Karthik A on 2019-07-08
 */
@Module
class AppModule {

    @Provides
    @Singleton
    fun provideContext(application: GoWeatherApp): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun providesDispatcherProvider() = CoroutineDispatcherProvider()

    @Singleton
    @Provides
    fun providesAppNavigator(): AppNavigator = AppNavigator()


}
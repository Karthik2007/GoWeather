package com.karthik.goweather.base.di

import com.karthik.goweather.weather.di.WeatherModule
import com.karthik.goweather.weather.ui.view.WeatherActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector


/**
 *
 * ActivityBuilder tells Dagger the list of activities which needs to be injected with dependencies
 *
 * created by Karthik A on 2019-07-08
 */
@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = [WeatherModule::class])
    abstract fun bindWeatherActivity(): WeatherActivity

}
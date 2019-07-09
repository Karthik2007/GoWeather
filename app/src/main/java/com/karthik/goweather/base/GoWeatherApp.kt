package com.karthik.goweather.base

import com.karthik.goweather.base.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication


/**
 * created by Karthik A on 2019-07-08
 */
class GoWeatherApp: DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        val appComponent = DaggerAppComponent.builder().application(this).build()
        appComponent.inject(this)
        return appComponent
    }
}
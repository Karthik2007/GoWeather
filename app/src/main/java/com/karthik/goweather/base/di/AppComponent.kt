package com.karthik.goweather.base.di

import com.karthik.goweather.base.GoWeatherApp
import com.karthik.goweather.base.di.ActivityBuilder
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import javax.inject.Singleton


/**
 *
 * Root component of the dagger dependency graph
 *
 * created by Karthik A on 2019-07-08
 */
@Singleton
@Component(modules = [ActivityBuilder::class, AndroidInjectionModule::class,
    AppModule::class, NetworkModule::class])
interface AppComponent: AndroidInjector<DaggerApplication>
{
    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(app: GoWeatherApp): Builder

        fun build(): AppComponent
    }


    fun inject(app: GoWeatherApp)

    override fun inject(instance: DaggerApplication?)
}
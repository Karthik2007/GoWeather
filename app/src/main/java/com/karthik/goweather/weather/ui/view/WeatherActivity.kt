package com.karthik.goweather.weather.ui.view

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.karthik.goweather.R
import com.karthik.goweather.weather.viewmodel.WeatherViewModel
import com.karthik.goweather.weather.viewmodel.WeatherViewModelFactory
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class WeatherActivity : DaggerAppCompatActivity() {


    private lateinit var weatherViewModel: WeatherViewModel
    @Inject lateinit var weatherViewModelFactory: WeatherViewModelFactory


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        weatherViewModel = ViewModelProviders.of(this, weatherViewModelFactory).get(WeatherViewModel::class.java)


        setUpActionListeners()

        setUpObservers()
    }


    private fun setUpActionListeners() {

    }


    private fun setUpObservers() {
        weatherViewModel.noInternetOrError.observe(this, Observer {

        })

        weatherViewModel.progressLoading.observe(this, Observer {

        })

        weatherViewModel.forecastResponse.observe(this, Observer {

        })

    }




}

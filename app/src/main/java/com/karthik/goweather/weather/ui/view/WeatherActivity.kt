package com.karthik.goweather.weather.ui.view

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.karthik.goweather.R
import com.karthik.goweather.weather.data.ForecastResponse
import com.karthik.goweather.weather.ui.ForecastListAdapter
import com.karthik.goweather.weather.viewmodel.WeatherViewModel
import com.karthik.goweather.weather.viewmodel.WeatherViewModelFactory
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.android.synthetic.main.error_layout.*
import javax.inject.Inject
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.karthik.goweather.base.util.slideUp


class WeatherActivity : DaggerAppCompatActivity() {


    private lateinit var weatherViewModel: WeatherViewModel
    @Inject lateinit var weatherViewModelFactory: WeatherViewModelFactory
    private lateinit var forecastListAdapter: ForecastListAdapter
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<RecyclerView>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        weatherViewModel = ViewModelProviders.of(this, weatherViewModelFactory).get(WeatherViewModel::class.java)


        setUpActionListeners()

        setRecyclerView()

        setUpObservers()
    }


    private fun setUpActionListeners() {

        retry_btn.setOnClickListener {
            weatherViewModel.retryClicked()
        }

    }


    private fun setUpObservers() {
        weatherViewModel.noInternetOrError.observe(this, Observer {
            error_view.visibility = if (it) View.VISIBLE else View.GONE
        })

        weatherViewModel.progressLoading.observe(this, Observer {
            progress_bar.visibility = if (it) View.VISIBLE else View.GONE

        })

        weatherViewModel.forecastResponse.observe(this, Observer {
            populateResponse(it)
        })

    }


    private fun populateResponse(forecastResponse: ForecastResponse?) {

        forecastResponse?.let {
            region_view.text = it.location.name
            cur_temp_view.text = it.forecast.forecastday[0].day.avgTemp.toString()

            weather_content_view.visibility = View.VISIBLE
            forecastListAdapter.setItems(it.forecast.forecastday)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

        }


    }

    private fun setRecyclerView() {

        val forecastRecyclerView = findViewById<RecyclerView>(R.id.forecast_recycler_view)
        val dividerItemDecoration = DividerItemDecoration(applicationContext, LinearLayoutManager.VERTICAL)

        forecastListAdapter = ForecastListAdapter()

        bottomSheetBehavior = BottomSheetBehavior.from(forecast_recycler_view)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        forecastRecyclerView.apply {

            layoutManager = LinearLayoutManager(applicationContext)
            adapter = forecastListAdapter
            addItemDecoration(dividerItemDecoration)
        }

    }


}

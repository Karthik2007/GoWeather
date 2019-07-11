package com.karthik.goweather.weather.ui.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
import com.karthik.goweather.base.util.LocationHandler


class WeatherActivity : DaggerAppCompatActivity() {


    private lateinit var weatherViewModel: WeatherViewModel
    @Inject
    lateinit var weatherViewModelFactory: WeatherViewModelFactory
    private lateinit var forecastListAdapter: ForecastListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        weatherViewModel = ViewModelProviders.of(this, weatherViewModelFactory).get(WeatherViewModel::class.java)

        setUpActionListeners()

        setRecyclerView()

        setUpObservers()

    }


    override fun onStart() {
        super.onStart()

        if(checkPermission())
        {
            if(LocationHandler(this).isEnabled)
            {
                weatherViewModel.fetchLocation()

            }else {
                showGpsNotEnabledDialog()
            }

        }
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
            cur_temp_view.text = "${it.forecast.forecastday[0].day.avgTemp.toInt()}\u00B0"

            weather_content_view.visibility = View.VISIBLE
            forecastListAdapter.setItems(it.forecast.forecastday)

            animateForecastList()

        }


    }

    private fun animateForecastList() {

        val bottomUp = AnimationUtils.loadAnimation(applicationContext, R.anim.bottom_up)
        forecast_recycler_view.startAnimation(bottomUp)
        forecast_recycler_view.visibility = View.VISIBLE
    }

    private fun setRecyclerView() {

        val forecastRecyclerView = findViewById<RecyclerView>(R.id.forecast_recycler_view)
        val dividerItemDecoration = DividerItemDecoration(applicationContext, LinearLayoutManager.VERTICAL)

        forecastListAdapter = ForecastListAdapter()


        forecastRecyclerView.apply {

            layoutManager = LinearLayoutManager(applicationContext)
            adapter = forecastListAdapter
            addItemDecoration(dividerItemDecoration)
        }

    }


    private fun checkPermission(): Boolean {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION )
            != PackageManager.PERMISSION_GRANTED
        ) {

            // Permission is not granted
            // Should we show an explanation?

            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                LOCATION_REQUEST_CODE
            )
            // LOCATION_REQUEST_CODE is an
            // app-defined int constant. The callback method gets the
            // result of the request.

            return false

        } else {
            // Permission has already been granted
            return true
        }

    }


    private fun showGpsNotEnabledDialog() {

        val alertDialog = AlertDialog.Builder(this)
            .setTitle(R.string.gps_required_title)
            .setMessage(R.string.gps_required_body)
            .setPositiveButton(R.string.action_settings) { _, _ ->
                // Open app's settings.
                val intent = Intent().apply {
                    action = Settings.ACTION_LOCATION_SOURCE_SETTINGS
                }
                startActivity(intent)
            }
            .setNegativeButton(android.R.string.cancel){_,_->
                finish()
            }
            .show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        when (requestCode) {
            LOCATION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                    //fetch location on permission allowed
                    if(LocationHandler(this).isEnabled)
                    {
                        weatherViewModel.fetchLocation()

                    }else {
                        showGpsNotEnabledDialog()
                    }
                } else {

                    // finish activity and close application when permission is denied
                    finish()
                }
                return

            }
        }
    }




    companion object {
        private const val LOCATION_REQUEST_CODE = 55
    }
}

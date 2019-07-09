package com.karthik.goweather.weather.data.api

import com.karthik.goweather.weather.data.ForecastResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


/**
 * created by Karthik A on 2019-07-09
 */
interface WeatherApi {

    @GET("/v1/forecast.json")
    fun getForecast(@Query("days")days: Int, @Query("q")region: String): Call<ForecastResponse>

}
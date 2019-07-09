package com.karthik.goweather.weather.data

import com.google.gson.annotations.SerializedName


/**
 *
 * Trimmed down version of Forecast response with just required field mapping
 *
 * created by Karthik A on 2019-07-09
 */

data class ForecastResponse(val location: Location,
                            val forecast: Forecast)

data class Location(val name: String,
                    val region: String,
                    val lat: Double,
                    val lon: Double)

data class Forecast(val forecastday: List<ForecastDay>)

data class ForecastDay(val date : String,
                       @SerializedName("date_epoch") val dateEpoch: Long,
                       val day: DayDetail)

data class DayDetail(@SerializedName("avgtemp_c") val avgTemp: Float)
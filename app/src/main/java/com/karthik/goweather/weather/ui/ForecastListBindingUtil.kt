package com.karthik.goweather.weather.ui

import android.text.format.DateFormat
import java.text.SimpleDateFormat
import java.util.*


/**
 *
 * * This class is to take out conversion/ any business logic from layout xml file
 * while binding data using data binding library.
 *
 * This class is completely unit testable
 *
 * created by Karthik A on 2019-07-09
 */
class ForecastListBindingUtil {

    companion object{

        @JvmStatic
        fun getTempString(temp: Float) = temp.toString().plus(" C")

        @JvmStatic
        fun getDayofDate(dateString: String): String{
            val dateFormat = SimpleDateFormat("yyyy-MM-dd");
            val date=  dateFormat.parse(dateString)
            return DateFormat.format("EEEE", date).toString()
        }
    }
}
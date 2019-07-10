package com.karthik.goweather.weather.view

import com.karthik.goweather.weather.ui.ForecastListBindingUtil
import org.junit.Assert.assertEquals
import org.junit.Test


/**
 * created by Karthik A on 2019-07-10
 */
class ForecastBindingUtilTest {


    @Test
    fun getTempStringTest()
    {
        var degree = 18f
        var expected = "18 C"

        assertEquals(expected, ForecastListBindingUtil.getTempString(degree))

    }
}
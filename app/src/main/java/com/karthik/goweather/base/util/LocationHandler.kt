package com.karthik.goweather.base.util

import android.content.Context
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.Build
import android.provider.Settings
import android.provider.Settings.Secure.*


/**
 * created by Karthik A on 2019-07-11
 */
class LocationHandler(private val context: Context) {

    val isEnabled: Boolean = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        context.getSystemService(LocationManager::class.java)
            .isProviderEnabled(LocationManager.GPS_PROVIDER)
    } else {
        try {
            getInt(context.contentResolver, LOCATION_MODE) != LOCATION_MODE_OFF
        } catch (e: Settings.SettingNotFoundException) {
            false
        }
    }
}
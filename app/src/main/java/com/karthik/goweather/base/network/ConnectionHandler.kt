package com.karthik.goweather.base.network

import android.content.Context
import android.net.ConnectivityManager


/**
 *
 * created by Karthik A on 2019-07-08
 */
class ConnectionHandler(private val context: Context) {

    val isConnected: Boolean
        get() {
            var networkInfo =
                (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo
            networkInfo?.let { return it.isConnected }
            return false
        }
}
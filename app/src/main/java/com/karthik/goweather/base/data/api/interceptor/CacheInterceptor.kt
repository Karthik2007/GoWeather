package com.karthik.goweather.base.data.api.interceptor

import com.karthik.goweather.base.network.ConnectionHandler
import okhttp3.Interceptor
import okhttp3.Response


/**
 *
 *
 * Interceptor which configures cache control from api response so it can be retrieved when there is no internet
 *
 * created by Karthik A on 2019-07-08
 */
class CacheInterceptor(var connectionHandler: ConnectionHandler) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        var request = chain.request()

        request = when (connectionHandler.isConnected) {
            true -> request.newBuilder().header("Cache-Control", "public, max-age=" + 1).build()
            false ->
                request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=$CACHE_SINCE").build()
        }

        return chain.proceed(request)
    }

    private companion object {
        const val CACHE_SINCE = 60 * 60 * 24 * 7  // 7 days
    }

}


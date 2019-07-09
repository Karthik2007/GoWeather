package com.karthik.goweather.base.data.api.interceptor

import okhttp3.Interceptor
import okhttp3.Response


/**
 *
 * Interceptor to add Api token to each request
 *
 * created by Karthik A on 2019-07-09
 */
class TokenInterceptor: Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url()

        val url = originalUrl.newBuilder().addQueryParameter("key", API_KEY).build()

        val newRequest = originalRequest.newBuilder().url(url).build()

        return chain.proceed(newRequest)
    }

    private companion object{
        const val API_KEY = "58d81147d1f5401c924112454190807"
    }
}
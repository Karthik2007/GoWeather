package com.karthik.goweather.base.data.api

import android.content.Context
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 *
 * App Level Api configuration using retrofit
 *
 * created by Karthik A on 2019-07-08
 */
class ApiClient(var context: Context, vararg apiInterceptors: Interceptor) {

    private val retrofit: Retrofit

    init {
        retrofit = buildRetrofit(BASE_URL, buildOkHttpClient(apiInterceptors))
    }

    fun <T> getApiInstance(apiServiceClass: Class<T>): T = retrofit.create(apiServiceClass)


    /**
     * builds the apiclient with required interceptors like Logginginterceptor, cacheinterceptor... etc
     */
    private fun buildOkHttpClient(apiInterceptors: Array<out Interceptor>): OkHttpClient {

        val okHttpBuilder = OkHttpClient.Builder()


        val cacheSize = CACHE_SIZE
        val cache = Cache(context.cacheDir, cacheSize)

        for (interceptor in apiInterceptors) {
            okHttpBuilder.addInterceptor(interceptor)
        }

        okHttpBuilder.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        okHttpBuilder.cache(cache)
        okHttpBuilder.connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
        okHttpBuilder.readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
        okHttpBuilder.writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)

        return okHttpBuilder.build()
    }

    private fun buildRetrofit(baseUrl: String, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }
}


private const val CONNECTION_TIMEOUT: Long = 20
private const val READ_TIMEOUT: Long = 30
private const val WRITE_TIMEOUT: Long = 20
private const val BASE_URL: String = "https://api.apixu.com/"
private const val CACHE_SIZE: Long = 5 * 1024 * 1024
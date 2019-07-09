package ccom.karthik.goweather.base.data.api.interceptor

import okhttp3.Interceptor
import okhttp3.Response


/**
 * Interceptor to default headers to all the requests
 *
 * created by Karthik A on 2019-07-08
 */
class BaseInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain?): Response {
        chain?.let {
            val originalRequest = chain.request()
            val currentRequest =
                when (originalRequest.header(ACCEPT_HEADER_KEY) == null ||
                        originalRequest.header(CONTENT_TYPE_HEADER_KEY) == null) {
                    true ->
                        originalRequest.newBuilder()
                            .addHeader(ACCEPT_HEADER_KEY, ACCEPT_HEADER_VALUE)
                            .addHeader(CONTENT_TYPE_HEADER_KEY, CONTENT_TYPE_VALUE_KEY)
                            .build()

                    false -> originalRequest
                }
            return chain.proceed(currentRequest)
        }
        return Response.Builder().build()
    }

    private companion object {
        const val ACCEPT_HEADER_KEY = "Accept"
        const val ACCEPT_HEADER_VALUE = "application/json"
        const val CONTENT_TYPE_HEADER_KEY = "Content-Type"
        const val CONTENT_TYPE_VALUE_KEY = "application/json"
    }


}
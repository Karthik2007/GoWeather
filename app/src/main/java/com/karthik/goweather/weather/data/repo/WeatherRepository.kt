package com.karthik.goweather.weather.data.repo

import com.karthik.goweather.base.network.BaseResponse
import com.karthik.goweather.base.network.ConnectionHandler
import com.karthik.goweather.base.util.*
import com.karthik.goweather.weather.data.ForecastResponse
import com.karthik.goweather.weather.data.api.WeatherApi
import kotlinx.coroutines.withContext


/**
 *
 * Repository to fetch Weather Forecast from api.
 * Co-routine on IO dispatcher is used to run network call on background thread
 *
 * created by Karthik A on 2019-07-09
 */
class WeatherRepository(
    private val weatherApi: WeatherApi,
    private val connectionHandler: ConnectionHandler,
    private val dispatcherProvider: CoroutineDispatcherProvider
) {

    suspend fun getForecast(days: Int, region: String): Either<Failure, BaseResponse<ForecastResponse>> =

        withContext(dispatcherProvider.IO) {

            return@withContext try {
                val response = weatherApi.getForecast(days,region).execute()

                when (response.isSuccessful && response.body() != null) {
                    true -> {

                        if (response.raw().networkResponse() == null) {
                            Either.Success(BaseResponse(response.body(), DataSource.FROM_CACHE))
                        } else {
                            Either.Success(BaseResponse(response.body(), DataSource.FROM_NETWORK))
                        }
                    } //Success(response.body()!!)}
                    false -> {

                        if (connectionHandler.isConnected) {
                            Either.Error(Failure.ServerError)
                        } else {
                            Either.Error(Failure.NoNetworkConnection)
                        }
                    }
                }

            } catch (e: Exception) {
                Either.Error(Failure.ServerError)
            }
        }

}
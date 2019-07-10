package com.karthik.goweather.weather.data

import com.karthik.goweather.base.network.BaseResponse
import com.karthik.goweather.base.network.ConnectionHandler
import com.karthik.goweather.base.util.DataSource
import com.karthik.goweather.base.util.Either
import com.karthik.goweather.base.util.Failure
import com.karthik.goweather.util.TestCoroutineDispachterProvider
import com.karthik.goweather.weather.data.api.WeatherApi
import com.karthik.goweather.weather.data.repo.WeatherRepository
import com.nhaarman.mockitokotlin2.given
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import retrofit2.Call
import retrofit2.Response


/**
 * created by Karthik A on 2019-07-10
 */
class WeatherRepositoryTest {

    //class to be tested
    private lateinit var weatherRepository: WeatherRepository

    //dep
    @Mock private lateinit var mockWeatherApi: WeatherApi
    @Mock private lateinit var mockConnectionHandler: ConnectionHandler
    @Mock private lateinit var mockApiCall: Call<ForecastResponse>
    @Mock private lateinit var mockApiResponse: Response<ForecastResponse>
    @Mock private lateinit var mockResponse: ForecastResponse
    @Mock private lateinit var mockRawResponse: okhttp3.Response


    @Before
    fun setup()
    {
        MockitoAnnotations.initMocks(this)

        weatherRepository = WeatherRepository(mockWeatherApi,mockConnectionHandler,TestCoroutineDispachterProvider())

    }


    @Test
    fun getForecast_noInternet() = runBlockingTest{
        var testDays = 5
        var testRegion = "Paris"
        given { mockConnectionHandler.isConnected }.willReturn(false)
        given { mockWeatherApi.getForecast(testDays,testRegion) }.willReturn(mockApiCall)
        given { mockApiCall.execute() }.willReturn(mockApiResponse)
        given { mockApiResponse.isSuccessful }.willReturn(false)

        assertEquals(Either.Error(Failure.NoNetworkConnection),weatherRepository.getForecast(testDays,testRegion))
    }

    @Test
    fun getForecast_non200_response() = runBlockingTest{
        var testDays = 5
        var testRegion = "Paris"
        given { mockConnectionHandler.isConnected }.willReturn(true)
        given { mockWeatherApi.getForecast(testDays,testRegion) }.willReturn(mockApiCall)
        given { mockApiCall.execute() }.willReturn(mockApiResponse)
        given { mockApiResponse.isSuccessful }.willReturn(false)

        assertEquals(Either.Error(Failure.ServerError),weatherRepository.getForecast(testDays,testRegion))
    }


    @Test
    fun getForecast_fromCache()= runBlockingTest {
        var testDays = 5
        var testRegion = "Paris"
        var testBaseResponse = BaseResponse(mockResponse, DataSource.FROM_CACHE)
        given { mockConnectionHandler.isConnected }.willReturn(true)
        given { mockWeatherApi.getForecast(testDays,testRegion) }.willReturn(mockApiCall)
        given { mockApiCall.execute() }.willReturn(mockApiResponse)
        given { mockApiResponse.isSuccessful }.willReturn(true)
        given { mockApiResponse.body() }.willReturn(mockResponse)
        given { mockApiResponse.raw() }.willReturn(mockRawResponse)
        given { mockRawResponse.networkResponse() }.willReturn(null)

        assertEquals(Either.Success(testBaseResponse),weatherRepository.getForecast(testDays,testRegion))
    }


    @Test
    fun getForecast_fromNetwork() = runBlockingTest {
        var testDays = 5
        var testRegion = "Paris"
        var testBaseResponse = BaseResponse(mockResponse, DataSource.FROM_NETWORK)
        given { mockConnectionHandler.isConnected }.willReturn(true)
        given { mockWeatherApi.getForecast(testDays,testRegion) }.willReturn(mockApiCall)
        given { mockApiCall.execute() }.willReturn(mockApiResponse)

        given { mockApiResponse.isSuccessful }.willReturn(true)
        given { mockApiResponse.body() }.willReturn(mockResponse)
        given { mockApiResponse.raw() }.willReturn(mockRawResponse)
        given { mockRawResponse.networkResponse() }.willReturn(mockRawResponse)

        assertEquals(Either.Success(testBaseResponse),weatherRepository.getForecast(testDays,testRegion))
    }


}
package com.karthik.goweather.util

import com.karthik.goweather.base.util.CoroutineDispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers


/**
 * created by Karthik A on 2019-07-10
 */
/**
 *
 * This class provides the corountine dispatchers option by injecting objects
 * This helps in setting up mock dispatchers for testing coroutine functions
 *
 * created by Karthik A on 2019-07-10
 */
open class TestCoroutineDispachterProvider: CoroutineDispatcherProvider() {

    override val Main: CoroutineDispatcher by lazy { Dispatchers.Unconfined }
    override val IO: CoroutineDispatcher by lazy { Dispatchers.Unconfined }
}
package com.karthik.goweather.base.util


/**
 * created by Karthik A on 2019-05-26
 */
sealed class Failure {
    object NoNetworkConnection : Failure()
    object ServerError : Failure()
}
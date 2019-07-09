package com.karthik.goweather.base.util


/**
 *
 * DataSource to indicate the source of response object
 *
 * created by Karthik A on 2019-05-29
 */
sealed class DataSource {

    object FROM_CACHE: DataSource()
    object FROM_NETWORK: DataSource()

}
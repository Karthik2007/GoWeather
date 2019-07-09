package com.karthik.goweather.base.network

import com.karthik.goweather.base.util.DataSource


/**
 * Wrapper response to all api response with information of
 *
 * @see DataSource
 *
 * created by Karthik A on 2019-07-08
 */
data class BaseResponse<T> (var data: T? = null,
             var source: DataSource)

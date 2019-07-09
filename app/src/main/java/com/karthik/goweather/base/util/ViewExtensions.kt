package com.karthik.goweather.base.util

import android.view.View
import android.view.animation.TranslateAnimation


/**
 * created by Karthik A on 2019-07-09
 */
fun View.slideUp(duration: Int = 500) {
    visibility = View.VISIBLE
    val animate = TranslateAnimation(0f, 0f, 500f, 0f)
    animate.duration = duration.toLong()
    animate.fillAfter = true
    this.startAnimation(animate)
}
package com.wenhaiz.himusic.utils

import android.content.Context


object ScreenUtil {
    @JvmStatic
    fun px2dp(context: Context, px: Float): Int {
        val density = context.resources.displayMetrics.density
        return (px / density + 0.5f).toInt()
    }

    @JvmStatic
    fun dp2px(context: Context, dp: Float): Int {
        val density = context.resources.displayMetrics.density
        return (dp * density + 0.5f).toInt()
    }

    @JvmStatic
    fun sp2px(context: Context, spValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }
}
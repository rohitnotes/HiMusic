package com.wenhaiz.himusic.utils

import android.util.Log
import com.wenhaiz.himusic.BuildConfig

object LogUtil {
    private val isDebug = BuildConfig.DEBUG
    @JvmStatic
    fun d(tag: String, msg: String) {
        if (isDebug) {
            Log.d(tag, msg)
        }
    }

    @JvmStatic
    fun e(tag: String, msg: String) {
        if (isDebug) {
            Log.e(tag, msg)
        }
    }

    @JvmStatic
    fun i(tag: String, msg: String) {
        if (isDebug) {
            Log.i(tag, msg)
        }
    }


}
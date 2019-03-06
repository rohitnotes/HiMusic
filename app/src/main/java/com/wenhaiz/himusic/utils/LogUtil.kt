package com.wenhaiz.himusic.utils

import android.util.Log

object LogUtil {
    private const val isDebug = true
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
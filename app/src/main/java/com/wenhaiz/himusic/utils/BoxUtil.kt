package com.wenhaiz.himusic.utils

import android.content.Context
import com.wenhaiz.himusic.MyApp
import io.objectbox.BoxStore


object BoxUtil {

    const val TAG = "BoxUtil"

    fun getBoxStore(context: Context): BoxStore {
        return MyApp.getBoxStore()
    }
}
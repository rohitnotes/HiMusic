package com.example.wenhai.listenall.utils

import android.content.Context
import com.example.wenhai.listenall.MyApp
import io.objectbox.BoxStore


object BoxUtil {

    const val TAG = "BoxUtil"

    fun getBoxStore(context: Context): BoxStore {
        return (context.applicationContext as MyApp).boxStore
    }
}
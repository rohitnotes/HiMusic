package com.example.wenhai.listenall

import android.app.Application
import com.example.wenhai.listenall.data.bean.MyObjectBox
import io.objectbox.BoxStore

class MyApp : Application() {
    lateinit var boxStore: BoxStore

    override fun onCreate() {
        super.onCreate()
        boxStore = MyObjectBox.builder().androidContext(this).build()
    }
}
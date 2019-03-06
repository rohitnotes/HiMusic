package com.wenhaiz.himusic

import android.app.Application
import com.wenhaiz.himusic.data.bean.MyObjectBox
import io.objectbox.BoxStore

class MyApp : Application() {
    lateinit var boxStore: BoxStore

    override fun onCreate() {
        super.onCreate()
        boxStore = MyObjectBox.builder().androidContext(this).build()
    }
}
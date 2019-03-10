package com.wenhaiz.himusic;

import android.app.Application;

import com.wenhaiz.himusic.data.bean.MyObjectBox;

import io.objectbox.BoxStore;

public class MyApp extends Application {

    private static BoxStore boxStore;

    private static MyApp sApp;

    @Override
    public void onCreate() {
        super.onCreate();
        sApp = this;
        boxStore = MyObjectBox.builder().androidContext(this).build();
    }

    public static BoxStore getBoxStore() {
        return boxStore;
    }

    public static MyApp getAppContext() {
        return sApp;
    }

}

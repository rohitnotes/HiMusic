package com.wenhaiz.himusic.utils;

import android.content.Context;

import com.wenhaiz.himusic.MyApp;
import com.wenhaiz.himusic.common.Const;

public class SharedPreferencesUtils {

    private static final String SP_FILE_NAME = "remusic_sp";


    public static void putInt(String key, int value) {
        MyApp.getAppContext().getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE)
                .edit().putInt(key, value).apply();


    }

    public static void putString(String key, String value) {
        MyApp.getAppContext().getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE)
                .edit().putString(key, value).apply();
    }


    public static String getString(String key) {
        return MyApp.getAppContext().getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE)
                .getString(key, "");
    }

    public static int getInt(String key) {
        return MyApp.getAppContext().getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE)
                .getInt(key, 0);
    }



    public static String getToken(){
        return getString(Const.SpKey.XIA_MI_TOKEN);
    }


    public static void setToken(String token){
        putString(Const.SpKey.XIA_MI_TOKEN,token);
    }
}

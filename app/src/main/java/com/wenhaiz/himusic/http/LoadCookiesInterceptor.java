package com.wenhaiz.himusic.http;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.wenhaiz.himusic.MyApp;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class LoadCookiesInterceptor implements Interceptor {
    private static final String TAG = "LoadCookiesInterceptor";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        //获取请求构造器
        Request.Builder builder = request.newBuilder();
        //读取本地 Cookie 信息
        String cookie = loadCookie(request.url().host());
        if (!TextUtils.isEmpty(cookie)) {
            //将 Cookie 添加到请求头中
            builder.addHeader("Cookie", cookie);
            Log.d(TAG, "intercept: cookie for " + request.url().host() + ":" + cookie);
        }
        //重新构造请求并进行处理
        return chain.proceed(builder.build());
    }

    private String loadCookie(String host) {
        SharedPreferences sp = MyApp.getAppContext()
                .getSharedPreferences(HttpUtil.SP_COOKIES, Context.MODE_PRIVATE);
        if (!TextUtils.isEmpty(host) && sp.contains(host)) {
            return sp.getString(host, "");
        }
        return null;
    }
}

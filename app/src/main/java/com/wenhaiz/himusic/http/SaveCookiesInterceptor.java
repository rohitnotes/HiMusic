package com.wenhaiz.himusic.http;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.wenhaiz.himusic.MyApp;
import com.wenhaiz.himusic.utils.SharedPreferencesUtils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class SaveCookiesInterceptor implements Interceptor {
    private static final String TAG = "SaveCookiesInterceptor";

    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        //获取请求及其响应
        Request request = chain.request();
        Response response = chain.proceed(request);
        if (!response.headers("set-cookie").isEmpty()) {
            //获取响应头“set-cookie”值，即为服务器发送的 Cookie
            List<String> cookies = response.headers("set-cookie");
            //将 Cookie 拼接成字符串
            Log.d(TAG, "intercept: coolies=" + cookies.toString());
            String cookie = encodeCookie(cookies);
            //保存 Cookie 字符串
            Log.d(TAG, "intercept: cookies from " + request.url().host() + "=" + cookie);

            //提取 token
            Pattern p = Pattern.compile(".*xm_sg_tk=(.*?)_.*");
            Matcher matcher = p.matcher(cookie);
            if (matcher.matches()) {
                String token = matcher.group(1);
                SharedPreferencesUtils.setToken(token);
                Log.d(TAG, "intercept: token=" + token);
                saveCookie(request.url().host(), cookie);
            } else {
                Log.e(TAG, "intercept: 没有提取到 token");
            }

        }
        return response;
    }

    private String encodeCookie(List<String> cookies) {
        StringBuilder sb = new StringBuilder();
        Set<String> set = new HashSet<>();
        for (String cookie : cookies) {
            String[] arr = cookie.split(";");
            set.addAll(Arrays.asList(arr));
        }
        for (String cookie : set) {
            sb.append(cookie).append(";");
        }
        sb.deleteCharAt(sb.lastIndexOf(";"));
        return sb.toString();
    }

    private void saveCookie(String host, String cookie) {
        SharedPreferences.Editor editor = MyApp.getAppContext()
                .getSharedPreferences(HttpUtil.SP_COOKIES, Context.MODE_PRIVATE)
                .edit();
        if (!TextUtils.isEmpty(host)) {
            editor.putString(host, cookie);
        }
        editor.apply();
    }


    static void cleanCookie() {
        SharedPreferences.Editor editor = MyApp.getAppContext()
                .getSharedPreferences(HttpUtil.SP_COOKIES, Context.MODE_PRIVATE)
                .edit();
        editor.remove("www.xiami.com").apply();
    }
}

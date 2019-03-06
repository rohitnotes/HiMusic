package com.wenhaiz.himusic.http;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.gson.Gson;
import com.wenhaiz.himusic.MyApp;
import com.wenhaiz.himusic.R;
import com.wenhaiz.himusic.http.data.BaseResultData;
import com.wenhaiz.himusic.http.request.BaseRequest;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;


public abstract class HttpCallback<T> {

    private static List<BaseRequest> failedRequestBecauseToken = new ArrayList<>();

    private static final String TAG = "HttpCallback";

    private BaseRequest request;

    public HttpCallback(BaseRequest request) {
        this.request = request;
    }

    /**
     * 主线程 Handler
     */
    private static final Handler sMainHandler = new Handler(Looper.getMainLooper());


    void onHttpResponse(Response response) {
        if (response.isSuccessful()) {
            try {
                if (response.body() == null) {
                    onHttpFailure(ErrorCode.EMPTY_RESPONSE, MyApp.getAppContext().getString(R.string.http_empty_response));
                    return;
                }
                final String bodyString = response.body().string();

                if (bodyString.contains("rgv") || bodyString.contains("x5step")) {
                    onHttpFailure(ErrorCode.FREQUENCY_REQUEST, MyApp.getAppContext().getString(R.string.http_frequency_request));
                    return;
                }
                Gson gson = new Gson();

                Object o;
                try {
                    o = gson.fromJson(bodyString, getType());
                } catch (Exception e) {
                    sMainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            onStringResponse(bodyString);
                        }
                    });
                    return;
                }

                if (!(o instanceof BaseResultData)) {
                    onHttpFailure(ErrorCode.WRONG_FORMAT, MyApp.getAppContext().getString(R.string.http_wrong_format));
                    return;
                }

                BaseResultData data = ((BaseResultData) o);
                if ("SG_TOKEN_EMPTY".equals(data.getCode())||"SG_INVALID".equals(data.getCode()) || "SG_TOKEN_EXPIRED".equals(data.getCode())) {
                    onTokenInvalid();
                    failedRequestBecauseToken.add(this.request);
                    return;
                }

                final T t = (T) data.getResultData().getData();
                if (t == null) {
                    onHttpFailure(data.getCode(), MyApp.getAppContext().getString(R.string.null_data));
                    return;
                }
                sMainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        onHttpSuccess(t);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
                onHttpFailure(response.code() + "", e.getLocalizedMessage());
            }
        } else {
            onHttpFailure(response.code() + "", response.message());
        }
    }


    private void onTokenInvalid() {
        //取消所有请求
        HttpUtil.getHttpClient().dispatcher().cancelAll();
        //清理 cookie
        SaveCookiesInterceptor.cleanCookie();
        //重新获取 token
        Log.d(TAG, "onHttpFailure: 重新获取token中");
        HttpUtil.get(API.XiaMi.BASE, new HttpCallback(request) {

            @Override
            public void onStringResponse(String bodyString) {
                super.onStringResponse(bodyString);
                Log.d(TAG, "onStringResponse: 获取 token 成功，重新发送请求");
                // 重新发送所有 token 失效后的请求
                for (BaseRequest request:failedRequestBecauseToken) {
                    request.send();
                }
                failedRequestBecauseToken.clear();
            }

            @Override
            public void onHttpSuccess(Object data) {
                Log.d(TAG, "onHttpSuccess: token success");
            }

            @Override
            public void onFailure(String code, String msd) {

            }

            @Override
            public Type getType() {
                return null;
            }
        });
    }


    public abstract void onHttpSuccess(T data);

    public void onStringResponse(String bodyString) {

    }


    final void onHttpFailure(final String code, final String msg) {
        Log.e(TAG, "onHttpFailure: code=" + code + ",msg" + msg);
        sMainHandler.post(new Runnable() {
            @Override
            public void run() {
                onFailure(code, msg);
            }
        });
    }

    public abstract void onFailure(String code, String msd);


    public abstract Type getType();


}

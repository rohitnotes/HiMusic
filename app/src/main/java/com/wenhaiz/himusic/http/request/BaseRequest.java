package com.wenhaiz.himusic.http.request;

import android.support.annotation.NonNull;

import com.wenhaiz.himusic.http.HttpCallback;
import com.wenhaiz.himusic.http.HttpMethod;
import com.wenhaiz.himusic.http.HttpUtil;

import java.lang.reflect.Type;

public abstract class BaseRequest<T> {
    private BaseDataCallback<T> dataCallback = null;

    public abstract Type getType();

    public abstract HttpMethod getHttpMethod();

    public void send() {
        //发送请求
        if (getHttpMethod() == null) {
            return;
        }


        HttpCallback<T> callback = new HttpCallback<T>(this) {
            @Override
            public void onHttpSuccess(T data) {
                if (dataCallback == null) {
                    return;
                }

                dataCallback.onSuccess(data);
            }

            @Override
            public Type getType() {
                return BaseRequest.this.getType();
            }

            @Override
            public void onFailure(String code, String msg) {
                if (dataCallback == null) {
                    return;
                }
                dataCallback.onFailure(code, msg);
            }
        };

        if (dataCallback != null) {
            dataCallback.beforeRequest();
        }

        HttpMethod method = getHttpMethod();
        if (method.getRequestType() == HttpMethod.RequestType.GET) {
            HttpUtil.get(method.getUrl(), method.getData(), callback);
        } else if (method.getRequestType() == HttpMethod.RequestType.POST) {
            HttpUtil.post(method.getUrl(), method.getData(), callback);
        }
    }

    public BaseRequest<T> setDataCallback(BaseDataCallback<T> dataCallback) {
        this.dataCallback = dataCallback;
        return this;
    }

    public static abstract class BaseDataCallback<T> {

        public abstract void onSuccess(@NonNull T data);


        public abstract void onFailure(String code, String msg);


        public abstract void beforeRequest();
    }
}

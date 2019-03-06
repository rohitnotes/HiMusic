package com.wenhaiz.himusic.http;

import android.net.Uri;

import java.util.HashMap;

public class HttpMethod {
    private RequestType requestType;
    private HashMap<String, Object> data;
    private String url;

    public static HttpMethod get(Uri uri, HashMap<String, Object> data) {
        return new HttpMethod(RequestType.GET, data, uri.toString());
    }

    public static HttpMethod get(String url) {
        return get(Uri.parse(url), null);
    }

    public static HttpMethod get(String url, HashMap<String, Object> data) {
        return get(Uri.parse(url), data);
    }

    public static HttpMethod post(Uri uri, HashMap<String, Object> data) {
        return new HttpMethod(RequestType.POST, data, uri.toString());
    }

    public static HttpMethod post(String url, HashMap<String, Object> data) {
        return new HttpMethod(RequestType.POST, data, url);
    }

    private HttpMethod(RequestType requestType, HashMap<String, Object> data, String url) {
        this.requestType = requestType;
        this.data = data;
        this.url = url;
    }


    public RequestType getRequestType() {
        return requestType;
    }

    public HashMap<String, Object> getData() {
        return data;
    }

    public String getUrl() {
        return url;
    }

    public enum RequestType {
        GET, POST
    }
}

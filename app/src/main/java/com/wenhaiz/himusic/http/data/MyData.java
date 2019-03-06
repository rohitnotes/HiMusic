package com.wenhaiz.himusic.http.data;

import com.google.gson.annotations.SerializedName;

public class MyData {
    @SerializedName("hello")
    private String hello;

    public String getHello() {
        return hello;
    }

    public void setHello(String hello) {
        this.hello = hello;
    }
}

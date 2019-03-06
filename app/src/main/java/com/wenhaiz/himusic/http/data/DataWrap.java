package com.wenhaiz.himusic.http.data;

import com.google.gson.annotations.SerializedName;

public class DataWrap<T> {

    @SerializedName("status")
    private String status;
    @SerializedName("data")
    private T data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

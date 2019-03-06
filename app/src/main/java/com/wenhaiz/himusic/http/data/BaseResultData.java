package com.wenhaiz.himusic.http.data;

import com.google.gson.annotations.SerializedName;

public class BaseResultData<T> {
    @SerializedName("code")
    private String code;
    @SerializedName("msg")
    private String msg;
    @SerializedName("result")
    private DataWrap<T> resultData;

    public void setResultData(DataWrap<T> resultData) {
        this.resultData = resultData;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public DataWrap<T> getResultData() {
        return resultData;
    }

}

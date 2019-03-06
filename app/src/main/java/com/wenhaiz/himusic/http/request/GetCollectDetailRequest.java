package com.wenhaiz.himusic.http.request;

import com.google.gson.reflect.TypeToken;
import com.wenhaiz.himusic.http.API;
import com.wenhaiz.himusic.http.HttpMethod;
import com.wenhaiz.himusic.http.HttpUtil;
import com.wenhaiz.himusic.http.data.BaseResultData;
import com.wenhaiz.himusic.http.data.CollectDetail;

import java.lang.reflect.Type;
import java.util.HashMap;

public class GetCollectDetailRequest extends BaseRequest<CollectDetail> {

    private long listId;

    public GetCollectDetailRequest(long listId) {
        this.listId = listId;
    }

    @Override
    public Type getType() {
        return new TypeToken<BaseResultData<CollectDetail>>() {
        }.getType();
    }

    @Override
    public HttpMethod getHttpMethod() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("listId", listId);
        return HttpMethod.get(HttpUtil.getXiamiUrl(API.XiaMi.GET_COLLECT_DETAIL, data));
    }
}

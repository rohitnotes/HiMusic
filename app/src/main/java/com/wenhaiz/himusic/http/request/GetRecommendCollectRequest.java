package com.wenhaiz.himusic.http.request;

import com.google.gson.reflect.TypeToken;
import com.wenhaiz.himusic.http.API;
import com.wenhaiz.himusic.http.HttpMethod;
import com.wenhaiz.himusic.http.HttpUtil;
import com.wenhaiz.himusic.http.data.BaseResultData;
import com.wenhaiz.himusic.http.data.QueryPage;
import com.wenhaiz.himusic.http.data.RecommendListRecommendInfo;

import java.lang.reflect.Type;
import java.util.HashMap;

public class GetRecommendCollectRequest extends BaseRequest<RecommendListRecommendInfo> {

    private CollectType collectType;

    private int page = 1;

    public GetRecommendCollectRequest() {
        this(CollectType.SYSTEM);
    }

    public GetRecommendCollectRequest(CollectType collectType) {
        this.collectType = collectType;
    }


    public void setPage(int page) {
        this.page = page;
    }

    @Override
    public Type getType() {
        return new TypeToken<BaseResultData<RecommendListRecommendInfo>>() {
        }.getType();
    }

    @Override
    public HttpMethod getHttpMethod() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("pagingV0", new QueryPage(page, 60));
        data.put("dataType", collectType.value);
        return HttpMethod.get(HttpUtil.getXiamiUrl(API.XiaMi.GET_COLLECT_LIST, data));
    }


    public enum CollectType {
        SYSTEM("system"), HOT("hot"), NEW("new"), RECOMMEND("recommand");
        private String value;

        CollectType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}

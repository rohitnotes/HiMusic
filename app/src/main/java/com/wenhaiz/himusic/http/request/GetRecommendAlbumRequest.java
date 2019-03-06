package com.wenhaiz.himusic.http.request;

import com.google.gson.reflect.TypeToken;
import com.wenhaiz.himusic.http.API;
import com.wenhaiz.himusic.http.HttpMethod;
import com.wenhaiz.himusic.http.HttpUtil;
import com.wenhaiz.himusic.http.data.BaseResultData;
import com.wenhaiz.himusic.http.data.QueryPage;
import com.wenhaiz.himusic.http.data.RecommendListNewAlbumInfo;

import java.lang.reflect.Type;
import java.util.HashMap;

public class GetRecommendAlbumRequest extends BaseRequest<RecommendListNewAlbumInfo> {
    @Override
    public Type getType() {
        return new TypeToken<BaseResultData<RecommendListNewAlbumInfo>>() {
        }.getType();
    }

    @Override
    public HttpMethod getHttpMethod() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("pagingV0", new QueryPage(1, 60));
        data.put("filter","");
        return HttpMethod.get(HttpUtil.getXiamiUrl(API.XiaMi.GET_ALBUM_LIST, data));
    }
}

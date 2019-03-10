package com.wenhaiz.himusic.http.request

import com.google.gson.reflect.TypeToken
import com.wenhaiz.himusic.http.API
import com.wenhaiz.himusic.http.HttpMethod
import com.wenhaiz.himusic.http.HttpUtil
import com.wenhaiz.himusic.http.data.BaseResultData
import com.wenhaiz.himusic.http.data.RankList
import java.lang.reflect.Type

class GetRankListRequest : BaseRequest<RankList>() {
    override fun getType(): Type {
        return object : TypeToken<BaseResultData<RankList>>() {}.type
    }

    override fun getHttpMethod(): HttpMethod {
        return HttpMethod.get(HttpUtil.getXiamiUrl(API.XiaMi.GET_RANK_LIST, null))
    }

}

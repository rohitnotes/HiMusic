package com.wenhaiz.himusic.http.request

import com.google.gson.reflect.TypeToken
import com.wenhaiz.himusic.http.API
import com.wenhaiz.himusic.http.HttpMethod
import com.wenhaiz.himusic.http.HttpUtil
import com.wenhaiz.himusic.http.data.BaseResultData
import com.wenhaiz.himusic.http.data.RankDetail
import com.wenhaiz.himusic.http.data.RankList
import java.lang.reflect.Type

class GetRankDetailRequest(private val rank: RankList.Rank) : BaseRequest<RankDetail>() {
    override fun getType(): Type {
        return object : TypeToken<BaseResultData<RankDetail>>() {}.type
    }

    override fun getHttpMethod(): HttpMethod {
        val data: HashMap<String, Any> = hashMapOf("billboardId" to rank.billboardId)
        return HttpMethod.get(HttpUtil.getXiamiUrl(API.XiaMi.GET_RANK_DETAIL,data))
    }
}
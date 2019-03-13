package com.wenhaiz.himusic.http.request

import com.google.gson.reflect.TypeToken
import com.wenhaiz.himusic.http.API
import com.wenhaiz.himusic.http.HttpMethod
import com.wenhaiz.himusic.http.HttpUtil
import com.wenhaiz.himusic.http.data.BaseResultData
import com.wenhaiz.himusic.http.data.SearchTips
import java.lang.reflect.Type

class GetSearchTipsRequest(val keyword: String) : BaseRequest<SearchTips>() {
    override fun getType(): Type {
        return object : TypeToken<BaseResultData<SearchTips>>() {}.type
    }

    override fun getHttpMethod(): HttpMethod {
        val data: HashMap<String, Any> = hashMapOf("key" to keyword)
        return HttpMethod.get(HttpUtil.getXiamiUrl(API.XiaMi.GET_SEARCH_TIPS, data))

    }
}
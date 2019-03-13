package com.wenhaiz.himusic.http.request

import com.google.gson.reflect.TypeToken
import com.wenhaiz.himusic.http.API
import com.wenhaiz.himusic.http.HttpMethod
import com.wenhaiz.himusic.http.HttpUtil
import com.wenhaiz.himusic.http.data.BaseResultData
import com.wenhaiz.himusic.http.data.QueryPage
import com.wenhaiz.himusic.http.data.SearchSongResult
import java.lang.reflect.Type

class SearchSongsRequest(val keyword: String) : BaseRequest<SearchSongResult>() {
    override fun getType(): Type {
        return object : TypeToken<BaseResultData<SearchSongResult>>() {}.type
    }

    override fun getHttpMethod(): HttpMethod {
        val data: HashMap<String, Any> = hashMapOf("key" to keyword,
                "pagingVO" to QueryPage(1, 60))
        return HttpMethod.get(HttpUtil.getXiamiUrl(API.XiaMi.SEARCH_SONGS, data))
    }
}
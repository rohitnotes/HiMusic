package com.wenhaiz.himusic.http.request

import com.google.gson.reflect.TypeToken
import com.wenhaiz.himusic.http.API
import com.wenhaiz.himusic.http.HttpMethod
import com.wenhaiz.himusic.http.HttpUtil
import com.wenhaiz.himusic.http.data.AlbumDetail
import com.wenhaiz.himusic.http.data.BaseResultData
import java.lang.reflect.Type

class GetAlbumDetailRequest(val albumStringId: String) : BaseRequest<AlbumDetail>() {
    override fun getType(): Type {
        return object : TypeToken<BaseResultData<AlbumDetail>>() {}.type
    }

    override fun getHttpMethod(): HttpMethod {
        val data = hashMapOf<String, Any>("albumId" to albumStringId)
        return HttpMethod.get(HttpUtil.getXiamiUrl(API.XiaMi.GET_ALBUM_DETAIL, data))
    }
}
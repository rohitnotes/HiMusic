package com.wenhaiz.himusic.http.request

import com.google.gson.reflect.TypeToken
import com.wenhaiz.himusic.data.bean.SongDetail
import com.wenhaiz.himusic.http.API
import com.wenhaiz.himusic.http.HttpMethod
import com.wenhaiz.himusic.http.data.BaseResultData
import com.wenhaiz.himusic.http.data.CollectDetail
import java.lang.reflect.Type

class GetSongDetailRequest(private val songId: Long) : BaseRequest<SongDetail>() {
    override fun getType(): Type {
        return object : TypeToken<SongDetail>() {
        }.type
    }

    override fun getHttpMethod(): HttpMethod {
        return HttpMethod.get(API.XiaMi.getSongDetailUrl(songId))
    }
}
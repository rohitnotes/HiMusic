package com.wenhaiz.himusic.http.request

import com.google.gson.reflect.TypeToken
import com.wenhaiz.himusic.data.bean.PlayInfo
import com.wenhaiz.himusic.data.bean.Song
import com.wenhaiz.himusic.http.API
import com.wenhaiz.himusic.http.HttpMethod
import com.wenhaiz.himusic.http.HttpUtil
import com.wenhaiz.himusic.http.data.BaseResultData
import java.lang.reflect.Type

class GetPlayInfoRequest(val songs: List<Song>) : BaseRequest<PlayInfo>() {
    override fun getType(): Type {
        return object : TypeToken<BaseResultData<PlayInfo>>() {}.type
    }

    override fun getHttpMethod(): HttpMethod {
        val ids = songs.map { it.songId }
        val data: HashMap<String, Any> = hashMapOf("songIds" to ids)
        return HttpMethod.get(HttpUtil.getXiamiUrl(API.XiaMi.GET_SONG_PLAY_INFO, data))
    }
}
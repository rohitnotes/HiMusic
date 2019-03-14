package com.wenhaiz.himusic.http.request

import com.google.gson.reflect.TypeToken
import com.wenhaiz.himusic.data.bean.Artist
import com.wenhaiz.himusic.http.API
import com.wenhaiz.himusic.http.HttpMethod
import com.wenhaiz.himusic.http.data.HotSongs
import java.lang.reflect.Type

class GetArtistHotSongsRequest(val artist: Artist, val page: Int = 1) : BaseRequest<HotSongs>() {
    override fun getType(): Type {
        return object : TypeToken<HotSongs>() {}.type
    }

    override fun getHttpMethod(): HttpMethod {
        return HttpMethod.get(API.XiaMi.getArtistHotSongsUrl(artist.artistId, page))
    }
}
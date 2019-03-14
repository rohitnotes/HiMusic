package com.wenhaiz.himusic.http.request

import com.google.gson.reflect.TypeToken
import com.wenhaiz.himusic.data.bean.Artist
import com.wenhaiz.himusic.http.API
import com.wenhaiz.himusic.http.HttpMethod
import com.wenhaiz.himusic.http.data.ArtistDetail
import java.lang.reflect.Type

class GetArtistDetailRequest(val artist: Artist, val page: Int = 1) : BaseRequest<ArtistDetail>() {
    override fun getType(): Type {
        return object : TypeToken<ArtistDetail>() {}.type
    }

    override fun getHttpMethod(): HttpMethod {
        return HttpMethod.get(API.XiaMi.getArtistDetailUrl(artist.artistId,page))
    }

}
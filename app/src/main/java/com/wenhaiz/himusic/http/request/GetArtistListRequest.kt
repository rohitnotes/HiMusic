package com.wenhaiz.himusic.http.request

import com.google.gson.reflect.TypeToken
import com.wenhaiz.himusic.http.API
import com.wenhaiz.himusic.http.HttpMethod
import com.wenhaiz.himusic.http.HttpUtil
import com.wenhaiz.himusic.http.data.Artists
import com.wenhaiz.himusic.http.data.BaseResultData
import java.lang.reflect.Type

class GetArtistListRequest(val language: Language = Language.ALL,
                           val tag: Int = 0,
                           val gender: Int = 0) : BaseRequest<Artists>() {
    override fun getType(): Type {
        return object : TypeToken<BaseResultData<Artists>>() {}.type
    }

    override fun getHttpMethod(): HttpMethod {
        val data: HashMap<String, Any> = hashMapOf(
                "tag" to tag,
                "gender" to gender,
                "language" to language.value
        )
        return HttpMethod.get(HttpUtil.getXiamiUrl(API.XiaMi.GET_ARTIST_LIST, data))
    }

    enum class Language(val value: Int) {
        ALL(0), CN(1), US(4), JP(2),
        KO(3), OTHER(6), MUSICIAN(5)
    }
}
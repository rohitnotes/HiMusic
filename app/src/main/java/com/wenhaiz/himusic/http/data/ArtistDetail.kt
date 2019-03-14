package com.wenhaiz.himusic.http.data

import com.google.gson.annotations.SerializedName

data class ArtistDetail(
        @SerializedName("state") var state: Int = 0,
        @SerializedName("message") var message: String = "",
        @SerializedName("data") var detail: ArtistDetailData
) : BaseData()

data class ArtistDetailData(
        @SerializedName("artist_id") var artistId: String = "",
        @SerializedName("company") var company: String = "",
        @SerializedName("area") var area: String = "",
        @SerializedName("logo") var logo: String = "",
        @SerializedName("artist_name") var artistName: String = ""
)
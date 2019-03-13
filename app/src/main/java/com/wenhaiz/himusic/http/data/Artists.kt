package com.wenhaiz.himusic.http.data

import com.google.gson.annotations.SerializedName
import com.wenhaiz.himusic.data.bean.Artist

data class Artists(
        @SerializedName("labels") val labels: List<Label> = ArrayList(),
        @SerializedName("hotArtists") val hotArtists: List<Artist> = ArrayList()
)

data class Label(
        @SerializedName("type") val type: String = "",
        @SerializedName("items") val items: List<LabelItem> = ArrayList()
)

data class LabelItem(
        @SerializedName("id") val id: Int = 0,
        @SerializedName("name") val name: String = ""
)
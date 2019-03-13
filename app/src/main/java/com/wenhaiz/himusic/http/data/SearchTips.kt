package com.wenhaiz.himusic.http.data

import com.google.gson.annotations.SerializedName

data class SearchTips(
        @SerializedName("objectList") var tips: List<SearchTip> = ArrayList()
)

data class SearchTip(
        @SerializedName("type") var type: String = "",
        @SerializedName("tip") var tip: String = "",
        @SerializedName("scm") var scm: String = ""
)

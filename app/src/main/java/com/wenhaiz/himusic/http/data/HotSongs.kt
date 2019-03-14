package com.wenhaiz.himusic.http.data

import com.google.gson.annotations.SerializedName
import com.wenhaiz.himusic.data.MusicProvider
import com.wenhaiz.himusic.data.bean.Song

data class HotSongs(
        @SerializedName("state") var state: Int = 0,
        @SerializedName("data") var data: List<HotSong> = ArrayList()
) : BaseData()

data class HotSong(
        @SerializedName("song_id") var songId: Long = 0,
        @SerializedName("song_name") var songName: String = "",
        @SerializedName("singers") var singers: String = "",
        @SerializedName("need_pay_flag") var need_pay_flag: Int = 0
) {
    fun toSong(): Song {
        val song = Song()
        song.songId = songId
        song.singers = singers
        song.name = songName
        song.supplier = MusicProvider.XIAMI
        return song
    }
}
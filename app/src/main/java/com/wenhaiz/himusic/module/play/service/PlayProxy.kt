package com.wenhaiz.himusic.module.play.service

import com.wenhaiz.himusic.data.bean.Song

interface PlayProxy {
    fun setNextSong(song: Song): Boolean

    fun playSong(song: Song)
}
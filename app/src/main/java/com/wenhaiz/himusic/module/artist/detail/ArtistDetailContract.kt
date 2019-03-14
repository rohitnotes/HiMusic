package com.wenhaiz.himusic.module.artist.detail

import com.wenhaiz.himusic.base.BasePresenter
import com.wenhaiz.himusic.base.BaseView
import com.wenhaiz.himusic.data.bean.Album
import com.wenhaiz.himusic.data.bean.Artist
import com.wenhaiz.himusic.data.bean.Song

interface ArtistDetailContract {
    interface Presenter : BasePresenter {
        //获取信息和图片
        fun loadArtistDetail(artist: Artist)
        fun loadArtistHotSongs(artist: Artist, page: Int)
    }

    interface View : BaseView<Presenter> {
        fun onArtistDetail(artist: Artist)
        fun onHotSongsLoad(hotSongs: List<Song>)
    }

}
package com.wenhaiz.himusic.module.search

import com.wenhaiz.himusic.base.BasePresenter
import com.wenhaiz.himusic.base.BaseView
import com.wenhaiz.himusic.data.bean.Song

interface SearchContract {
    companion object {
        val SONG_NOT_AVAILABLE = "songNotAvailable"
    }
    interface View : BaseView<Presenter> {
        fun onSearchResult(songs: List<Song>)
        fun onSearchRecommendLoaded(recommends: List<String>)
        fun onSongDetailLoad(song: Song)
    }

    interface Presenter : BasePresenter {
        fun searchByKeyWord(keyword: String)
        fun loadSearchRecommend(keyword: String)
        fun loadSongDetail(song: Song)
    }
}
package com.example.wenhai.listenall.module.search

import com.example.wenhai.listenall.base.BasePresenter
import com.example.wenhai.listenall.base.BaseView
import com.example.wenhai.listenall.data.bean.Song

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
package com.wenhaiz.himusic.module.search

import com.wenhaiz.himusic.data.LoadSearchRecommendCallback
import com.wenhaiz.himusic.data.LoadSearchResultCallback
import com.wenhaiz.himusic.data.MusicRepository
import com.wenhaiz.himusic.data.bean.Song

internal class SearchPresenter(val view: SearchContract.View) : SearchContract.Presenter {
    private val musicRepository: MusicRepository = MusicRepository.getInstance(view.getViewContext())

    init {
        view.setPresenter(this)
    }

    override fun searchByKeyWord(keyword: String) {
        musicRepository.searchByKeyword(keyword, object : LoadSearchResultCallback {
            override fun onStart() {
                view.onLoading()
            }

            override fun onFailure(msg: String) {
                view.onFailure("搜索失败")
            }

            override fun onSuccess(loadedSongs: List<Song>) {
                view.onSearchResult(loadedSongs)
            }

        })

    }

    override fun loadSearchRecommend(keyword: String) {
        musicRepository.loadSearchRecommend(keyword, object : LoadSearchRecommendCallback {
            override fun onStart() {
                //do not call view.onLoading()
            }

            override fun onFailure(msg: String) {
                view.onFailure("获取关键字失败")
            }

            override fun onSuccess(recommendKeyword: List<String>) {
                view.onSearchRecommendLoaded(recommendKeyword)
            }

        })
    }

    override fun loadSongDetail(song: Song) {
//        musicRepository.loadSongDetail(song, object : LoadSongDetailCallback {
//            override fun onStart() {
//            }
//
//            override fun onFailure(msg: String) {
//                view.onFailure(SearchContract.SONG_NOT_AVAILABLE)
//            }
//
//            override fun onSuccess(loadedSong: Song) {
//                view.onSongDetailLoad(loadedSong)
//            }
//
//        })
    }

}
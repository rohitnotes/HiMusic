package com.wenhaiz.himusic.module.search

import com.wenhaiz.himusic.MyApp
import com.wenhaiz.himusic.R
import com.wenhaiz.himusic.data.LoadSearchResultCallback
import com.wenhaiz.himusic.data.LoadSearchTipsCallback
import com.wenhaiz.himusic.data.MusicRepository
import com.wenhaiz.himusic.data.bean.Song
import com.wenhaiz.himusic.http.data.SearchTip

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
                view.onFailure(MyApp.getAppContext().getString(R.string.search_failed))
            }

            override fun onSuccess(loadedSongs: List<Song>) {
                view.onSearchResult(loadedSongs)
            }

        })

    }

    override fun loadSearchRecommend(keyword: String) {
        musicRepository.loadSearchTips(keyword, object : LoadSearchTipsCallback {
            override fun onStart() {
                //do not call view.onLoading()
            }

            override fun onFailure(msg: String) {
                view.onFailure(msg)
            }

            override fun onSuccess(recommendKeyword: List<SearchTip>) {
                view.onSearchRecommendLoaded(recommendKeyword)
            }

        })
    }
}
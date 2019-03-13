package com.wenhaiz.himusic.module.search

import com.wenhaiz.himusic.base.BasePresenter
import com.wenhaiz.himusic.base.BaseView
import com.wenhaiz.himusic.data.bean.Song
import com.wenhaiz.himusic.http.data.SearchTip

interface SearchContract {
    interface View : BaseView<Presenter> {
        fun onSearchResult(songs: List<Song>)
        fun onSearchRecommendLoaded(recommends: List<SearchTip>)
    }

    interface Presenter : BasePresenter {
        fun searchByKeyWord(keyword: String)
        fun loadSearchRecommend(keyword: String)
    }
}
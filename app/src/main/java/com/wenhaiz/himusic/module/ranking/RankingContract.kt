package com.wenhaiz.himusic.module.ranking

import com.wenhaiz.himusic.base.BasePresenter
import com.wenhaiz.himusic.base.BaseView
import com.wenhaiz.himusic.http.data.RankList

interface RankingContract {
    interface Presenter : BasePresenter {
        fun loadRankingList()
    }

    interface View : BaseView<Presenter> {
        fun onRankingListLoad(rankList: RankList)
    }
}
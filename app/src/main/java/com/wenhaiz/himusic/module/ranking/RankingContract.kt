package com.wenhaiz.himusic.module.ranking

import com.wenhaiz.himusic.base.BasePresenter
import com.wenhaiz.himusic.base.BaseView
import com.wenhaiz.himusic.data.MusicProvider
import com.wenhaiz.himusic.data.bean.Collect

interface RankingContract {
    interface Presenter : BasePresenter {
        fun loadOfficialRanking(provider: MusicProvider)
    }

    interface View : BaseView<Presenter> {
        fun onOfficialRankingLoad(collects: List<Collect>)
    }

    enum class GlobalRanking {
        BILLBOARD, UK, ORICON
    }
}
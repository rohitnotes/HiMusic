package com.wenhaiz.himusic.module.ranking

import com.wenhaiz.himusic.data.LoadRankingCallback
import com.wenhaiz.himusic.data.MusicRepository
import com.wenhaiz.himusic.http.data.RankList

class RankingPresenter(val view: RankingContract.View) : RankingContract.Presenter {

    private val musicRepository = MusicRepository.getInstance(view.getViewContext())

    init {
        view.setPresenter(this)
    }

    override fun loadRankingList() {
        musicRepository.loadOfficialRanking(object : LoadRankingCallback {
            override fun onSuccess(rankList: RankList) {
                view.onRankingListLoad(rankList)
            }

            override fun onStart() {
                view.onLoading()
            }

            override fun onFailure(msg: String) {
                view.onFailure(msg)
            }
        })
    }
}

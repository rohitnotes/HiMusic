package com.wenhaiz.himusic.module.ranking

import com.wenhaiz.himusic.data.LoadRankingCallback
import com.wenhaiz.himusic.data.MusicProvider
import com.wenhaiz.himusic.data.MusicRepository
import com.wenhaiz.himusic.data.bean.Collect

class RankingPresenter(val view: RankingContract.View) : RankingContract.Presenter {

    private val musicRepository = MusicRepository.getInstance(view.getViewContext())

    init {
        view.setPresenter(this)
    }

    override fun loadOfficialRanking(provider: MusicProvider) {
        musicRepository.changeMusicSource(provider, view.getViewContext())
        musicRepository.loadOfficialRanking(provider, object : LoadRankingCallback {
            override fun onStart() {
                view.onLoading()
            }

            override fun onSuccess(collects: List<Collect>) {
                view.onOfficialRankingLoad(collects)
            }

            override fun onFailure(msg: String) {
                view.onFailure(msg)
            }
        })
    }
}

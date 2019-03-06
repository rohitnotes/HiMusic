package com.wenhaiz.himusic.module.playhistory

import android.content.Context
import com.wenhaiz.himusic.data.bean.PlayHistory
import com.wenhaiz.himusic.data.bean.PlayHistory_
import com.wenhaiz.himusic.utils.BoxUtil


internal class PlayHistoryPresenter(val view: PlayHistoryContract.View) : PlayHistoryContract.Presenter {
    init {
        view.setPresenter(this)
    }

    override fun loadPlayHistory(context: Context) {
        val box = BoxUtil.getBoxStore(context).boxFor(PlayHistory::class.java)
        val playHistoryList = box.query().orderDesc(PlayHistory_.playTimeInMills)
                .build()
                .find()
        if (playHistoryList.size > 0) {
            view.onPlayHistoryLoad(playHistoryList)
        } else {
            view.onNoPlayHistory()
        }
    }


}
package com.example.wenhai.listenall.module.playhistory

import android.content.Context
import com.example.wenhai.listenall.data.bean.PlayHistory
import com.example.wenhai.listenall.data.bean.PlayHistory_
import com.example.wenhai.listenall.utils.BoxUtil


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
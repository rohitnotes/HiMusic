package com.wenhaiz.himusic.module.playhistory

import android.content.Context
import com.wenhaiz.himusic.base.BasePresenter
import com.wenhaiz.himusic.base.BaseView
import com.wenhaiz.himusic.data.bean.PlayHistory


interface PlayHistoryContract {
    interface View : BaseView<Presenter> {
        fun onPlayHistoryLoad(playHistory: List<PlayHistory>)
        fun onNoPlayHistory()
    }

    interface Presenter : BasePresenter {
        fun loadPlayHistory(context: Context)
    }
}
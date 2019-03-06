package com.wenhaiz.himusic.module.collectlist

import com.wenhaiz.himusic.base.BasePresenter
import com.wenhaiz.himusic.base.BaseView
import com.wenhaiz.himusic.data.bean.Collect

interface CollectListContract {
    interface View : BaseView<Presenter> {
        fun setCollects(collects: List<Collect>)

    }

    interface Presenter : BasePresenter {
        fun loadCollects(page: Int)
    }
}
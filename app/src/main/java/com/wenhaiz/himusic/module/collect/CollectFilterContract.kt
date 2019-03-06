package com.wenhaiz.himusic.module.collect

import com.wenhaiz.himusic.base.BasePresenter
import com.wenhaiz.himusic.base.BaseView
import com.wenhaiz.himusic.data.bean.Collect


interface CollectFilterContract {
    interface View : BaseView<Presenter> {
        fun onCollectLoad(collects: List<Collect>)
    }

    interface Presenter : BasePresenter {
        fun loadCollectByCategory(category: String, page: Int)
    }
}
package com.wenhaiz.himusic.module.collect

import com.wenhaiz.himusic.data.LoadCollectByCategoryCallback
import com.wenhaiz.himusic.data.MusicRepository
import com.wenhaiz.himusic.data.bean.Collect


internal class CollectFilterPresenter(val view: CollectFilterContract.View) : CollectFilterContract.Presenter {

    private val musicRepository = MusicRepository.getInstance(view.getViewContext())

    init {
        view.setPresenter(this)
    }

    override fun loadCollectByCategory(category: String, page: Int) {
        musicRepository.loadCollectByCategory(category, page, object : LoadCollectByCategoryCallback {
            override fun onStart() {
                view.onLoading()
            }

            override fun onFailure(msg: String) {
                view.onFailure("获取歌单失败")
            }

            override fun onSuccess(collects: List<Collect>) {
                view.onCollectLoad(collects)
            }

        })

    }
}
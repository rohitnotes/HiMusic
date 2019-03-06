package com.wenhaiz.himusic.module.main.online

import com.wenhaiz.himusic.base.BasePresenter
import com.wenhaiz.himusic.base.BaseView
import com.wenhaiz.himusic.data.MusicProvider
import com.wenhaiz.himusic.data.bean.Album
import com.wenhaiz.himusic.data.bean.Banner
import com.wenhaiz.himusic.data.bean.Collect

interface OnLineContract {
    interface View : BaseView<Presenter> {
        fun onBannerLoad(banners: List<Banner>)
        fun onHotCollectsLoad(hotCollects: List<Collect>)
        fun onNewAlbumsLoad(newAlbums: List<Album>)
    }

    interface Presenter : BasePresenter {
        fun loadBanner(provider: MusicProvider)
        fun loadHotCollects()
        fun loadNewAlbums()
    }
}
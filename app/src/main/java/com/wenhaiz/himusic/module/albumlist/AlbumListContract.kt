package com.wenhaiz.himusic.module.albumlist

import com.wenhaiz.himusic.base.BasePresenter
import com.wenhaiz.himusic.base.BaseView
import com.wenhaiz.himusic.data.bean.Album

interface AlbumListContract {
    interface Presenter : BasePresenter {
        fun loadNewAlbums(page: Int)
    }

    interface View : BaseView<Presenter> {
        fun onNewAlbumsLoad(albumList: List<Album>)
    }
}
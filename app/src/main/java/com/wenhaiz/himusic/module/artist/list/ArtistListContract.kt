package com.wenhaiz.himusic.module.artist.list

import com.wenhaiz.himusic.base.BasePresenter
import com.wenhaiz.himusic.base.BaseView
import com.wenhaiz.himusic.data.ArtistRegion
import com.wenhaiz.himusic.data.bean.Artist


interface ArtistListContract {
    interface Presenter : BasePresenter {
        fun loadArtists(region: ArtistRegion, page: Int)

    }

    interface View : BaseView<Presenter> {
        fun onArtistsLoad(artists: List<Artist>)

    }
}
package com.wenhaiz.himusic.module.artist.list

import com.wenhaiz.himusic.data.ArtistRegion
import com.wenhaiz.himusic.data.LoadArtistsCallback
import com.wenhaiz.himusic.data.MusicRepository
import com.wenhaiz.himusic.data.bean.Artist

internal class ArtistListPresenter(val view: ArtistListContract.View) : ArtistListContract.Presenter {
    private val musicRepository: MusicRepository = MusicRepository.getInstance(view.getViewContext())

    init {
        view.setPresenter(this)
    }

    override fun loadArtists(region: ArtistRegion, page: Int) {
        musicRepository.loadArtists(region, page, object : LoadArtistsCallback {
            override fun onStart() {
                view.onLoading()
            }

            override fun onFailure(msg: String) {
                view.onFailure("获取艺人列表失败")
            }

            override fun onSuccess(artists: List<Artist>) {
                view.onArtistsLoad(artists)
            }

        })
    }
}
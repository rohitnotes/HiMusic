package com.wenhaiz.himusic.module.main.online

import com.wenhaiz.himusic.data.LoadAlbumCallback
import com.wenhaiz.himusic.data.LoadBannerCallback
import com.wenhaiz.himusic.data.LoadCollectCallback
import com.wenhaiz.himusic.data.MusicProvider
import com.wenhaiz.himusic.data.MusicRepository
import com.wenhaiz.himusic.data.bean.Album
import com.wenhaiz.himusic.data.bean.Banner
import com.wenhaiz.himusic.data.bean.Collect

internal class OnLinePresenter(var view: OnLineContract.View) : OnLineContract.Presenter {

    private var musicRepository: MusicRepository = MusicRepository.getInstance(view.getViewContext())

    init {
        view.setPresenter(this)
    }

    override fun loadBanner(provider: MusicProvider) {
        musicRepository.loadBanner(object : LoadBannerCallback {
            override fun onStart() {
            }

            override fun onSuccess(banners: List<Banner>) {
                view.onBannerLoad(banners)
            }

            override fun onFailure(msg: String) {
                view.onFailure(msg)
            }
        })
    }

    override fun loadHotCollects() {
        musicRepository.loadHotCollect(1, object : LoadCollectCallback {
            override fun onStart() {
                view.onLoading()
            }

            override fun onFailure(msg: String) {
                view.onFailure(msg)
            }

            override fun onSuccess(collectList: List<Collect>) {
                view.onHotCollectsLoad(collectList)
            }

        })
    }

    override fun loadNewAlbums() {
        musicRepository.loadNewAlbum(1, object : LoadAlbumCallback {
            override fun onStart() {
                view.onLoading()
            }

            override fun onFailure(msg: String) {
                view.onFailure(msg)
            }

            override fun onSuccess(albumList: List<Album>) {
                view.onNewAlbumsLoad(albumList)
            }

        })
    }

    companion object {
        const val TAG = "OnLinePresenter"
    }
}
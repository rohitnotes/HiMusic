package com.wenhaiz.himusic.module.artist.detail

import com.wenhaiz.himusic.data.LoadArtistDetailCallback
import com.wenhaiz.himusic.data.LoadArtistHotSongsCallback
import com.wenhaiz.himusic.data.MusicRepository
import com.wenhaiz.himusic.data.bean.Artist
import com.wenhaiz.himusic.data.bean.Song

internal class ArtistDetailPresenter(val view: ArtistDetailContract.View) : ArtistDetailContract.Presenter {


    private val musicRepository: MusicRepository = MusicRepository.getInstance(view.getViewContext())

    init {
        view.setPresenter(this)
    }

    override fun loadArtistHotSongs(artist: Artist, page: Int) {
        musicRepository.loadArtistHotSongs(artist, page, object : LoadArtistHotSongsCallback {
            override fun onStart() {
            }

            override fun onFailure(msg: String) {
                view.onFailure(msg)
            }


            override fun onSuccess(hotSongs: List<Song>) {
                if (hotSongs.isNotEmpty()) {
                    view.onHotSongsLoad(hotSongs)
                }
            }

        })

    }

    override fun loadArtistDetail(artist: Artist) {
        musicRepository.loadArtistDetail(artist, object : LoadArtistDetailCallback {
            override fun onStart() {
            }

            override fun onFailure(msg: String) {
                view.onFailure(msg)
            }

            override fun onSuccess(artistDetail: Artist) {
                view.onArtistDetail(artistDetail)
            }

        })

    }

}
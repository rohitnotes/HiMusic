package com.wenhaiz.himusic.module.artist.detail

import com.wenhaiz.himusic.data.LoadArtistAlbumsCallback
import com.wenhaiz.himusic.data.LoadArtistDetailCallback
import com.wenhaiz.himusic.data.LoadArtistHotSongsCallback
import com.wenhaiz.himusic.data.MusicRepository
import com.wenhaiz.himusic.data.bean.Album
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

    override fun loadArtistAlbums(artist: Artist, page: Int) {
        musicRepository.loadArtistAlbums(artist, page, object : LoadArtistAlbumsCallback {

            override fun onStart() {
            }

            override fun onFailure(msg: String) {
                view.onFailure(msg)
            }

            override fun onSuccess(albums: List<Album>) {
                if (albums.isNotEmpty()) {
                    view.onAlbumsLoad(albums)
                }
            }

        })

    }

    override fun loadArtistDetail(artist: Artist) {
        musicRepository.loadArtistDetail(artist, object : LoadArtistDetailCallback {
            override fun onStart() {
            }

            override fun onFailure(msg: String) {
                view.onFailure("获取艺人详情失败")
            }

            override fun onSuccess(artistDetail: Artist) {
                view.onArtistDetail(artistDetail)
            }

        })

    }

    override fun loadSongDetail(song: Song) {
//        musicRepository.loadSongDetails(song, object : LoadSongDetailCallback {
//            override fun onStart() {
//            }
//
//            override fun onFailure(msg: String) {
//                view.onFailure("当前歌曲无法播放")
//            }
//
//            override fun onSuccess(loadedSong: Song) {
//                view.onSongDetailLoaded(loadedSong)
//            }
//        })
    }

}
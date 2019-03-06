package com.wenhaiz.himusic.module.detail

import com.wenhaiz.himusic.MyApp
import com.wenhaiz.himusic.data.LoadAlbumDetailCallback
import com.wenhaiz.himusic.data.LoadCollectDetailCallback
import com.wenhaiz.himusic.data.LoadSingleRankingCallback
import com.wenhaiz.himusic.data.LoadSongDetailCallback
import com.wenhaiz.himusic.data.MusicRepository
import com.wenhaiz.himusic.data.bean.Album
import com.wenhaiz.himusic.data.bean.Collect
import com.wenhaiz.himusic.data.bean.Collect_
import com.wenhaiz.himusic.data.bean.Song
import com.wenhaiz.himusic.module.ranking.RankingContract

internal class DetailPresenter(val view: DetailContract.View) : DetailContract.Presenter {

    private val musicRepository: MusicRepository = MusicRepository.getInstance(view.getViewContext())

    init {
        view.setPresenter(this)
    }

    override fun loadAlbumDetail(id: Long) {
        musicRepository.loadAlbumDetail(id, object : LoadAlbumDetailCallback {
            override fun onStart() {
                view.onLoading()
            }

            override fun onFailure(msg: String) {
                view.onFailure(msg)
            }

            override fun onSuccess(album: Album) {
                view.onAlbumDetailLoad(album)
            }

        })
    }

    override fun loadCollectDetail(id: Long, isFromUser: Boolean) {
        if (isFromUser) {
            view.onLoading()
            val collectBox = MyApp.getBoxStore().boxFor(Collect::class.java)
            val collect: Collect? = collectBox.query().equal(Collect_.id, id).build().findFirst()
            if (collect == null) {
                view.onFailure("没有找到该歌单信息！")
            } else {
                view.onCollectDetailLoad(collect)
            }

        } else {
            musicRepository.loadCollectDetail(id, object : LoadCollectDetailCallback {
                override fun onStart() {
                    view.onLoading()
                }

                override fun onFailure(msg: String) {
                    view.onFailure(msg)
                }

                override fun onSuccess(collect: Collect) {
                    view.onCollectDetailLoad(collect)
                }

            })
        }

    }

    override fun loadSongDetail(id: Long) {
        val song = Song()
        song.songId = id
        musicRepository.loadSongDetail(song, object : LoadSongDetailCallback {
            override fun onStart() {
                view.onLoading()
            }

            override fun onFailure(msg: String) {
                view.onFailure(msg)
            }

            override fun onSuccess(loadedSong: Song) {
                //加载专辑详情
                loadAlbumDetail(song.albumId)
            }

        })
    }


    override fun loadGlobalRanking(ranking: RankingContract.GlobalRanking) {
        musicRepository.loadGlobalRanking(ranking, object : LoadSingleRankingCallback {
            override fun onStart() {
                view.onLoading()

            }

            override fun onSuccess(collect: Collect) {
                view.onGlobalRankingLoad(collect)
            }

            override fun onFailure(msg: String) {
                view.onFailure(msg)
            }
        })

    }

    companion object {
        const val TAG = "DetailPresenter"
    }

}
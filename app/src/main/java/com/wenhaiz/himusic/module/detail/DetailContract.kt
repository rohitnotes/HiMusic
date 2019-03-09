package com.wenhaiz.himusic.module.detail

import com.wenhaiz.himusic.base.BasePresenter
import com.wenhaiz.himusic.base.BaseView
import com.wenhaiz.himusic.data.bean.Album
import com.wenhaiz.himusic.data.bean.Collect
import com.wenhaiz.himusic.data.bean.Song
import com.wenhaiz.himusic.http.data.AlbumDetail
import com.wenhaiz.himusic.http.data.CollectDetail
import com.wenhaiz.himusic.module.ranking.RankingContract
import java.io.Serializable

interface DetailContract {

    interface View : BaseView<Presenter> {
        fun onCollectDetailLoad(collect: CollectDetail)
        fun onAlbumDetailLoad(album: Album)
        fun onGlobalRankingLoad(collect: Collect)
    }

    interface Presenter : BasePresenter {
        fun loadAlbumDetail(album: Album)
        fun loadCollectDetail(id: Long, isFromUser: Boolean)
//        fun loadSongDetail(song: Song)
        fun loadGlobalRanking(ranking: RankingContract.GlobalRanking)
    }

    enum class LoadType : Serializable {
        SONG, COLLECT, ALBUM,
        GLOBAL_RANKING, OFFICIAL_RANKING;
    }

    companion object {
        const val ARGS_ID = "id"
        const val ARGS_COLLECT = "collect"
        const val ARGS_IS_USER_COLLECT = "isUserCollect"
        const val ARGS_LOAD_TYPE = "type"
        const val ARGS_GLOBAL_RANKING = "ranking"
    }
}
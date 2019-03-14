package com.wenhaiz.himusic.data

import com.wenhaiz.himusic.base.BaseCallBack
import com.wenhaiz.himusic.data.bean.Album
import com.wenhaiz.himusic.data.bean.Artist
import com.wenhaiz.himusic.data.bean.Collect
import com.wenhaiz.himusic.data.bean.Song
import com.wenhaiz.himusic.http.data.RankList
import com.wenhaiz.himusic.http.data.SearchTip

/**
 * 音乐数据接口类
 *
 * Created by Wenhai on 2017/8/4.
 */
interface MusicSource {
    fun loadHotCollect(page: Int = 1, callback: LoadCollectCallback)
    fun loadNewAlbum(page: Int = 1, callback: LoadAlbumCallback)
    fun loadCollectDetail(collect: Collect, callback: LoadCollectDetailCallback)
    fun loadAlbumDetail(album: Album, callback: LoadAlbumDetailCallback)
    fun loadSongDetail(song: Song, callback: LoadSongDetailCallback)
    fun searchByKeyword(keyword: String, callback: LoadSearchResultCallback)
    fun loadSearchTips(keyword: String, callback: LoadSearchTipsCallback)
    fun loadArtistDetail(artist: Artist, callback: LoadArtistDetailCallback)
    fun loadArtistHotSongs(artist: Artist, page: Int, callback: LoadArtistHotSongsCallback)
    fun loadArtistAlbums(artist: Artist, page: Int, callback: LoadArtistAlbumsCallback)
    fun loadCollectByCategory(category: String, page: Int, callback: LoadCollectByCategoryCallback)
    fun loadRankingList(callback: LoadRankingCallback)
    fun loadRankingDetail(rank: RankList.Rank, callback: LoadRankingDetailCallback)
}


//callbacks
interface LoadCollectCallback : BaseCallBack {
    fun onSuccess(collectList: List<Collect>)
}

interface LoadAlbumCallback : BaseCallBack {
    fun onSuccess(albumList: List<Album>)
}

interface LoadCollectDetailCallback : BaseCallBack {
    fun onSuccess(collect: Collect)
}

interface LoadAlbumDetailCallback : BaseCallBack {
    fun onSuccess(album: Album)
}

interface LoadSongDetailCallback : BaseCallBack {
    fun onSuccess(loadedSong: Song)
}

interface LoadSearchResultCallback : BaseCallBack {
    fun onSuccess(loadedSongs: List<Song>)
}

interface LoadSearchTipsCallback : BaseCallBack {
    fun onSuccess(recommendKeyword: List<SearchTip>)
}

interface LoadArtistDetailCallback : BaseCallBack {
    fun onSuccess(artistDetail: Artist)
}

interface LoadArtistHotSongsCallback : BaseCallBack {
    fun onSuccess(hotSongs: List<Song>)
}

interface LoadArtistAlbumsCallback : BaseCallBack {
    fun onSuccess(albums: List<Album>)
}

interface LoadCollectByCategoryCallback : BaseCallBack {
    fun onSuccess(collects: List<Collect>)
}

interface LoadRankingCallback : BaseCallBack {
    fun onSuccess(rankList: RankList)
}

interface LoadRankingDetailCallback : BaseCallBack {
    fun onSuccess(rank: RankList.Rank)
}
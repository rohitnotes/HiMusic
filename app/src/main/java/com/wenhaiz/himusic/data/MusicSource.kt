package com.wenhaiz.himusic.data

import com.wenhaiz.himusic.base.BaseCallBack
import com.wenhaiz.himusic.data.bean.Album
import com.wenhaiz.himusic.data.bean.Artist
import com.wenhaiz.himusic.data.bean.Banner
import com.wenhaiz.himusic.data.bean.Collect
import com.wenhaiz.himusic.data.bean.Song
import com.wenhaiz.himusic.http.data.AlbumDetail
import com.wenhaiz.himusic.http.data.CollectDetail
import com.wenhaiz.himusic.module.ranking.RankingContract

/**
 * 音乐数据接口类
 *
 * Created by Wenhai on 2017/8/4.
 */
interface MusicSource {
    fun loadBanner(callback: LoadBannerCallback)
    fun loadHotCollect(page: Int = 1, callback: LoadCollectCallback)
    fun loadNewAlbum(page: Int = 1, callback: LoadAlbumCallback)
    fun loadCollectDetail(collect: Collect, callback: LoadCollectDetailCallback)
    fun loadAlbumDetail(album: Album, callback: LoadAlbumDetailCallback)
    fun loadSongDetail(song: Song, callback: LoadSongDetailCallback)
    fun searchByKeyword(keyword: String, callback: LoadSearchResultCallback)
    fun loadSearchRecommend(keyword: String, callback: LoadSearchRecommendCallback)
    fun loadArtists(region: ArtistRegion, page: Int, callback: LoadArtistsCallback)
    fun loadArtistDetail(artist: Artist, callback: LoadArtistDetailCallback)
    fun loadArtistHotSongs(artist: Artist, page: Int, callback: LoadArtistHotSongsCallback)
    fun loadArtistAlbums(artist: Artist, page: Int, callback: LoadArtistAlbumsCallback)
    fun loadCollectByCategory(category: String, page: Int, callback: LoadCollectByCategoryCallback)
    fun loadOfficialRanking(provider: MusicProvider, callback: LoadRankingCallback)
    fun loadGlobalRanking(ranking: RankingContract.GlobalRanking, callback: LoadSingleRankingCallback)
}


//callbacks
interface LoadBannerCallback : BaseCallBack {
    fun onSuccess(banners: List<Banner>)
}

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

interface LoadSearchRecommendCallback : BaseCallBack {
    fun onSuccess(recommendKeyword: List<String>)
}

interface LoadArtistsCallback : BaseCallBack {
    fun onSuccess(artists: List<Artist>)
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
    fun onSuccess(collects: List<Collect>)
}

interface LoadSingleRankingCallback : BaseCallBack {
    fun onSuccess(collect: Collect)
}
package com.wenhaiz.himusic.data.onlineprovider

import android.content.Context
import com.wenhaiz.himusic.R
import com.wenhaiz.himusic.data.*
import com.wenhaiz.himusic.data.bean.Album
import com.wenhaiz.himusic.data.bean.Artist
import com.wenhaiz.himusic.data.bean.Banner
import com.wenhaiz.himusic.data.bean.BannerType
import com.wenhaiz.himusic.data.bean.Collect
import com.wenhaiz.himusic.data.bean.Song
import com.wenhaiz.himusic.data.bean.SongDetail
import com.wenhaiz.himusic.http.data.AlbumDetail
import com.wenhaiz.himusic.http.data.Artists
import com.wenhaiz.himusic.http.data.CollectDetail
import com.wenhaiz.himusic.http.data.RankDetail
import com.wenhaiz.himusic.http.data.RankList
import com.wenhaiz.himusic.http.data.RecommendListNewAlbumInfo
import com.wenhaiz.himusic.http.data.RecommendListRecommendInfo
import com.wenhaiz.himusic.http.data.SearchSongResult
import com.wenhaiz.himusic.http.data.SearchTips
import com.wenhaiz.himusic.http.request.*
import com.wenhaiz.himusic.utils.BaseResponseCallback
import com.wenhaiz.himusic.utils.OkHttpUtil
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.net.URLDecoder
import java.net.URLEncoder

/**
 * 虾米音乐
 * Created by Wenhai on 2017/8/4.
 */
class Xiami(val context: Context) : MusicSource {
    companion object {
        val TAG = "Xiami"

        val URL_HOME = "http://www.xiami.com"
        val CATEGORY_HOT_COLLECT = "热门歌单"

        /**
         *parse "location" string and get listen file url
         */
        fun getListenUrlFromLocation(location: String): String {
            val num = location[0] - '0'
            val avgLen = Math.floor((location.substring(1).length / num).toDouble()).toInt()
            val remainder = location.substring(1).length % num

            val result = ArrayList<String>()
            (0 until remainder).mapTo(result) { location.substring(it * (avgLen + 1) + 1, (it + 1) * (avgLen + 1) + 1) }
            (0 until num - remainder).mapTo(result) { location.substring((avgLen + 1) * remainder).substring(it * avgLen + 1, (it + 1) * avgLen + 1) }

            val s = ArrayList<String>()
            for (i in 0 until avgLen) {
                (0 until num).mapTo(s) { result[it][i].toString() }
            }
            (0 until remainder).mapTo(s) { result[it][result[it].length - 1].toString() }

            val joinStr = s.joinToString("")
            return URLDecoder.decode(joinStr, "utf-8").replace("^", "0")
        }

        fun maskUrl(url: String): String {
            return if (url.startsWith("//")) {
                "https:$url"
            } else {
                url
            }
        }
    }


    override fun loadBanner(callback: LoadBannerCallback) {
        val url = URL_HOME
        OkHttpUtil.getForXiami(context, url, object : BaseResponseCallback() {
            override fun onStart() {
                callback.onStart()
            }

            override fun onHtmlResponse(html: String) {
                val banners = parseBanners(html)
                callback.onSuccess(banners)
            }

            override fun onFailure(msg: String) {
                super.onFailure(msg)
                callback.onFailure(msg)
            }

        })
    }

    fun parseBanners(html: String): List<Banner> {
        val document = Jsoup.parse(html)
        val slider: Element? = document.getElementById("slider")
        val items: Elements = slider!!.getElementsByClass("item")
        val banners = ArrayList<Banner>()
        for (item in items) {
            val banner = Banner()
            val a = item.select("a").first()
            val imgUrl = a.select("img").first().attr("src")
            banner.imgUrl = maskUrl(imgUrl)
//            LogUtil.d(TAG, banner.imgUrl)
            val href = a.attr("href")
            when {
                href.contains("album/") -> {
                    banner.type = BannerType.ALBUM
                    banner.id = href.substring(href.lastIndexOf("/") + 1).toLong()
                }
                href.contains("song/") -> {
                    banner.type = BannerType.SONG
                    banner.id = href.substring(href.lastIndexOf("/") + 1).toLong()
                }
                else -> {
                    banner.type = BannerType.OTHER
                    banner.id = 0
                }
            }
            banners.add(banner)
        }
        return banners
    }

    override fun loadHotCollect(page: Int, callback: LoadCollectCallback) {
        GetRecommendCollectRequest()
                .setDataCallback(object : BaseRequest.BaseDataCallback<RecommendListRecommendInfo>() {
                    override fun onSuccess(data: RecommendListRecommendInfo) {
                        data.collects.forEach {
                            it.source = MusicProvider.XIAMI
                        }
                        callback.onSuccess(data.collects)
                    }

                    override fun onFailure(code: String?, msg: String?) {
                        callback.onFailure(msg ?: "")
                    }

                    override fun beforeRequest() {
                        callback.onStart()
                    }

                })
                .send()
    }

    override fun loadNewAlbum(page: Int, callback: LoadAlbumCallback) {
        GetRecommendAlbumRequest()
                .setDataCallback(object : BaseRequest.BaseDataCallback<RecommendListNewAlbumInfo>() {
                    override fun onSuccess(data: RecommendListNewAlbumInfo) {
                        callback.onSuccess(data.albums)
                    }

                    override fun onFailure(code: String?, msg: String?) {
                        callback.onFailure(msg ?: "")
                    }

                    override fun beforeRequest() {
                        callback.onStart()
                    }

                }).send()

    }

    override fun loadCollectDetail(collect: Collect, callback: LoadCollectDetailCallback) {
        GetCollectDetailRequest(collect.collectId).setDataCallback(object : BaseRequest.BaseDataCallback<CollectDetail>() {
            override fun onSuccess(data: CollectDetail) {
                collect.addDetail(data)
                collect.songs.forEach {
                    it.supplier = MusicProvider.XIAMI
                }
                callback.onSuccess(collect)
            }

            override fun onFailure(code: String?, msg: String?) {
                callback.onFailure(msg ?: "")
            }

            override fun beforeRequest() {
                callback.onStart()
            }

        }).send()
    }


    override fun loadRankingDetail(rank: RankList.Rank, callback: LoadRankingDetailCallback) {
        GetRankDetailRequest(rank).setDataCallback(object : BaseRequest.BaseDataCallback<RankDetail>() {
            override fun onSuccess(data: RankDetail) {
                data.detail.songs.forEach {
                    it.supplier = MusicProvider.XIAMI
                }
                rank.addDetail(data.detail)
                callback.onSuccess(rank)
            }

            override fun onFailure(code: String?, msg: String?) {
                callback.onFailure(msg ?: "")
            }

            override fun beforeRequest() {
                callback.onStart()
            }

        }).send()
    }

    override fun loadSongDetail(song: Song, callback: LoadSongDetailCallback) {
        GetSongDetailRequest(song.songId)
                .setDataCallback(object : BaseRequest.BaseDataCallback<SongDetail>() {
                    override fun onSuccess(data: SongDetail) {
                        song.addDetailInfo(data.data)
                        callback.onSuccess(song)
                    }

                    override fun onFailure(code: String?, msg: String?) {
                        callback.onFailure(msg ?: "")
                    }

                    override fun beforeRequest() {
                        callback.onStart()
                    }
                })
                .send()
    }

//    override fun loadPlayInfo(songs: List<Song>, callback: LoadPlayInfoCallback) {
//        GetPlayInfoRequest(songs).setDataCallback(object : BaseRequest.BaseDataCallback<PlayInfo>() {
//            override fun onSuccess(data: PlayInfo) {
//                LogUtil.d(TAG, "play info loaded!")
//                data.infos.forEach {
//                    val songId = it.songId
//                    val song = songs.find { song ->
//                        song.songId == songId
//                    }
//                    song?.addPlayInfo(it.playInfos)
//                }
//                callback.onSuccess(songs)
//            }
//
//            override fun onFailure(code: String?, msg: String?) {
//                callback.onFailure(msg ?: "")
//            }
//
//            override fun beforeRequest() {
//                callback.onStart()
//            }
//
//        }).send()
//
//    }


    override fun loadAlbumDetail(album: Album, callback: LoadAlbumDetailCallback) {
        GetAlbumDetailRequest(album.albumStringID)
                .setDataCallback(object : BaseRequest.BaseDataCallback<AlbumDetail>() {
                    override fun onSuccess(data: AlbumDetail) {
                        album.addDetail(data.albumDetail)
                        album.songs.forEach {
                            it.supplier = MusicProvider.XIAMI
                        }
                        callback.onSuccess(album)
                    }

                    override fun onFailure(code: String?, msg: String?) {
                        callback.onFailure(msg ?: "")
                    }

                    override fun beforeRequest() {
                        callback.onStart()
                    }

                })
                .send()
    }

    override fun searchByKeyword(keyword: String, callback: LoadSearchResultCallback) {
        SearchSongsRequest(keyword)
                .setDataCallback(object : BaseRequest.BaseDataCallback<SearchSongResult>() {
                    override fun onSuccess(data: SearchSongResult) {
                        if (data.songs == null || data.songs.isEmpty()) {
                            callback.onFailure(context.getString(R.string.no_search_result))
                            return
                        }
                        data.songs.forEach {
                            it.supplier = MusicProvider.XIAMI
                        }
                        callback.onSuccess(data.songs)
                    }

                    override fun onFailure(code: String?, msg: String?) {
                        callback.onFailure(msg ?: "")
                    }

                    override fun beforeRequest() {
                        callback.onStart()
                    }

                })
                .send()
    }

    override fun loadSearchTips(keyword: String, callback: LoadSearchTipsCallback) {
        GetSearchTipsRequest(keyword).setDataCallback(
                object : BaseRequest.BaseDataCallback<SearchTips>() {
                    override fun onSuccess(data: SearchTips) {
                        callback.onSuccess(data.tips)
                    }

                    override fun onFailure(code: String?, msg: String?) {
                        callback.onFailure(msg ?: "")
                    }

                    override fun beforeRequest() {
                        callback.onStart()
                    }

                }
        ).send()
    }

    override fun loadArtists(language: GetArtistListRequest.Language,
                             page: Int, callback: LoadArtistsCallback) {
        GetArtistListRequest(language)
                .setDataCallback(object : BaseRequest.BaseDataCallback<Artists>() {
                    override fun onSuccess(data: Artists) {
                        callback.onSuccess(data.hotArtists)
                    }

                    override fun onFailure(code: String?, msg: String?) {
                        callback.onFailure(msg ?: "")
                    }

                    override fun beforeRequest() {
                        callback.onStart()
                    }

                })
                .send()
    }

    override fun loadArtistDetail(artist: Artist, callback: LoadArtistDetailCallback) {
        val url = URL_HOME + "/artist/" + artist.artistId
        OkHttpUtil.getForXiami(context, url, object : BaseResponseCallback() {
            override fun onStart() {
                callback.onStart()
            }

            override fun onHtmlResponse(html: String) {
                super.onHtmlResponse(html)
                val detailedArtist = parseAndAddArtistDetail(html, artist)
                callback.onSuccess(detailedArtist)
            }

            override fun onFailure(msg: String) {
                super.onFailure(msg)
                callback.onFailure(msg)
            }

        })
    }

    //get desc and imgUrl
    private fun parseAndAddArtistDetail(html: String, artist: Artist): Artist {
        val document = Jsoup.parse(html)
        val block = document.getElementById("artist_block")
        val info = block.getElementById("artist_info")
        val desc = info.select("tr").last()
                .getElementsByClass("record").first()
                .text()
        artist.desc = desc
        val img = block.getElementById("artist_photo")
        val imgUrl = img.select("a").first().attr("href")
        artist.imgUrl = imgUrl
        return artist
    }

    override fun loadArtistHotSongs(artist: Artist, page: Int, callback: LoadArtistHotSongsCallback) {
        val url = URL_HOME + "/artist/top-" + artist.artistId + "?page=$page"
        OkHttpUtil.getForXiami(context, url, object : BaseResponseCallback() {
            override fun onStart() {
                callback.onStart()
            }

            override fun onHtmlResponse(html: String) {
                super.onHtmlResponse(html)
                try {
                    val hotSongs = parseArtistHotSongs(artist, html)
                    callback.onSuccess(hotSongs)
                } catch (e: NullPointerException) {
                    callback.onFailure("没有更多歌曲了")
                }
            }

            override fun onFailure(msg: String) {
                super.onFailure(msg)
                callback.onFailure(msg)
            }

        })
    }

    private fun parseArtistHotSongs(artist: Artist, html: String): List<Song> {
        val document = Jsoup.parse(html)
        val songs = ArrayList<Song>()
        val trackList = document.getElementsByClass("track_list").first()
        val tracks = trackList.select("tr")
        for (track in tracks) {
            val song = Song()
            song.name = track.getElementsByClass("song_name").first()
                    .select("a").first()
                    .attr("title")
            val onClick = track.getElementsByClass("song_act").first()
                    .getElementsByClass("song_play").first()
                    .attr("onClick")
            val extra = track.getElementsByClass("song_name").first()
                    .getElementsByClass("show_zhcn").first()
            if (extra != null) {
                //临时显示用
                song.albumName = extra.text()
            } else {
                song.albumName = ""
            }
            val songId = onClick.substring(onClick.indexOf("'") + 1, onClick.indexOf(",") - 1)
            song.songId = songId.toLong()
            song.artistName = artist.artistName
            song.supplier = MusicProvider.XIAMI
            songs.add(song)
        }
        return songs
    }

    override fun loadArtistAlbums(artist: Artist, page: Int, callback: LoadArtistAlbumsCallback) {

        val url = URL_HOME + "/artist/album-" + artist.artistId + "?page=$page"
        OkHttpUtil.getForXiami(context, url, object : BaseResponseCallback() {
            override fun onStart() {
                callback.onStart()
            }

            override fun onHtmlResponse(html: String) {
                super.onHtmlResponse(html)
                try {
                    val albums = parseArtistAlbums(html)
                    callback.onSuccess(albums)
                } catch (e: NullPointerException) {
                    callback.onFailure("没有更多专辑了")
                }
            }

            override fun onFailure(msg: String) {
                callback.onFailure(msg)
            }

        })
    }

    private fun parseArtistAlbums(html: String): List<Album> {
        val albums = ArrayList<Album>()
        val document = Jsoup.parse(html)
        val albumsElement = document.getElementById("artist_albums").getElementsByClass("albumThread_list").first()
                .select("li")
        for (albumElement in albumsElement) {
            val album = Album()
            album.supplier = MusicProvider.XIAMI
            val id = albumElement.select("div").first().attr("id")
            album.id = id.substring(id.indexOf("_") + 1).toLong()
            album.miniCoverUrl = albumElement.getElementsByClass("cover").first()
                    .select("img").attr("src")
            album.coverUrl = album.miniCoverUrl.substring(0, album.miniCoverUrl.indexOf("@"))
            val detail = albumElement.getElementsByClass("detail").first()
            val title = detail.getElementsByClass("name").first()
                    .select("a").first().attr("title")
            album.title = title
            val publishDate = detail.getElementsByClass("company").first()
                    .select("a").last().text()
            album.publishDateStr = publishDate
            albums.add(album)
        }

        return albums
    }

    override fun loadCollectByCategory(category: String, page: Int, callback: LoadCollectByCategoryCallback) {
        val url = if (category == CATEGORY_HOT_COLLECT) {
            "http://www.xiami.com/collect/recommend/page/$page" //热门
        } else {
            "http://www.xiami.com/search/collect/page/$page?key=${URLEncoder.encode(category, "utf-8")}"
        }
        OkHttpUtil.getForXiami(context, url, object : BaseResponseCallback() {
            override fun onStart() {
                callback.onStart()
            }

            override fun onHtmlResponse(html: String) {
                super.onHtmlResponse(html)
                try {
                    val collects = parseCollectsFromHTML(html)
                    callback.onSuccess(collects)
                } catch (e: NullPointerException) {
                    callback.onFailure("没有更多歌单了")
                }
            }

            override fun onFailure(msg: String) {
                super.onFailure(msg)
                callback.onFailure(msg)
            }

        })

    }

    private fun parseCollectsFromHTML(html: String): List<Collect> {
        val document = Jsoup.parse(html)
        val collects = ArrayList<Collect>()
        val page = document.getElementById("page")
        val list = page.getElementsByClass("block_items clearfix")
        for (i in 0 until list.size) {
            val element = list[i]
            val a = element.select("a").first()
            val title = a.attr("title")
            val ref = a.attr("href")
            val id = parseIdFromHref(ref)
            val coverUrl = a.select("img").first().attr("src")
            val collect = Collect()
            collect.collectId = id.toLong()
            collect.title = title
            collect.coverUrl = coverUrl.substring(0, coverUrl.length - 11)
            collect.source = MusicProvider.XIAMI
            collects.add(collect)
        }
        return collects
    }

    private fun parseIdFromHref(ref: String): Int {
        val idStr = ref.substring(ref.lastIndexOf('/') + 1)
        return Integer.valueOf(idStr)
    }

    override fun loadRankingList(callback: LoadRankingCallback) {
        GetRankListRequest()
                .setDataCallback(object : BaseRequest.BaseDataCallback<RankList>() {
                    override fun onSuccess(data: RankList) {
                        callback.onSuccess(data)
                    }

                    override fun onFailure(code: String?, msg: String?) {
                        callback.onFailure(msg ?: "")
                    }

                    override fun beforeRequest() {
                        callback.onStart()
                    }

                })
                .send()
    }

    fun maskUrl(url: String): String {
        return if (url.startsWith("//")) {
            "https:$url"
        } else {
            url
        }
    }
}
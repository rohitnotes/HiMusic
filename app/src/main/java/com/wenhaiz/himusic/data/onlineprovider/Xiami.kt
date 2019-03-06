package com.wenhaiz.himusic.data.onlineprovider

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import android.text.TextUtils
import com.wenhaiz.himusic.R
import com.wenhaiz.himusic.data.*
import com.wenhaiz.himusic.data.bean.Album
import com.wenhaiz.himusic.data.bean.Artist
import com.wenhaiz.himusic.data.bean.Banner
import com.wenhaiz.himusic.data.bean.BannerType
import com.wenhaiz.himusic.data.bean.Collect
import com.wenhaiz.himusic.data.bean.Song
import com.wenhaiz.himusic.data.bean.SongDetail
import com.wenhaiz.himusic.http.data.CollectDetail
import com.wenhaiz.himusic.http.data.RecommendListNewAlbumInfo
import com.wenhaiz.himusic.http.data.RecommendListRecommendInfo
import com.wenhaiz.himusic.http.request.BaseRequest
import com.wenhaiz.himusic.http.request.GetCollectDetailRequest
import com.wenhaiz.himusic.http.request.GetRecommendAlbumRequest
import com.wenhaiz.himusic.http.request.GetRecommendCollectRequest
import com.wenhaiz.himusic.http.request.GetSongDetailRequest
import com.wenhaiz.himusic.module.ranking.RankingContract
import com.wenhaiz.himusic.utils.BaseResponseCallback
import com.wenhaiz.himusic.utils.OkHttpUtil
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.io.IOException
import java.net.URLDecoder
import java.net.URLEncoder

/**
 * 虾米音乐
 * Created by Wenhai on 2017/8/4.
 */
class Xiami(val context: Context) : MusicSource {
    companion object {
        @JvmStatic
        val TAG = "Xiami"
        val BASE_URL = "http://api.xiami.com/web?v=2.0&app_key=1&"
        val SUFFIX_COLLECT_DETAIL = "&callback=jsonp122&r=collect/detail"
        val SUFFIX_ALBUM_DETAIL = "&page=1&limit=20&callback=jsonp217&r=album/detail"

        val PREFIX_SEARCH_SONG = "http://api.xiami.com/web?v=2.0&app_key=1&key="
        val SUFFIX_SEARCH_SONG = "&page=1&limit=50&callback=jsonp154&r=search/songs"
        val PREFIX_SEARCH_RECOMMEND = "http://www.xiami.com/ajax/search-index?key="

        val INFIX_SEARCH_RECOMMEND = "&_="//后面加时间


        //get hidden listen url when "listen file" is null
        val PREFIX_SONG_DETAIL = "/song/playlist/id/"
        val SUFFIX_SONG_DETAIL = "/object_name/default/object_id/0/cat/json"

        //singer type:0-全部 1-华语 2-欧美 3-日本 4-韩国
        //http://www.xiami.com/artist/index/c/2/type/1
        // c 1-本周流行 2-热门艺人
//        http://www.xiami.com/artist/index/c/2/type/1/class/0/page/1
        val URL_PREFIX_LOAD_ARTISTS = "/artist/index/c/2/type/"
        val URL_INFIX_LOAD_ARTISTS = "/class/0/page/"
        val URL_HOME = "http://www.xiami.com"
        val CATEGORY_HOT_COLLECT = "热门歌单"
        //        val URL_SEARCH_ARTIST = "/search/artist?key=%E7%94%B0"
        val URL_HOT_COLLECT = "/collect/recommend/page/"
        //type:all-全部 huayu-华语 oumei-欧美 ri-日本 han-韩国
        val URL_NEW_ALBUM = "/music/newalbum/type/all/page/"

        //虾米音乐榜
        val URL_RANKING_DATA_MUSIC = "/chart/data?c=103&type=0&page=1&limit=100&_="
        //虾米原创榜
        val URL_RANKING_DATA_ORIGINAL = "/chart/data?c=104&type=0&page=1&limit=100&_="
        //虾米新歌榜
        val URL_RANKING_DATA_NEW = "/chart/data?c=102&type=0&page=1&limit=100&_="
        //billboard
        val URL_RANKING_BILLBOARD = "/chart/data?c=204&type=0&page=1&limit=100&_="
        //uk
        val URL_RANKING_UK = "/chart/data?c=203&type=0&page=1&limit=100&_="
        //oricon
        val URL_RANKING_ORICON = "/chart/data?c=205&type=0&page=1&limit=100&_="

        val RANKING_MUSIC = "虾米音乐榜"
        val RANKING_ORIGIN = "虾米原创榜"
        val RANKING_NEW = "虾米新歌榜"

        /*
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

    override fun loadCollectDetail(id: Long, callback: LoadCollectDetailCallback) {
        GetCollectDetailRequest(id).setDataCallback(object : BaseRequest.BaseDataCallback<CollectDetail>() {
            override fun onSuccess(data: CollectDetail) {
                data.detail?.source = MusicProvider.XIAMI
                callback.onSuccess(data)
            }

            override fun onFailure(code: String?, msg: String?) {
                callback.onFailure(msg ?: "")
            }

            override fun beforeRequest() {
                callback.onStart()
            }

        }).send()
    }

    private fun parseSongsFromJson(songs: JSONArray?): ArrayList<Song> {
        val songCount = songs!!.length()
        val songList = ArrayList<Song>(songCount)
        for (i in 0 until songCount) {
            val song = Song()
            val jsonSong: JSONObject = songs.get(i) as JSONObject
            song.songId = jsonSong.getLong("song_id")
            song.name = jsonSong.getString("song_name")
            song.albumId = jsonSong.getLong("album_id")
            song.albumName = jsonSong.getString("album_name")
            song.artistId = jsonSong.getLong("artist_id")
            try {
                song.artistName = jsonSong.getString("artist_name")
            } catch (e: JSONException) {
                song.artistName = jsonSong.getString("singers")
            }
            // multi artist
            if (song.artistName.contains(";")) {
                val artists = song.artistName.split(";")
                val artistBuilder = StringBuilder()
                for (artist in artists) {
                    artistBuilder.append(artist)
                    artistBuilder.append("/")
                }
                song.artistName = artistBuilder.substring(0, artistBuilder.length - 1)
            }

            song.listenFileUrl = ""
            song.supplier = MusicProvider.XIAMI
            songList.add(song)
        }
        return songList
    }

    override fun loadSongDetail(song: Song, callback: LoadSongDetailCallback) {
        GetSongDetailRequest(song.songId)
                .setDataCallback(object : BaseRequest.BaseDataCallback<SongDetail>() {
                    override fun onSuccess(data: SongDetail) {
                        song.addDetailInfo(data.data)
                        song.supplier = MusicProvider.XIAMI
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


    override fun loadAlbumDetail(id: Long, callback: LoadAlbumDetailCallback) {
        val url = BASE_URL + "id=$id" + SUFFIX_ALBUM_DETAIL
        OkHttpUtil.getForXiami(context, url, object : BaseResponseCallback() {
            override fun onStart() {
                callback.onStart()
            }

            override fun onJsonObjectResponse(jsonObject: JSONObject) {
                super.onJsonObjectResponse(jsonObject)
                val album = Album()
                album.supplier = MusicProvider.XIAMI
                album.id = jsonObject.getLong("album_id")
                album.artist = jsonObject.getString("artist_name")
                album.artistId = jsonObject.getLong("artist_id")
                album.title = jsonObject.getString("album_name")
                album.songNumber = jsonObject.getInt("song_count")
                album.publishDate = jsonObject.getLong("gmt_publish")
                album.coverUrl = jsonObject.getString("album_logo")
                album.miniCoverUrl = album.coverUrl + "@1e_1c_100Q_100w_100h"
                album.songs = parseSongsFromJson(jsonObject.getJSONArray("songs"))
                callback.onSuccess(album)
            }

            override fun onFailure(msg: String) {
                callback.onFailure(msg)
            }

        })
    }

    override fun searchByKeyword(keyword: String, callback: LoadSearchResultCallback) {
        val encodedKeyword = URLEncoder.encode(keyword, "utf-8")
        val url = PREFIX_SEARCH_SONG + encodedKeyword + SUFFIX_SEARCH_SONG
        OkHttpUtil.getForXiami(context, url, object : BaseResponseCallback() {
            override fun onStart() {
                callback.onStart()
            }

            override fun onJsonObjectResponse(jsonObject: JSONObject) {
                super.onJsonObjectResponse(jsonObject)
                val songs: ArrayList<Song>? = parseSongsFromJson(jsonObject.getJSONArray("songs"))
                if (songs == null || songs.size == 0) {
                    callback.onFailure("搜索失败")
                } else {
                    callback.onSuccess(songs)
                }
            }

            override fun onFailure(msg: String) {
                callback.onFailure(msg)
            }

        })
    }

    override fun loadSearchRecommend(keyword: String, callback: LoadSearchRecommendCallback) {
        val currentTime = System.currentTimeMillis()
        val url = PREFIX_SEARCH_RECOMMEND + URLEncoder.encode(keyword, "utf-8") + INFIX_SEARCH_RECOMMEND + currentTime
        OkHttpUtil.getForXiami(context, url, object : BaseResponseCallback() {

            override fun onStart() {
                callback.onStart()
            }

            override fun onHtmlResponse(html: String) {
                val keywordList = parseRecommendKeywords(html)
                callback.onSuccess(keywordList)
            }

            override fun onFailure(msg: String) {
                super.onFailure(msg)
                callback.onFailure(msg)
            }

        })

    }

    private fun parseRecommendKeywords(string: String): List<String> {
        val keywordList = ArrayList<String>()
        val document = Jsoup.parse(string)
        val result = document.getElementsByClass("result")
        result.map { it.select("a").first().attr("title") }
                .filterNotTo(keywordList) { TextUtils.isEmpty(it) }
        return keywordList
    }

    override fun loadArtists(region: ArtistRegion, page: Int, callback: LoadArtistsCallback) {
        val type = when (region) {
            ArtistRegion.ALL -> {
                0
            }
            ArtistRegion.CN -> {
                1
            }
            ArtistRegion.EA -> {
                2
            }
            ArtistRegion.JP -> {
                3
            }
            ArtistRegion.KO -> {
                4
            }
        }
        val url = URL_HOME + URL_PREFIX_LOAD_ARTISTS + "$type" + URL_INFIX_LOAD_ARTISTS + page
        OkHttpUtil.getForXiami(context, url, object : BaseResponseCallback() {
            override fun onStart() {
                callback.onStart()
            }

            override fun onFailure(msg: String) {
                super.onFailure(msg)
                callback.onFailure(msg)
            }

            override fun onHtmlResponse(html: String) {
                super.onHtmlResponse(html)
                try {
                    val artists: ArrayList<Artist> = parseArtistList(html)
                    callback.onSuccess(artists)
                } catch (e: NullPointerException) {
                    callback.onFailure("没有更多艺人了")
                }
            }

        })

    }

    private fun parseArtistList(html: String): ArrayList<Artist> {
        val result = ArrayList<Artist>()
        val document = Jsoup.parse(html)
        val artists = document.getElementById("artists")
        val artistElements = artists.getElementsByClass("artist")
        for (artistElement in artistElements) {
            val artist = Artist()
            val img = artistElement.getElementsByClass("image").first()
            artist.name = artistElement.getElementsByClass("info").first()
                    .select("a").first().attr("title")
            artist.miniImgUrl = img.select("img").first()
                    .attr("src")
            val homePageSuffix = artistElement.getElementsByClass("image").first()
                    .select("a").first()
                    .attr("href")
            val artistId = homePageSuffix.substring(homePageSuffix.lastIndexOf("/") + 1)
            artist.artistId = artistId
            result.add(artist)
        }
        return result

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
            song.artistName = artist.name
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
        return Integer.valueOf(idStr)!!
    }

    override fun loadOfficialRanking(provider: MusicProvider, callback: LoadRankingCallback) {
        callback.onStart()
        val collects = ArrayList<Collect>()
        LoadRankingListTask(collects, RANKING_MUSIC, callback)
                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, URL_HOME + URL_RANKING_DATA_MUSIC + System.currentTimeMillis())
        LoadRankingListTask(collects, RANKING_ORIGIN, callback)
                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, URL_HOME + URL_RANKING_DATA_ORIGINAL + System.currentTimeMillis())
        LoadRankingListTask(collects, RANKING_NEW, callback)
                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, URL_HOME + URL_RANKING_DATA_NEW + System.currentTimeMillis())
    }

    override fun loadGlobalRanking(ranking: RankingContract.GlobalRanking, callback: LoadSingleRankingCallback) {
        val collect = Collect()
        collect.source = MusicProvider.XIAMI
        val suffix = when (ranking) {
            RankingContract.GlobalRanking.BILLBOARD -> {
                collect.title = "Billboard周榜"
                collect.desc = "美国公告牌每周最热100首单曲，每周五更新"
                collect.coverDrawable = R.drawable.ranking_billboard
                URL_RANKING_BILLBOARD
            }

            RankingContract.GlobalRanking.UK
            -> {
                collect.coverDrawable = R.drawable.ranking_uk
                collect.title = "英国UK榜"
                collect.desc = "英国官方每周最热单曲排行榜，每周一更新"
                URL_RANKING_UK
            }
            RankingContract.GlobalRanking.ORICON
            -> {
                collect.coverDrawable = R.drawable.ranking_oricon
                collect.title = "日本 Oricon 周榜"
                collect.desc = "日本公信榜上周销量前20位单曲，每周三更新"
                URL_RANKING_ORICON
            }
        }
        val url = URL_HOME + suffix + System.currentTimeMillis()
        OkHttpUtil.getForXiami(context, url, object : BaseResponseCallback() {
            override fun onStart() {
                callback.onStart()
            }

            override fun onHtmlResponse(html: String) {
                val songList = parseRankingSongs(Jsoup.parse(html))
                collect.songs.addAll(songList)
                callback.onSuccess(collect)
            }

            override fun onFailure(msg: String) {
                callback.onFailure(msg)
            }
        })

    }

    fun maskUrl(url: String): String {
        return if (url.startsWith("//")) {
            "https:$url"
        } else {
            url
        }
    }

    private fun parseRankingSongs(document: Document): List<Song> {
        val songElements = document.getElementsByClass("song")
        val moreElements = document.getElementsByClass("more")

        val songs = ArrayList<Song>()
        for (i in 0 until songElements.size) {
            val element = songElements[i]
            val song = Song()
            song.supplier = MusicProvider.XIAMI
            song.miniAlbumCoverUrl = element.getElementsByClass("image").first()
                    .select("img").first().attr("src")
            song.albumName = ""
            song.albumCoverUrl = song.miniAlbumCoverUrl.substring(0, song.miniAlbumCoverUrl.indexOf("@"))
            val info = element.getElementsByClass("info").first()
            song.name = info.select("p").first().select("a").first().text()
            song.artistName = info.select("p").last().select("a").first().text()
            val songId = moreElements[i].select("li").first().attr("onclick")
            song.songId = songId.substring(songId.indexOf("(") + 1, songId.indexOf(",")).toLong()
            songs.add(song)
        }
        return songs
    }

    @SuppressLint("StaticFieldLeak")
    inner class LoadRankingListTask(val collects: ArrayList<Collect>, private val rankingTitle: String, private val callback: LoadRankingCallback)
        : AsyncTask<String, Void, Collect>() {

        override fun doInBackground(vararg urls: String?): Collect? {
            val url = urls[0]

            try {
                val document = Jsoup.connect(url).get()
                val collect = Collect()
                collect.title = rankingTitle
                when (rankingTitle) {
                    RANKING_MUSIC -> {
                        collect.coverDrawable = R.mipmap.xiami_music
                        collect.desc = "虾米音乐全曲库歌曲试听量排名"
                    }
                    RANKING_NEW -> {
                        collect.coverDrawable = R.mipmap.xiami_new
                        collect.desc = "虾米音乐30天内新歌试听量排名"
                    }
                    RANKING_ORIGIN -> {
                        collect.coverDrawable = R.mipmap.xiami_original
                        collect.desc = "虾米音乐人最新作品试听量排名"
                    }
                }
                collect.source = MusicProvider.XIAMI
                collect.songs.addAll(parseRankingSongs(document))
                return collect
            } catch (e: IOException) {
                callback.onFailure("获取排行榜信息失败")
            }
            return null
        }

        override fun onPostExecute(result: Collect?) {
            super.onPostExecute(result)
            if (result != null) {
                collects.add(result)
                if (collects.size == 3) {
                    callback.onSuccess(collects)
                }
            }
        }
    }
}
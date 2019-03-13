package com.wenhaiz.himusic.http;

public class API {

    public static class XiaMi{
        static final String BASE = "https://www.xiami.com";

        public static final String GET_COLLECT_LIST = "/api/list/collect";

        public static final String GET_COLLECT_DETAIL = "/api/collect/initialize";

        public static final String GET_ALBUM_LIST = "/api/list/album";

        public static final String GET_ALBUM_DETAIL = "/api/album/initialize";

        public static final String GET_SONG_DETAIL = "/api/song/getSongDetails";
        public static final String GET_SONG_PLAY_INFO = "/api/song/getPlayInfo";

        public static final String GET_RANK_LIST = "/api/billboard/getBillboards";
        public static final String GET_RANK_DETAIL = "/api/billboard/getBillboardDetail";

        public static final String SEARCH_SONGS = "/api/search/searchSongs";
        public static final String GET_SEARCH_TIPS = "/api/search/searchTips";




        public static String getSongDetailUrl(Long songId) {
            return BASE + "/song/playlist/id/" + songId + "/object_name/default/object_id/0/cat/json";
        }
    }

}

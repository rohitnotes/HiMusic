package com.wenhaiz.himusic.http.data;

import com.google.gson.annotations.SerializedName;
import com.wenhaiz.himusic.data.bean.Song;

import java.util.List;

public class SearchSongResult {
    @SerializedName("songs")
    private List<Song> songs;
    @SerializedName("pagingVO")
    private QueryPage paging;

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    public QueryPage getPaging() {
        return paging;
    }

    public void setPaging(QueryPage paging) {
        this.paging = paging;
    }
}

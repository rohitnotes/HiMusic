package com.wenhaiz.himusic.http.data;

import com.google.gson.annotations.SerializedName;
import com.wenhaiz.himusic.data.bean.Album;

import java.util.ArrayList;

/**
 * Created by wm on 2016/7/28.
 */
public class RecommendListNewAlbumInfo {

    @SerializedName("albums")
    private ArrayList<Album> albums;

    public ArrayList<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(ArrayList<Album> albums) {
        this.albums = albums;
    }

}

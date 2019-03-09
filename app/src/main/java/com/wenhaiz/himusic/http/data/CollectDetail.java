package com.wenhaiz.himusic.http.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;
import com.wenhaiz.himusic.data.MusicProvider;
import com.wenhaiz.himusic.data.bean.Song;

import java.util.ArrayList;

public class CollectDetail {

    @SerializedName("collectDetail")
    private Detail detail;

    @SerializedName("collectSongs")
    private ArrayList<Song> songs;

    public @Nullable Detail getDetail() {
        return detail;
    }

    public void setDetail(Detail detail) {
        this.detail = detail;
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }

    public void setSongs(ArrayList<Song> songs) {
        this.songs = songs;
    }

    public class Detail{
        @SerializedName("listId")
        private long listId;
        @SerializedName("collectName")
        private String collectName;
        @SerializedName("description")
        private String description;
        @SerializedName("songCount")
        private int songCount;
        @SerializedName("userName")
        private String userName;
        @SerializedName("authorAvatar")
        private String authorAvatar;
        @SerializedName("playCount")
        private int  playCount;
        @SerializedName("collectLogo")
        private String collectLogo;
        @SerializedName("collectLogoSmall")
        private String collectLogoSmall;
        @SerializedName("collectLogoMiddle")
        private String collectLogoMiddle;
        @SerializedName("cleanDesc")
        private String cleanDesc;
        @SerializedName("gmtCreate")
        private long gmtCreate;
        @SerializedName("gmtModify")
        private long gmtModify;
        @SerializedName("h5Url")
        private String h5Url;


        private MusicProvider source;

        public MusicProvider getSource() {
            return source;
        }

        public long getGmtModify() {
            return gmtModify;
        }

        public void setGmtModify(long gmtModify) {
            this.gmtModify = gmtModify;
        }

        public void setSource(MusicProvider source) {
            this.source = source;
        }

        public String getCollectLogoSmall() {
            return collectLogoSmall;
        }

        public void setCollectLogoSmall(String collectLogoSmall) {
            this.collectLogoSmall = collectLogoSmall;
        }

        public String getCollectLogoMiddle() {
            return collectLogoMiddle;
        }

        public void setCollectLogoMiddle(String collectLogoMiddle) {
            this.collectLogoMiddle = collectLogoMiddle;
        }

        public String getH5Url() {
            return h5Url;
        }

        public void setH5Url(String h5Url) {
            this.h5Url = h5Url;
        }

        public long getListId() {
            return listId;
        }

        public void setListId(long listId) {
            this.listId = listId;
        }

        public String getCollectName() {
            return collectName;
        }

        public void setCollectName(String collectName) {
            this.collectName = collectName;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getSongCount() {
            return songCount;
        }

        public void setSongCount(int songCount) {
            this.songCount = songCount;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getAuthorAvatar() {
            return authorAvatar;
        }

        public void setAuthorAvatar(String authorAvatar) {
            this.authorAvatar = authorAvatar;
        }

        public int getPlayCount() {
            return playCount;
        }

        public void setPlayCount(int playCount) {
            this.playCount = playCount;
        }

        public String getCollectLogo() {
            return collectLogo;
        }

        public void setCollectLogo(String collectLogo) {
            this.collectLogo = collectLogo;
        }

        public String getCleanDesc() {
            return cleanDesc;
        }

        public void setCleanDesc(String cleanDesc) {
            this.cleanDesc = cleanDesc;
        }

        public long getGmtCreate() {
            return gmtCreate;
        }

        public void setGmtCreate(long gmtCreate) {
            this.gmtCreate = gmtCreate;
        }
    }
}

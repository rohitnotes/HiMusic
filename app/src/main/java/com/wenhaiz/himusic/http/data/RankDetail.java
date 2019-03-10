package com.wenhaiz.himusic.http.data;

import com.google.gson.annotations.SerializedName;
import com.wenhaiz.himusic.data.bean.Song;

import java.util.List;

public class RankDetail {
    @SerializedName("billboard")
    private Detail detail;

    public Detail getDetail() {
        return detail;
    }

    public void setDetail(Detail detail) {
        this.detail = detail;
    }

    public class Detail{
        @SerializedName("billboardId")
        private long billboardId;
        @SerializedName("code")
        private String code;
        @SerializedName("name")
        private String name;
        @SerializedName("songs")
        private List<Song> songs;

        public long getBillboardId() {
            return billboardId;
        }

        public void setBillboardId(long billboardId) {
            this.billboardId = billboardId;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Song> getSongs() {
            return songs;
        }

        public void setSongs(List<Song> songs) {
            this.songs = songs;
        }
    }
}

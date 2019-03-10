package com.wenhaiz.himusic.http.data;

import com.google.gson.annotations.SerializedName;
import com.wenhaiz.himusic.data.MusicProvider;
import com.wenhaiz.himusic.data.bean.Song;

import java.util.List;

public class AlbumDetail {

    @SerializedName("albumDetail")
    private DetailData albumDetail;

    public DetailData getAlbumDetail() {
        return albumDetail;
    }

    public void setAlbumDetail(DetailData albumDetail) {
        this.albumDetail = albumDetail;
    }

    public class DetailData {
        @SerializedName("h5Url")
        private String h5Url;
        @SerializedName("albumLogo")
        private String albumLogo;
        @SerializedName("artistId")
        private long artistId;
        @SerializedName("artistStringId")
        private String artistStringId;
        @SerializedName("gmtPublish")
        private long gmtPublish;
        @SerializedName("albumLogoS")
        private String albumLogoS;
        @SerializedName("albumLogoM")
        private String albumLogoM;
        @SerializedName("description")
        private String htmlDescription;
        @SerializedName("songs")
        private List<Song> songs;
        @SerializedName("songCount")
        private int songCount;
        private MusicProvider source;

        public MusicProvider getSource() {
            return source;
        }

        public void setSource(MusicProvider source) {
            this.source = source;
        }

        public int getSongCount() {
            return songCount;
        }

        public void setSongCount(int songCount) {
            this.songCount = songCount;
        }

        public String getH5Url() {
            return h5Url;
        }

        public void setH5Url(String h5Url) {
            this.h5Url = h5Url;
        }

        public String getAlbumLogo() {
            return albumLogo;
        }

        public void setAlbumLogo(String albumLogo) {
            this.albumLogo = albumLogo;
        }

        public long getArtistId() {
            return artistId;
        }

        public void setArtistId(long artistId) {
            this.artistId = artistId;
        }

        public String getArtistStringId() {
            return artistStringId;
        }

        public void setArtistStringId(String artistStringId) {
            this.artistStringId = artistStringId;
        }

        public long getGmtPublish() {
            return gmtPublish;
        }

        public void setGmtPublish(long gmtPublish) {
            this.gmtPublish = gmtPublish;
        }

        public String getAlbumLogoS() {
            return albumLogoS;
        }

        public void setAlbumLogoS(String albumLogoS) {
            this.albumLogoS = albumLogoS;
        }

        public String getAlbumLogoM() {
            return albumLogoM;
        }

        public void setAlbumLogoM(String albumLogoM) {
            this.albumLogoM = albumLogoM;
        }

        public String getHtmlDescription() {
            return htmlDescription;
        }

        public void setHtmlDescription(String htmlDescription) {
            this.htmlDescription = htmlDescription;
        }

        public List<Song> getSongs() {
            return songs;
        }

        public void setSongs(List<Song> songs) {
            this.songs = songs;
        }
    }

}

package com.wenhaiz.himusic.data.bean;

import com.google.gson.annotations.SerializedName;
import com.wenhaiz.himusic.http.data.BaseData;

import java.util.List;

public class SongDetail extends BaseData {
    @SerializedName("status")
    private boolean status;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private DetailData data;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DetailData getData() {
        return data;
    }

    public void setData(DetailData data) {
        this.data = data;
    }

    public class DetailData {
        @SerializedName("trackList")
        private List<TrackInfo> trackList;

        public List<TrackInfo> getTrackList() {
            return trackList;
        }

        public void setTrackList(List<TrackInfo> trackList) {
            this.trackList = trackList;
        }
    }

    public class TrackInfo {
        @SerializedName("songId")
        private long songId;
        @SerializedName("songStringId")
        private String songStringId;
        @SerializedName("songName")
        private String songName;
        @SerializedName("albumId")
        private long albumId;
        @SerializedName("albumStringId")
        private String albumStringId;
        @SerializedName("artistId")
        private long artistId;
        @SerializedName("singers")
        private String singers;
        @SerializedName("lyricInfo")
        private LyricInfo lyricInfo;
        @SerializedName("location")
        private String location;
        @SerializedName("album_pic")
        private String albumPic;
        @SerializedName("pic")
        private String pic;
        @SerializedName("length")
        private int length;

        public int getLength() {
            return length;
        }

        public void setLength(int length) {
            this.length = length;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public long getSongId() {
            return songId;
        }

        public void setSongId(long songId) {
            this.songId = songId;
        }

        public String getSongStringId() {
            return songStringId;
        }

        public void setSongStringId(String songStringId) {
            this.songStringId = songStringId;
        }

        public String getSongName() {
            return songName;
        }

        public void setSongName(String songName) {
            this.songName = songName;
        }

        public long getAlbumId() {
            return albumId;
        }

        public void setAlbumId(long albumId) {
            this.albumId = albumId;
        }

        public String getAlbumStringId() {
            return albumStringId;
        }

        public void setAlbumStringId(String albumStringId) {
            this.albumStringId = albumStringId;
        }

        public long getArtistId() {
            return artistId;
        }

        public void setArtistId(long artistId) {
            this.artistId = artistId;
        }

        public String getSingers() {
            return singers;
        }

        public void setSingers(String singers) {
            this.singers = singers;
        }

        public LyricInfo getLyricInfo() {
            return lyricInfo;
        }

        public void setLyricInfo(LyricInfo lyricInfo) {
            this.lyricInfo = lyricInfo;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getAlbumPic() {
            return albumPic;
        }

        public void setAlbumPic(String albumPic) {
            this.albumPic = albumPic;
        }

        public class LyricInfo {
            @SerializedName("lyricId")
            private long lyricId;
            @SerializedName("lyricType")
            private int lyricType;
            @SerializedName("lyricFile")
            private String lyricFile;

            public long getLyricId() {
                return lyricId;
            }

            public void setLyricId(long lyricId) {
                this.lyricId = lyricId;
            }

            public int getLyricType() {
                return lyricType;
            }

            public void setLyricType(int lyricType) {
                this.lyricType = lyricType;
            }

            public String getLyricFile() {
                return lyricFile;
            }

            public void setLyricFile(String lyricFile) {
                this.lyricFile = lyricFile;
            }
        }
    }

}

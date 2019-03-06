package com.wenhaiz.himusic.http.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Song implements Serializable {

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
    @SerializedName("albumLogo")
    private String albumLogo;
    @SerializedName("albumName")
    private String albumName;
    @SerializedName("artistId")
    private long artistId;
    @SerializedName("artistName")
    private String artistName;
    @SerializedName("artistLogo")
    private String artistLogo;

    @SerializedName("lrcFile")
    private String lrcFile;

    public String getLrcFile() {
        return lrcFile;
    }

    public void setLrcFile(String lrcFile) {
        this.lrcFile = lrcFile;
    }

    private boolean isLocal = false;

    public boolean isLocal() {
        return isLocal;
    }

    public void setLocal(boolean local) {
        isLocal = local;
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

    public String getAlbumLogo() {
        return albumLogo;
    }

    public void setAlbumLogo(String albumLogo) {
        this.albumLogo = albumLogo;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public long getArtistId() {
        return artistId;
    }

    public void setArtistId(long artistId) {
        this.artistId = artistId;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getArtistLogo() {
        return artistLogo;
    }

    public void setArtistLogo(String artistLogo) {
        this.artistLogo = artistLogo;
    }
}

package com.wenhaiz.himusic.data.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.wenhaiz.himusic.data.MusicProvider;
import com.wenhaiz.himusic.data.onlineprovider.Xiami;

import java.io.Serializable;
import java.util.List;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Transient;

@Entity
public final class Song implements Parcelable, Serializable {
    public static final Parcelable.Creator<Song> CREATOR = new Parcelable.Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };
    private static final long serialVersionUID = 10001;
    @Id
    private Long id;//本地数据库 id
    @SerializedName("songId")
    private long songId;
    @SerializedName("songStringId")
    private String songStringId;
    @SerializedName("songName")
    private String name;
    private int length;//second
    private String listenFileUrl;//xiami
    @SerializedName("lrcFile")
    private String lyricUrl;//lyric
    @SerializedName("artistName")
    private String artistName;
    @SerializedName("albumName")
    private String albumName;
    private String displayArtistName;
    @SerializedName("artistLogo")
    private String artistLogo;
    @SerializedName("artistId")
    private long artistId;
    @SerializedName("albumId")
    private long albumId;
    @SerializedName("albumStringId")
    private String albumStringId;
    @SerializedName("albumLogo")
    private String albumCoverUrl;
    private String miniAlbumCoverUrl;
    @Transient
    private MusicProvider supplier;
    private String providerName;
    public boolean isPlaying = false;
    @SerializedName("singers")
    private String singers;

    public boolean canListen = true;



    public Song() {

    }

    public void addDetailInfo(SongDetail.DetailData detail) {
        List<SongDetail.TrackInfo> trackList = detail.getTrackList();
        if (trackList == null || trackList.isEmpty()) {
            //不能播放
            canListen = false;
            return;
        }
        lyricUrl = Xiami.Companion.maskUrl(trackList.get(0).getLyricInfo().getLyricFile());
        miniAlbumCoverUrl = Xiami.Companion.maskUrl(trackList.get(0).getPic());
        listenFileUrl = Xiami.Companion.maskUrl(Xiami.Companion.getListenUrlFromLocation(trackList.get(0).getLocation()));
        if (TextUtils.isEmpty(albumCoverUrl)) {
            albumCoverUrl = Xiami.Companion.maskUrl(trackList.get(0).getAlbumPic());
        }
        if (length <= 0) {
            length = trackList.get(0).getLength() * 1000;
        }
    }

    protected Song(Parcel in) {
        songId = in.readLong();
        name = in.readString();
        length = in.readInt();
        listenFileUrl = in.readString();
        lyricUrl = in.readString();
        artistName = in.readString();
        displayArtistName = in.readString();
        artistLogo = in.readString();
        artistId = in.readLong();
        albumId = in.readLong();
        albumName = in.readString();
        albumCoverUrl = in.readString();
        miniAlbumCoverUrl = in.readString();
        switch (in.readInt()) {
            case 0:
                supplier = MusicProvider.XIAMI;
                break;
            case 1:
                supplier = MusicProvider.QQMUSIC;
                break;
            case 2:
                supplier = MusicProvider.NETEASE;
                break;
        }
        isPlaying = in.readInt() == 1;
        singers = in.readString();

    }

    public Song(Long id, long songId, String name, int length, String listenFileUrl,
                String lyricUrl, String artistName, String displayArtistName, String artistLogo,
                long artistId, long albumId, String albumName, String albumCoverUrl,
                String miniAlbumCoverUrl, String providerName, boolean isPlaying) {
        this.id = id;
        this.songId = songId;
        this.name = name;
        this.length = length;
        this.listenFileUrl = listenFileUrl;
        this.lyricUrl = lyricUrl;
        this.artistName = artistName;
        this.displayArtistName = displayArtistName;
        this.artistLogo = artistLogo;
        this.artistId = artistId;
        this.albumId = albumId;
        this.albumName = albumName;
        this.albumCoverUrl = albumCoverUrl;
        this.miniAlbumCoverUrl = miniAlbumCoverUrl;
        this.providerName = providerName;
        this.isPlaying = isPlaying;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(songId);
        dest.writeString(name);
        dest.writeInt(length);
        dest.writeString(listenFileUrl);
        dest.writeString(lyricUrl);
        dest.writeString(artistName);
        dest.writeString(displayArtistName);
        dest.writeString(artistLogo);
        dest.writeLong(artistId);
        dest.writeLong(albumId);
        dest.writeString(albumName);
        dest.writeString(albumCoverUrl);
        dest.writeString(miniAlbumCoverUrl);
        switch (supplier) {
            case XIAMI:
                dest.writeInt(0);
                break;
            case QQMUSIC:
                dest.writeInt(1);
                break;
            case NETEASE:
                dest.writeInt(2);
                break;
        }
        dest.writeInt(isPlaying ? 1 : 0);
        dest.writeString(singers);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getArtistName() {
        if (TextUtils.isEmpty(artistName)) {
            return singers;
        }
        return artistName;
    }

    @Keep
    public void setArtistName(String artistName) {
        this.artistName = artistName;
        String[] split = artistName.split(";");
        StringBuilder sb = new StringBuilder();
        for (String s : split) {
            sb.append(s);
            sb.append("/");
        }
        setDisplayArtistName(sb.toString().substring(0, sb.length() - 1));
    }

    public String getSingers() {
        return singers;
    }

    public void setSingers(String singers) {
        this.singers = singers;
    }

    public boolean isCanListen() {
        return canListen;
    }

    public void setCanListen(boolean canListen) {
        this.canListen = canListen;
    }

    public String getDisplayArtistName() {
        return displayArtistName;
    }

    public void setDisplayArtistName(String displayArtistName) {
        this.displayArtistName = displayArtistName;
    }

    public String getArtistLogo() {
        return artistLogo;
    }

    public void setArtistLogo(String artistLogo) {
        this.artistLogo = artistLogo;
    }

    public long getSongId() {
        return songId;
    }

    public void setSongId(long songId) {
        this.songId = songId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getListenFileUrl() {
        return listenFileUrl;
    }

    public void setListenFileUrl(String listenFileUrl) {
        this.listenFileUrl = listenFileUrl;
    }

    public String getLyricUrl() {
        return lyricUrl;
    }

    public void setLyricUrl(String lyricUrl) {
        this.lyricUrl = lyricUrl;
    }

    public long getArtistId() {
        return artistId;
    }

    public void setArtistId(long artistId) {
        this.artistId = artistId;
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getAlbumCoverUrl() {
        return albumCoverUrl;
    }

    public void setAlbumCoverUrl(String albumCoverUrl) {
        this.albumCoverUrl = albumCoverUrl;
    }

    public String getMiniAlbumCoverUrl() {
        return miniAlbumCoverUrl;
    }

    public void setMiniAlbumCoverUrl(String miniAlbumCoverUrl) {
        this.miniAlbumCoverUrl = miniAlbumCoverUrl;
    }

    public String getSongStringId() {
        return songStringId;
    }

    public void setSongStringId(String songStringId) {
        this.songStringId = songStringId;
    }

    public String getAlbumStringId() {
        return albumStringId;
    }

    public void setAlbumStringId(String albumStringId) {
        this.albumStringId = albumStringId;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public Artist getArtist() {
        Artist artist = new Artist();
        artist.setArtistId(String.valueOf(artistId));
        artist.setArtistName(artistName);
        return artist;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Song) {
            Song song = (Song) obj;
            if (song.songId == this.songId && song.getSupplier() == this.supplier) {
                return true;
            }
        }
        return false;
    }

    public MusicProvider getSupplier() {
        return supplier;
    }

    public void setSupplier(MusicProvider supplier) {
        this.supplier = supplier;
        this.providerName = supplier.name();
    }

    @Override
    public String toString() {
        return "Song{" +
                "songId=" + songId +
                ", name='" + name + '\'' +
                ", length=" + length +
                ", listenFileUrl='" + listenFileUrl + '\'' +
                ", lyricUrl='" + lyricUrl + '\'' +
                ", artistName='" + artistName + '\'' +
                ", artistLogo='" + artistLogo + '\'' +
                ", artistId=" + artistId +
                ", albumId=" + albumId +
                ", albumName='" + albumName + '\'' +
                ", albumCoverUrl='" + albumCoverUrl + '\'' +
                ", miniAlbumCoverUrl='" + miniAlbumCoverUrl + '\'' +
                ", supplier=" + supplier +
                '}';
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProviderName() {
        return this.providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public boolean getIsPlaying() {
        return this.isPlaying;
    }

    public void setIsPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }
}

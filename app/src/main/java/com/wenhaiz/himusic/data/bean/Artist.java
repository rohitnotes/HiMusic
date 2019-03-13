package com.wenhaiz.himusic.data.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

public class Artist implements Parcelable {
    @SerializedName("artistId")
    private String artistId;
    @SerializedName("artistStringId")
    private String artistStringId;
    @SerializedName("artistName")
    private String artistName;
    private String desc;
    private String miniImgUrl;
    @SerializedName("artistLogo")
    private String imgUrl;

    public Artist() {
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getArtistId() {
        return artistId;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    public String getMiniImgUrl() {
        return TextUtils.isEmpty(miniImgUrl)?imgUrl:miniImgUrl;
    }

    public void setMiniImgUrl(String miniImgUrl) {
        this.miniImgUrl = miniImgUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }


    protected Artist(Parcel in) {
        artistId = in.readString();
        artistStringId = in.readString();
        artistName = in.readString();
        desc = in.readString();
        miniImgUrl = in.readString();
        imgUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(artistId);
        dest.writeString(artistStringId);
        dest.writeString(artistName);
        dest.writeString(desc);
        dest.writeString(miniImgUrl);
        dest.writeString(imgUrl);
    }

    public String getArtistStringId() {
        return artistStringId;
    }

    public void setArtistStringId(String artistStringId) {
        this.artistStringId = artistStringId;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Artist> CREATOR = new Creator<Artist>() {
        @Override
        public Artist createFromParcel(Parcel in) {
            return new Artist(in);
        }

        @Override
        public Artist[] newArray(int size) {
            return new Artist[size];
        }
    };
}

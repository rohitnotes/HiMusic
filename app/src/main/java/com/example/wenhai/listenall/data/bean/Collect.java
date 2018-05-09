package com.example.wenhai.listenall.data.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.wenhai.listenall.data.MusicProvider;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Transient;
import io.objectbox.relation.ToMany;

/**
 * 歌单对应实体类
 * Created by Wenhai on 2017/7/30.
 */

@Entity
public class Collect implements Parcelable {
    public static final Creator<Collect> CREATOR = new Creator<Collect>() {
        @Override
        public Collect createFromParcel(Parcel in) {
            return new Collect(in);
        }

        @Override
        public Collect[] newArray(int size) {
            return new Collect[size];
        }
    };
    //歌单所有歌曲
    public ToMany<Song> songs;
    @Id(assignable = true)
    private Long id;//本地数据库 id
    private String title;
    private String desc;//简介
    private long collectId;//歌单 曲库中的 collectId
    private String coverUrl;//封面 url
    private int coverDrawable;//封面 drawable
    private int songCount;//包含歌曲数量
    private int songDownloadNumber;//已下载歌曲数量
    @Transient
    private MusicProvider source;//来源
    private String providerName;
    private int playTimes;//播放次数
    private long createDate;//创建时间
    private long updateDate;//更新时间
    private boolean isFromUser = false;//是否是用户创建的

    public Collect() {

    }

    protected Collect(Parcel in) {
        title = in.readString();
        desc = in.readString();
        collectId = in.readLong();
        coverUrl = in.readString();
        coverDrawable = in.readInt();
        songCount = in.readInt();
        songDownloadNumber = in.readInt();
        playTimes = in.readInt();
        createDate = in.readLong();
        updateDate = in.readLong();
        songs.addAll(in.createTypedArrayList(Song.CREATOR));
        isFromUser = in.readInt() == 1;
    }

    public Collect(Long id, String title, String desc, long collectId,
                   String coverUrl, int coverDrawable, int songCount,
                   int songDownloadNumber, String providerName, int playTimes,
                   long createDate, long updateDate, boolean isFromUser) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.collectId = collectId;
        this.coverUrl = coverUrl;
        this.coverDrawable = coverDrawable;
        this.songCount = songCount;
        this.songDownloadNumber = songDownloadNumber;
        this.providerName = providerName;
        this.playTimes = playTimes;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.isFromUser = isFromUser;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public long getCollectId() {
        return collectId;
    }

    public void setCollectId(long collectId) {
        this.collectId = collectId;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public int getCoverDrawable() {
        return coverDrawable;
    }

    public void setCoverDrawable(int coverDrawable) {
        this.coverDrawable = coverDrawable;
    }

    public int getSongCount() {
        return songCount;
    }

    public void setSongCount(int songCount) {
        this.songCount = songCount;
    }

    public int getSongDownloadNumber() {
        return songDownloadNumber;
    }

    public void setSongDownloadNumber(int songDownloadNumber) {
        this.songDownloadNumber = songDownloadNumber;
    }

    public MusicProvider getSource() {
        return source;
    }

    public void setSource(MusicProvider source) {
        this.source = source;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public int getPlayTimes() {
        return playTimes;
    }

    public void setPlayTimes(int playTimes) {
        this.playTimes = playTimes;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public long getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(long updateDate) {
        this.updateDate = updateDate;
    }


    @Override
    public String toString() {
        return "Collect{" +
                "title='" + title + '\'' +
                ", collectId=" + collectId +
                ", coverUrl='" + coverUrl + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(desc);
        parcel.writeLong(collectId);
        parcel.writeString(coverUrl);
        parcel.writeInt(coverDrawable);
        parcel.writeInt(songCount);
        parcel.writeInt(songDownloadNumber);
        parcel.writeInt(playTimes);
        parcel.writeLong(createDate);
        parcel.writeLong(updateDate);
        parcel.writeTypedList(songs);
        parcel.writeInt(isFromUser ? 1 : 0);
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean getIsFromUser() {
        return this.isFromUser;
    }

    public void setIsFromUser(boolean isFromUser) {
        this.isFromUser = isFromUser;
    }

}

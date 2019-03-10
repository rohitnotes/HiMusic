package com.wenhaiz.himusic.http.data;

import com.google.gson.annotations.SerializedName;
import com.wenhaiz.himusic.data.bean.Collect;
import com.wenhaiz.himusic.data.bean.Song;

import java.io.Serializable;
import java.util.List;

import io.objectbox.relation.ToMany;

public class RankList {

    @SerializedName("xiamiBillboards")
    private List<Rank> xiamiRanks;
    @SerializedName("globalBillboards")
    private List<Rank> globalRank;
    @SerializedName("spBillboards")
    private List<Rank> spRanks;

    public List<Rank> getXiamiRanks() {
        return xiamiRanks;
    }

    public void setXiamiRanks(List<Rank> xiamiRanks) {
        this.xiamiRanks = xiamiRanks;
    }

    public List<Rank> getGlobalRank() {
        return globalRank;
    }

    public void setGlobalRank(List<Rank> globalRank) {
        this.globalRank = globalRank;
    }

    public List<Rank> getSpRanks() {
        return spRanks;
    }

    public void setSpRanks(List<Rank> spRanks) {
        this.spRanks = spRanks;
    }

    public class Rank implements Serializable {
        @SerializedName("billboardId")
        private long billboardId;
        @SerializedName("code")
        private String code;
        @SerializedName("name")
        private String name;
        @SerializedName("type")
        private int type;
        @SerializedName("logo")
        private String logo;
        @SerializedName("thinLogo")
        private String thinLogo;
        @SerializedName("logoMiddle")
        private  String logoMiddle;
        @SerializedName("cycleType")
        private String cycleType;
        @SerializedName("updateDate")
        private String updateDate;
        @SerializedName("url")
        private String url;
        @SerializedName("description")
        private String description;
        @SerializedName("items")
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

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getThinLogo() {
            return thinLogo;
        }

        public void setThinLogo(String thinLogo) {
            this.thinLogo = thinLogo;
        }

        public String getLogoMiddle() {
            return logoMiddle;
        }

        public void setLogoMiddle(String logoMiddle) {
            this.logoMiddle = logoMiddle;
        }

        public String getCycleType() {
            return cycleType;
        }

        public void setCycleType(String cycleType) {
            this.cycleType = cycleType;
        }

        public String getUpdateDate() {
            return updateDate;
        }

        public void setUpdateDate(String updateDate) {
            this.updateDate = updateDate;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public List<Song> getSongs() {
            return songs;
        }

        public void setSongs(List<Song> songs) {
            this.songs = songs;
        }
    }
}

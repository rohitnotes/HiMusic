package com.wenhaiz.himusic.data.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlayInfo {

    @SerializedName("songPlayInfos")
    private List<Info> infos;

    public List<Info> getInfos() {
        return infos;
    }

    public void setInfos(List<Info> infos) {
        this.infos = infos;
    }

    public static class Info{
        @SerializedName("songId")
        private long songId;
        @SerializedName("playInfos")
        private List<InfoBean> playInfos;

        public long getSongId() {
            return songId;
        }

        public void setSongId(long songId) {
            this.songId = songId;
        }

        public List<InfoBean> getPlayInfos() {
            return playInfos;
        }

        public void setPlayInfos(List<InfoBean> playInfos) {
            this.playInfos = playInfos;
        }

        public static class InfoBean{
            @SerializedName("listenFile")
            private String listenFile;
            @SerializedName("playVolume")
            private int playVolume;
            @SerializedName("fileSize")
            private int fileSize;
            @SerializedName("downloadFileSize")
            private int downloadFileSize;
            @SerializedName("length")
            private int length;
            @SerializedName("operations")
            private List<Operation> operations;

            public String getListenFile() {
                return listenFile;
            }

            public void setListenFile(String listenFile) {
                this.listenFile = listenFile;
            }

            public int getPlayVolume() {
                return playVolume;
            }

            public void setPlayVolume(int playVolume) {
                this.playVolume = playVolume;
            }

            public int getFileSize() {
                return fileSize;
            }

            public void setFileSize(int fileSize) {
                this.fileSize = fileSize;
            }

            public int getDownloadFileSize() {
                return downloadFileSize;
            }

            public void setDownloadFileSize(int downloadFileSize) {
                this.downloadFileSize = downloadFileSize;
            }

            public int getLength() {
                return length;
            }

            public void setLength(int length) {
                this.length = length;
            }

            public List<Operation> getOperations() {
                return operations;
            }

            public void setOperations(List<Operation> operations) {
                this.operations = operations;
            }

        }


    }

    public static class Operation{
        @SerializedName("purpose")
        private int purpose;
        @SerializedName("upgradeRole")
        private int upgradeRole;
        @SerializedName("needVip")
        private boolean needVip;
        @SerializedName("needPay")
        private boolean needPay;

        public int getPurpose() {
            return purpose;
        }

        public void setPurpose(int purpose) {
            this.purpose = purpose;
        }

        public int getUpgradeRole() {
            return upgradeRole;
        }

        public void setUpgradeRole(int upgradeRole) {
            this.upgradeRole = upgradeRole;
        }
    }


}

package com.wenhaiz.himusic.http.data;

import com.google.gson.annotations.SerializedName;
import com.wenhaiz.himusic.data.bean.Collect;

import java.util.ArrayList;

/**
 * Created by wm on 2016/7/28.
 */
public class RecommendListRecommendInfo {


    @SerializedName("collects")
    private ArrayList<com.wenhaiz.himusic.data.bean.Collect> collects;

    @SerializedName("pagingV0")
    private Paging paging;


    public ArrayList<Collect> getCollects() {
        return collects;
    }

    public void setCollects(ArrayList<Collect> collects) {
        this.collects = collects;
    }

    public Paging getPaging() {
        return paging;
    }

    public void setPaging(Paging paging) {
        this.paging = paging;
    }

    private static class Paging{
        @SerializedName("page")
        private int page;
        @SerializedName("pageSize")
        private int pageSize;
        @SerializedName("pages")
        private int pages;
        @SerializedName("count")
        private int totalCount;
    }

}

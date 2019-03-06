package com.wenhaiz.himusic.http.data;

import com.google.gson.annotations.SerializedName;

public class QueryPage {
    @SerializedName("page")
    private int page;
    @SerializedName("pageSize")
    private int pageSize;

    public QueryPage(int page, int pageSize) {
        this.page = page;
        this.pageSize = pageSize;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}

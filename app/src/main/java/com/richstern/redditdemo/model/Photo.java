package com.richstern.redditdemo.model;

import com.google.gson.annotations.SerializedName;

public class Photo {

    @SerializedName("thumbnail")
    private String mThumbnail;

    @SerializedName("title")
    private String mTitle;

    public String getThumbnail() {
        return mThumbnail;
    }

    public String getTitle() {
        return mTitle;
    }
}

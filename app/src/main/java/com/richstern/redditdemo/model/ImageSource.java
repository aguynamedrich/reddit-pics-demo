package com.richstern.redditdemo.model;

import com.google.gson.annotations.SerializedName;

public class ImageSource {

    @SerializedName("url")
    private String mUrl;

    public String getUrl() {
        return mUrl;
    }
}

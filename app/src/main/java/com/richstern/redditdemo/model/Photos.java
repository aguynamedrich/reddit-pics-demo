package com.richstern.redditdemo.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Photos {

    @SerializedName("children")
    private List<Photo> mPhotos;

    public List<Photo> getPhotos() {
        return mPhotos;
    }
}

package com.richstern.redditdemo.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Photos {

    @SerializedName("children")
    private List<Photo> mPhotos;

    @SerializedName("after")
    private String mAfter;

    public List<Photo> getPhotos() {
        return mPhotos;
    }

    public String getAfter() {
        return mAfter;
    }

    /**
     * Merge an incoming dataset into an existing list
     */
    public void mergeWith(Photos photos) {
        mAfter = photos.getAfter();
        mPhotos.addAll(photos.getPhotos());
    }
}

package com.richstern.redditdemo.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ImagePreview {

    @SerializedName("images")
    private List<Image> mImages;

    public List<Image> getImages() {
        return mImages;
    }
}

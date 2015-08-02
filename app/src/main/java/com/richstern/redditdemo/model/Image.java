package com.richstern.redditdemo.model;

import com.google.gson.annotations.SerializedName;

public class Image {

    @SerializedName("source")
    private ImageSource mImageSource;

    public ImageSource getImageSource() {
        return mImageSource;
    }
}

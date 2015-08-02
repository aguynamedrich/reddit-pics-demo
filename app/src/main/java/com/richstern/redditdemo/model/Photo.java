package com.richstern.redditdemo.model;

import com.google.gson.annotations.SerializedName;
import com.richstern.redditdemo.util.StringUtils;

public class Photo {

    public static final String BASE_URL = "http://www.reddit.com";

    @SerializedName("thumbnail")
    private String mThumbnail;

    @SerializedName("title")
    private String mTitle;

    @SerializedName("preview")
    private ImagePreview mImagePreview;

    @SerializedName("permalink")
    private String mPermalink;

    private String mSourceUrl;

    public ImagePreview getImagePreview() {
        return mImagePreview;
    }

    public String getPermalink() {
        return BASE_URL + mPermalink;
    }

    public String getThumbnail() {
        return mThumbnail;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getSourceUrl() {
        return mSourceUrl;
    }

    public void processImageSource() {
        if (mImagePreview != null && mImagePreview.getImages() != null && mImagePreview.getImages().size() > 0) {
            ImageSource source = mImagePreview.getImages().get(0).getImageSource();
            if (StringUtils.isValidUrl(source.getUrl())) {
                mSourceUrl = source.getUrl();
            }
        }
    }
}

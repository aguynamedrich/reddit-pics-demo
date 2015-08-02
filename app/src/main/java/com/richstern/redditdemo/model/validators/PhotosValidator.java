package com.richstern.redditdemo.model.validators;

import com.richstern.redditdemo.model.Photo;
import com.richstern.redditdemo.model.Photos;
import com.richstern.redditdemo.util.StringUtils;

public class PhotosValidator {
    public static boolean hasAfter(Photos photos) {
        return photos != null && photos.getAfter() != null && photos.getAfter().length() > 0;
    }

    public static Boolean isSourceValid(Photo photo) {
        return StringUtils.isValidUrl(photo.getSourceUrl());
    }
}

package com.richstern.redditdemo.model.validators;

import com.richstern.redditdemo.model.Photos;

public class PhotosValidator {
    public static boolean hasAfter(Photos photos) {
        return photos != null && photos.getAfter() != null && photos.getAfter().length() > 0;
    }
}

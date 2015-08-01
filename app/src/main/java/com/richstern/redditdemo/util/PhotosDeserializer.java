package com.richstern.redditdemo.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.richstern.redditdemo.model.Photo;

public class PhotosDeserializer extends PhotoDeserializerBase {

    @Override
    protected Gson getGson() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Photo.class, new PhotoDeserializer());
        return builder.create();
    }
}

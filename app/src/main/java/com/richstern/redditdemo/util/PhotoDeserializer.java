package com.richstern.redditdemo.util;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.richstern.redditdemo.model.Photo;

import java.lang.reflect.Type;

public class PhotoDeserializer extends PhotoDeserializerBase {

    @Override
    public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
        throws JsonParseException {
        Photo photo = (Photo) super.deserialize(json, typeOfT, context);
        photo.processImageSource();
        return photo;
    }

    @Override
    protected Gson getGson() {
        return new Gson();
    }
}

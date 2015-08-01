package com.richstern.redditdemo.util;

import com.google.gson.Gson;

public class PhotoDeserializer extends PhotoDeserializerBase {

    @Override
    protected Gson getGson() {
        return new Gson();
    }
}

package com.richstern.redditdemo.util;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * This allows us to pull out whatever is in the "data" element and treat the underlying data as the object we expect
 */
public abstract class PhotoDeserializerBase implements JsonDeserializer {

    protected Gson mGson = getGson();

    protected abstract Gson getGson();

    @Override
    public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
        throws JsonParseException {

        JsonObject outer = json.getAsJsonObject();
        if (outer.has("data")) {
            JsonElement inner = outer.get("data");
            return mGson.fromJson(inner, typeOfT);
        }

        // Fall back to default implementation
        return mGson.fromJson(json, typeOfT);
    }
}

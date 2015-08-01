package com.richstern.redditdemo.net;

import com.richstern.redditdemo.model.Photos;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

public interface RedditPhotoService {

    @GET("/r/pics/hot.json")
    Observable<Photos> getHotPhotos(@Query("after") String after);
}

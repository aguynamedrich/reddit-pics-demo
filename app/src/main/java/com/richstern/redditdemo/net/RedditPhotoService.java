package com.richstern.redditdemo.net;

import com.richstern.redditdemo.model.Photos;
import retrofit.http.GET;
import rx.Observable;

public interface RedditPhotoService {

    @GET("/r/pics/hot.json")
    Observable<Photos> getHotPhotos();
}

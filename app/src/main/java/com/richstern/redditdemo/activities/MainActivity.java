package com.richstern.redditdemo.activities;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.google.gson.GsonBuilder;
import com.richstern.redditdemo.R;
import com.richstern.redditdemo.adapters.PhotosAdapter;
import com.richstern.redditdemo.model.Photos;
import com.richstern.redditdemo.net.RedditPhotoService;
import com.richstern.redditdemo.util.PhotosDeserializer;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends RxAppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static final String REDDIT_BASE_URL = "http://www.reddit.com";

    @InjectView(R.id.toolbar) Toolbar mToolbar;
    @InjectView(R.id.swipe) SwipeRefreshLayout mSwipeRefreshLayout;
    @InjectView(R.id.list) RecyclerView mRecyclerView;

    RedditPhotoService mPhotoService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Init UI components
        ButterKnife.inject(this);
        setSupportActionBar(mToolbar);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(
            getResources().getColor(R.color.flipagram_red),
            getResources().getColor(R.color.accent)
        );

        initServices();
        initData();
    }

    @Override
    public void onRefresh() {
        initData();
    }

    private void initData() {
        mPhotoService.getHotPhotos()
            .compose(bindToLifecycle())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::bind, this::onErrorDownloading);
    }

    private void onErrorDownloading(Throwable throwable) {
        mSwipeRefreshLayout.setRefreshing(false);
        Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }

    private void initServices() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Photos.class, new PhotosDeserializer());

        RestAdapter restAdapter =new RestAdapter.Builder()
            .setEndpoint(REDDIT_BASE_URL)
            .setConverter(new GsonConverter(gsonBuilder.create()))
            .build();

        mPhotoService = restAdapter.create(RedditPhotoService.class);
    }

    private void bind(Photos photos) {
        mSwipeRefreshLayout.setRefreshing(false);
        mRecyclerView.setAdapter(new PhotosAdapter(photos.getPhotos()));
    }
}

package com.richstern.redditdemo.activities;

import android.content.Intent;
import android.net.Uri;
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
import com.richstern.redditdemo.model.Photo;
import com.richstern.redditdemo.model.Photos;
import com.richstern.redditdemo.model.validators.PhotosValidator;
import com.richstern.redditdemo.net.RedditPhotoService;
import com.richstern.redditdemo.rx.OnlyNextObserver;
import com.richstern.redditdemo.util.PhotosDeserializer;
import com.richstern.redditdemo.util.StringUtils;
import com.trello.rxlifecycle.ActivityEvent;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

public class MainActivity extends RxAppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static final String REDDIT_BASE_URL = "http://www.reddit.com";

    @InjectView(R.id.toolbar) Toolbar mToolbar;
    @InjectView(R.id.swipe) SwipeRefreshLayout mSwipeRefreshLayout;
    @InjectView(R.id.list) RecyclerView mRecyclerView;

    private final BehaviorSubject<String> mFetchAfterSubject = BehaviorSubject.create();

    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int itemCount = mLayoutManager.getItemCount();
            int lastVisibleItemPosition = mLayoutManager.findLastVisibleItemPosition();
            if (lastVisibleItemPosition == itemCount - 1 && PhotosValidator.hasAfter(mPhotos)) {
                mFetchAfterSubject.onNext(mPhotos.getAfter());
            }
        }
    };

    RedditPhotoService mPhotoService;
    Photos mPhotos;
    PhotosAdapter mPhotosAdapter;
    LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Init UI components
        ButterKnife.inject(this);
        setSupportActionBar(mToolbar);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addOnScrollListener(mOnScrollListener);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(
            getResources().getColor(R.color.accent),
            getResources().getColor(R.color.flipagram_red)
        );

        initServices();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRecyclerView.removeOnScrollListener(mOnScrollListener);
    }

    @Override
    public void onRefresh() {
        initData();
    }

    private void initData() {
        // Initial load
        mPhotoService.getHotPhotos(null)
            .doOnNext(__ -> runOnUiThread(() -> mSwipeRefreshLayout.setRefreshing(true)))
            .compose(bindToLifecycle())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::bind, this::onErrorDownloading);

        // Observable for loading more pages
        mFetchAfterSubject
            .filter(after -> !StringUtils.isNullOrEmpty(after))
            .distinctUntilChanged()
            .switchMap(mPhotoService::getHotPhotos)
            .doOnNext(__ -> runOnUiThread(() -> mSwipeRefreshLayout.setRefreshing(true)))
            .compose(bindUntilEvent(ActivityEvent.DESTROY))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::merge, this::onErrorDownloading);
    }

    private void merge(Photos photos) {
        mSwipeRefreshLayout.setRefreshing(false);
        mPhotos.mergeWith(photos);
        mPhotosAdapter.notifyDataSetChanged();
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
        mPhotos = photos;
        mPhotosAdapter = new PhotosAdapter(mPhotos.getPhotos());
        mRecyclerView.setAdapter(mPhotosAdapter);

        mPhotosAdapter.getThumbnailClickedSubject()
            .filter(PhotosValidator::isSourceValid)
            .compose(bindUntilEvent(ActivityEvent.DESTROY))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(OnlyNextObserver.forAction(this::showImage));

        mPhotosAdapter.getItemClickedSubject()
            .filter(photo -> StringUtils.isValidUrl(photo.getPermalink()))
            .compose(bindUntilEvent(ActivityEvent.DESTROY))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(OnlyNextObserver.forAction(this::showPost));
    }

    private void showPost(Photo photo) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(photo.getPermalink()));
        startActivity(intent);
    }

    private void showImage(Photo photo) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(photo.getSourceUrl()));
        startActivity(intent);
    }
}

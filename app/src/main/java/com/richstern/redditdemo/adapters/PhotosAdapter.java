package com.richstern.redditdemo.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.richstern.redditdemo.R;
import com.richstern.redditdemo.model.Photo;
import com.squareup.picasso.Picasso;
import rx.Observable;
import rx.subjects.BehaviorSubject;

import java.util.List;

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.PhotoViewHolder> {

    private final BehaviorSubject<Photo> mThumbnailClickedSubject = BehaviorSubject.create();
    private final BehaviorSubject<Photo> mItemClickedSubject = BehaviorSubject.create();

    private final List<Photo> mPhotos;

    public PhotosAdapter(List<Photo> photos) {
        mPhotos = photos;
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.list_item_photo, parent, false);
        return new PhotoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder holder, int position) {
        Photo photo = mPhotos.get(position);
        Picasso.with(holder.itemView.getContext()).load(photo.getThumbnail()).into(holder.mThumbnail);
        holder.mTitle.setText(photo.getTitle());
        holder.itemView.setOnClickListener(__ -> mItemClickedSubject.onNext(photo));
        holder.mThumbnail.setOnClickListener(__ -> mThumbnailClickedSubject.onNext(photo));
    }

    @Override
    public int getItemCount() {
        return mPhotos.size();
    }

    public Observable<Photo> getThumbnailClickedSubject() {
        return mThumbnailClickedSubject.asObservable();
    }

    public Observable<Photo> getItemClickedSubject() {
        return mItemClickedSubject.asObservable();
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.thumbnail) ImageView mThumbnail;
        @InjectView(R.id.title) TextView mTitle;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}

package com.vincentangway.popularmovies.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vincentangway.popularmovies.R;
import com.vincentangway.popularmovies.data.realm.RealmObjectVideo;
import com.vincentangway.popularmovies.data.model.Video;
import com.vincentangway.popularmovies.ui.fragment.MoviesDetailsFragment;

import java.util.List;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class VideosAdapter extends RealmRecyclerViewAdapter<RealmObjectVideo, RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_DEFAULT = 1;
    private static final int VIEW_TYPE_WITH_TITLE = 2;
    Context context;
    MoviesDetailsFragment moviesDetailsFragment;

    public VideosAdapter(Context context, OrderedRealmCollection<RealmObjectVideo> data, MoviesDetailsFragment moviesDetailsFragment) {
        super(context, data, true);
        this.context = context;
        this.moviesDetailsFragment = moviesDetailsFragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View view;

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
        viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return VIEW_TYPE_WITH_TITLE;
        else
            return VIEW_TYPE_DEFAULT;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        RealmObjectVideo video = new RealmObjectVideo();
        if (getData() != null)
            video = getData().get(position);

        ViewHolder holder = (ViewHolder) viewHolder;
        Picasso.with(context)
                .load(String.format(context.getString(R.string.youtube_thumbnail_format), video.getKey()))
                .placeholder(R.drawable.youtube_placeholder)
                .into(holder.thumbnailImageView);
        holder.titleTextView.setText(video.getName());
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView thumbnailImageView;
        TextView titleTextView;
        View share;
        View clickReceiver;

        public ViewHolder(View itemView) {
            super(itemView);
            thumbnailImageView = (ImageView) itemView.findViewById(R.id.thumbnail_image_view);
            titleTextView = (TextView) itemView.findViewById(R.id.title_text_view);
            clickReceiver = itemView.findViewById(R.id.item_video_click_receiver);
            share = itemView.findViewById(R.id.share_action_image_view);
            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getData() != null)
                        moviesDetailsFragment.handleVideoShare(getData().get(getLayoutPosition()));
                }
            });
            clickReceiver.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (getData() != null)
                moviesDetailsFragment.handleVideoKey(getData().get(getLayoutPosition()).getKey());
        }
    }
}

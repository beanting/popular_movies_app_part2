package com.vincentangway.popularmovies.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.vincentangway.popularmovies.R;
import com.vincentangway.popularmovies.data.realm.RealmObjectCastMember;
import com.vincentangway.popularmovies.ui.fragment.MoviesDetailsFragment;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class CastMembersAdapter extends RealmRecyclerViewAdapter<RealmObjectCastMember, RecyclerView.ViewHolder> {

    Context context;
    MoviesDetailsFragment moviesDetailsFragment;

    public CastMembersAdapter(Context context, OrderedRealmCollection<RealmObjectCastMember> data, MoviesDetailsFragment moviesDetailsFragment) {
        super(context, data, true);
        this.context = context;
        this.moviesDetailsFragment = moviesDetailsFragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cast_member, parent, false);
        viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        RealmObjectCastMember realmObjectCastMember = new RealmObjectCastMember();
        if (getData() != null)
            realmObjectCastMember = getData().get(position);

        ViewHolder holder = (ViewHolder) viewHolder;

        final String profileImageUrl = String.format(context.getString(R.string.image_url_format_w500)
                , realmObjectCastMember.getProfilePath());
        Picasso.with(context)
                .load(profileImageUrl)
                .placeholder(R.drawable.cast_member_place_holder)
                .fit()
                .centerCrop()
                .into(holder.castMemberProfileImageView);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView castMemberProfileImageView;
        public ViewHolder(View itemView) {
            super(itemView);
            castMemberProfileImageView = (ImageView) itemView.findViewById(R.id.item_cast_member_image_view);
        }
    }
}

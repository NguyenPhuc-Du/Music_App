package com.example.sound4you.ui.feed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.sound4you.R;
import com.example.sound4you.data.model.Track;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class FeedFollowingAdapter extends RecyclerView.Adapter<FeedFollowingAdapter.FeedFollowingViewHolder>{
    private final Context context;
    private final List<Track> data;

    public FeedFollowingAdapter(Context context, List<Track> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public FeedFollowingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_feed_following, parent, false);
        return new FeedFollowingViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedFollowingAdapter.FeedFollowingViewHolder viewHolder, int position) {
        Track track = data.get(position);
        viewHolder.tvTitle.setText(track.getTitle());
        viewHolder.tvUserName.setText(track.getArtist());

        Glide.with(context)
                .load(track.getCoverUrl())
                .into(viewHolder.ivCover);

        Glide.with(context)
                .load(track.getArtistProfilePicture())
                .placeholder(R.drawable.ic_avatar_placeholder)
                .into(viewHolder.ivAvatar);
    }

    @Override
    public int getItemCount() { return data.size();}

    static class FeedFollowingViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCover;
        ShapeableImageView ivAvatar;
        TextView tvTitle, tvUserName;

        FeedFollowingViewHolder(@NonNull View itemView) {
            super(itemView);

            ivCover = itemView.findViewById(R.id.ivTrackCoverBackground);
            ivAvatar = itemView.findViewById(R.id.ivTrackUserAvatarFeed);
            tvTitle = itemView.findViewById(R.id.tvTrackTitleFeed);
            tvUserName = itemView.findViewById(R.id.tvTrackUserNameFeed);
        }
    }
}

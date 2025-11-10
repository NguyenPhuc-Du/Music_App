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

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder> {
    private final Context context;
    private final List<Track> data;
    private OnFeedItemClickListener listener;

    public FeedAdapter(Context context, List<Track> data) {
        this.context = context;
        this.data = data;
    }

    public void setOnFeedItemClickListener(OnFeedItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_feed, parent, false);
        return new FeedViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedViewHolder holder, int position) {
        Track track = data.get(position);
        holder.tvTitle.setText(track.getTitle());
        holder.tvUserName.setText(track.getArtist());

        Glide.with(context)
                .load(track.getCoverUrl())
                .placeholder(R.drawable.ic_music_placeholder)
                .error(R.drawable.ic_music_placeholder)
                .into(holder.ivCover);

        Glide.with(context)
                .load(track.getArtistProfilePicture())
                .placeholder(R.drawable.ic_avatar_placeholder)
                .into(holder.ivAvatar);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(track);
            }
        });
    }

    @Override
    public int getItemCount() { return data.size(); }

    static class FeedViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCover;
        ShapeableImageView ivAvatar;
        TextView tvTitle, tvUserName;

        FeedViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCover = itemView.findViewById(R.id.ivTrackCoverBackground);
            ivAvatar = itemView.findViewById(R.id.ivTrackUserAvatarFeed);
            tvTitle = itemView.findViewById(R.id.tvTrackTitleFeed);
            tvUserName = itemView.findViewById(R.id.tvTrackUserNameFeed);
        }
    }

    public interface OnFeedItemClickListener {
        void onItemClick(Track track);
    }
}

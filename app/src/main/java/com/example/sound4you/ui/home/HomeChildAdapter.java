package com.example.sound4you.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;

import android.util.Log;

import com.example.sound4you.R;
import com.example.sound4you.data.model.Track;
import java.util.List;

public class HomeChildAdapter extends RecyclerView.Adapter<HomeChildAdapter.TrackViewHolder>{
    private final Context context;
    private final List<Track> trackList;
    private final OnTrackClickListener listener;

    public interface OnTrackClickListener {
        void onTrackClick(Track track);
    }

    public HomeChildAdapter(Context context, List<Track> trackList, OnTrackClickListener listener) {
        this.context = context;
        this.trackList = trackList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TrackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout = (trackList.size() == 4)
                ? R.layout.item_card_grid
                : R.layout.item_card_medium;

        View view = LayoutInflater.from(context).inflate(layout, parent, false);
        return new TrackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrackViewHolder holder, int position) {
        Track track = trackList.get(position);
        holder.tvTitle.setText(track.getTitle());
        holder.tvArtist.setText(track.getArtist());

        Glide.with(context)
                .load(track.getCoverUrl())
                .placeholder(R.drawable.ic_music_placeholder)
                .into(holder.ivCover);

        holder.itemView.setOnClickListener(v -> listener.onTrackClick(track));
    }

    @Override
    public int getItemCount() {
        return trackList.size();
    }

    static class TrackViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCover;
        TextView tvTitle;
        TextView tvArtist;

        public TrackViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCover = itemView.findViewById(R.id.ivTrackCoverGrid);
            if (ivCover == null) ivCover = itemView.findViewById(R.id.ivTrackCoverMedium);
            tvTitle = itemView.findViewById(R.id.tvTrackTitleGrid);
            if (tvTitle == null) tvTitle = itemView.findViewById(R.id.tvTrackTitleMedium);
            tvArtist = itemView.findViewById(R.id.tvTrackSourceGrid);
            if (tvArtist == null) tvArtist = itemView.findViewById(R.id.tvTrackSourceMedium);
        }
    }
}

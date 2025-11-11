package com.example.sound4you.ui.search;

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

import java.util.List;

public class SearchAdapterTrack extends RecyclerView.Adapter<SearchAdapterTrack.SearchTrackViewHolder>{
    public interface OnItemClick {
        void onClick(Track track);
    }

    private final Context context;
    private final List<Track> data;
    private OnItemClick listener;

    public SearchAdapterTrack(Context context, List<Track> data) {
        this.context = context;
        this.data = data;
    }

    public void setOnItemClick(OnItemClick listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public SearchTrackViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_search_tracks, parent, false);
        return new SearchTrackViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchTrackViewHolder holder, int position) {
        Track track = data.get(position);

        holder.tvTrackTitle.setText(track.getTitle());
        holder.tvTrackArtist.setText(track.getArtist());

        Glide.with(context)
                .load(track.getCoverUrl() != null ? track.getCoverUrl() : R.drawable.ic_music_placeholder)
                .into(holder.ivTrackCover);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onClick(track);
        });

        holder.btnLike.setOnClickListener(v -> {
            holder.btnLike.animate().scaleX(0.8f).scaleY(0.8f).setDuration(100)
                    .withEndAction(() -> holder.btnLike.animate().scaleX(1f).scaleY(1f).start())
                    .start();
        });
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    static class SearchTrackViewHolder extends RecyclerView.ViewHolder {
        ImageView ivTrackCover;
        TextView tvTrackTitle, tvTrackArtist;
        ImageView btnLike;
        SearchTrackViewHolder(@NonNull View v) {
            super(v);
            ivTrackCover = v.findViewById(R.id.ivTrackCover);
            tvTrackTitle = v.findViewById(R.id.tvTrackTitle);
            tvTrackArtist = v.findViewById(R.id.tvTrackArtist);
            btnLike = v.findViewById(R.id.btnLike);
        }
    }
}

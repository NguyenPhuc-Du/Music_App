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
    public void onBindViewHolder(@NonNull SearchTrackViewHolder viewHolder, int position) {
        Track track = data.get(position);
        viewHolder.tvTrackTitle.setText(track.getTitle());
        viewHolder.tvTrackArtist.setText(track.getArtist());

        Glide.with(context)
                .load(track.getArtistProfilePicture())
                .placeholder(R.drawable.ic_avatar_placeholder)
                .into(viewHolder.ivTrackCover);

        viewHolder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClick(track);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    static class SearchTrackViewHolder extends RecyclerView.ViewHolder {
        ImageView ivTrackCover;
        TextView tvTrackTitle;
        TextView tvTrackArtist;

        SearchTrackViewHolder(@NonNull View v) {
            super(v);

            ivTrackCover = v.findViewById(R.id.ivTrackCover);
            tvTrackTitle = v.findViewById(R.id.tvTrackTitle);
            tvTrackArtist = v.findViewById(R.id.tvTrackArtist);
        }
    }
}

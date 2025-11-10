package com.example.sound4you.ui.admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sound4you.R;
import com.example.sound4you.data.model.Track;

import java.util.ArrayList;
import java.util.List;

public class ManageTrackAdapter extends RecyclerView.Adapter<ManageTrackAdapter.TrackViewHolder> {
    private final List<Track> listTracks;
    private final TrackActionListener listener;

    public interface TrackActionListener {
        void onApproveClicked(int trackId);
        void onDeleteClicked(int trackId);
    }

    public ManageTrackAdapter(List<Track> listTracks, TrackActionListener listener) {
        this.listTracks = listTracks != null ? listTracks : new ArrayList<>();
        this.listener = listener;
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public TrackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_track, parent, false);
        return new TrackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrackViewHolder holder, int position) {
        Track track = listTracks.get(position);

        String avatarUrl = track.getCoverUrl();

        if (avatarUrl == null || avatarUrl.trim().isEmpty() || "NULL".equalsIgnoreCase(avatarUrl)) {
            Glide.with(holder.itemView.getContext())
                    .load(R.drawable.ic_logo)
                    .into(holder.imgMusicResId);
        } else {
            Glide.with(holder.itemView.getContext())
                    .load(avatarUrl)
                    .placeholder(R.drawable.ic_logo)
                    .error(R.drawable.ic_logo)
                    .into(holder.imgMusicResId);
        }

        holder.songNameTitle.setText(track.getTitle() != null ? track.getTitle() : "Unknown");
        holder.singerNameTitle.setText(track.getArtist() != null ? track.getArtist() : "Unknown");
        String status = track.getIsVerified() == 1 ? "Approved" : "Pending";
        holder.status.setText(status);

        holder.approveBtn.setOnClickListener(v -> {
            if (listener != null && track != null) listener.onApproveClicked(track.getId());
        });

        holder.deleteBtn.setOnClickListener(v -> {
            if (listener != null && track != null) listener.onDeleteClicked(track.getId());
        });
    }

    @Override
    public long getItemId(int position) {
        try {
            return listTracks.get(position) != null ? listTracks.get(position).getId() : position;
        } catch (Exception e) {
            return position;
        }
    }

    @Override
    public int getItemCount() {
        return listTracks.size();
    }

    public static class TrackViewHolder extends RecyclerView.ViewHolder {
        ImageView imgMusicResId;
        TextView songNameTitle, singerNameTitle, status;
        Button approveBtn, deleteBtn;

        public TrackViewHolder(@NonNull View itemView) {
            super(itemView);
            imgMusicResId = itemView.findViewById(R.id.logo_track);
            songNameTitle = itemView.findViewById(R.id.songName_title);
            singerNameTitle = itemView.findViewById(R.id.singerName_title);
            status = itemView.findViewById(R.id.status_song);
            approveBtn = itemView.findViewById(R.id.approve_track_btn);
            deleteBtn = itemView.findViewById(R.id.delete_track_btn);
        }
    }
}
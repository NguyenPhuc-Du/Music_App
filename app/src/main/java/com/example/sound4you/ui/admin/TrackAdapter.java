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

import java.util.List;

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.TrackViewHolder> {
    private List<TrackItem> listTracks;
    public ItemClickListener onTrackActionListener;

    public void setOnTrackActionListener(ItemClickListener onTrackActionListener) {
        this.onTrackActionListener = onTrackActionListener;
    }

    public TrackAdapter(List<TrackItem> listTracks) {
        this.listTracks = listTracks;
    }

    @NonNull
    @Override
    public TrackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_track, parent, false);

        return new TrackViewHolder(view, onTrackActionListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TrackViewHolder holder, int position) {

        TrackItem trackItem = listTracks.get(position);

        String avatarUrl = trackItem.getCover_url();

        if (avatarUrl == null || avatarUrl.trim().isEmpty() || avatarUrl.equals("NULL")) {
            Glide.with(holder.itemView.getContext())
                    .load(R.drawable.ic_logo) // ảnh mặc định
                    .into(holder.imgMusicResId);
        } else {
            Glide.with(holder.itemView.getContext())
                    .load(avatarUrl)
                    .placeholder(R.drawable.ic_logo)
                    .error(R.drawable.ic_logo)
                    .into(holder.imgMusicResId);
        }

        holder.songNameTitle.setText(trackItem.getTitle());
        holder.singerNameTitle.setText(trackItem.getArtist());
        String status = trackItem.getIs_verified() == 1 ? "Approved" : "Pending";
        holder.status.setText(status);
    }

    @Override
    public long getItemId(int i) {
        return i; // Returns a unique Indentifier for the item at the given position
    }

    @Override
    public int getItemCount() {
        return listTracks.size();
    }

    // Holds references to the views within an item layout
    public class TrackViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imgMusicResId;
        TextView songNameTitle, singerNameTitle, status;
        Button approveBtn, deleteBtn;

        public TrackViewHolder(@NonNull View itemView, final ItemClickListener onTrackListener) {
            super(itemView);
            imgMusicResId = itemView.findViewById(R.id.logo_track);
            songNameTitle = itemView.findViewById(R.id.songName_title);
            singerNameTitle = itemView.findViewById(R.id.singerName_title);
            status = itemView.findViewById(R.id.status_song);
            approveBtn = itemView.findViewById(R.id.approve_track_btn);
            deleteBtn = itemView.findViewById(R.id.delete_track_btn);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            approveBtn.setOnClickListener(v -> {
                if (onTrackActionListener != null) {
                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION) {
                        onTrackActionListener.onApproveClick(position);
                    }
                }
            });

            deleteBtn.setOnClickListener(v -> {
                if (onTrackActionListener != null) {
                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION) {
                        onTrackActionListener.onDeleteClick(position);
                    }
                }
            });
        }
    }
}

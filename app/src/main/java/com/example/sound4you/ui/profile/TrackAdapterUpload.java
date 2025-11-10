package com.example.sound4you.ui.profile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Handler;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.sound4you.R;
import com.example.sound4you.data.model.Track;
import java.util.List;

public class TrackAdapterUpload extends RecyclerView.Adapter<TrackAdapterUpload.VH> {

    public interface OnItemClick { void onClick(Track track); }
    public interface OnDeleteConfirmed { void onDelete(Track track); }
    public interface OnLikeClick { void onLikeClick(Track track, boolean isLiked); }

    private final Context context;
    private final List<Track> tracks;
    private final boolean isSelf;
    private OnItemClick click;
    private OnDeleteConfirmed deleteConfirmed;
    private OnLikeClick likeClick;

    public TrackAdapterUpload(Context context, List<Track> tracks, boolean isSelf) {
        this.context = context;
        this.tracks = tracks;
        this.isSelf = isSelf;
    }

    public void setOnItemClick(OnItemClick listener) { this.click = listener; }
    public void setOnDeleteConfirmed(OnDeleteConfirmed listener) { this.deleteConfirmed = listener; }
    public void setOnLikeClick(OnLikeClick listener) { this.likeClick = listener; }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_card_upload, parent, false);
        return new VH(v);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        Track track = tracks.get(position);
        h.title.setText(track.getTitle());
        h.artist.setText(track.getArtist());

        Glide.with(context)
                .load(track.getCoverUrl())
                .placeholder(R.drawable.ic_music_placeholder)
                .into(h.cover);

        int color = track.isLiked() ? Color.parseColor("#FF7A00") : Color.WHITE;
        h.like.setImageTintList(ColorStateList.valueOf(color));

        h.like.setImageResource(R.drawable.ic_like);
        h.like.setOnClickListener(v -> {
            boolean newState = !track.isLiked();
            track.setLiked(newState);
            h.like.setImageResource(R.drawable.ic_like);
            h.like.setImageTintList(ColorStateList.valueOf(newState ? Color.parseColor("#FF7A00") : Color.WHITE));

            if (likeClick != null) likeClick.onLikeClick(track, newState);
        });

        if (isSelf) {
            Handler handler = new Handler();
            Runnable holdAction = () -> new AlertDialog.Builder(context)
                    .setTitle("Xóa bài hát?")
                    .setMessage("Bạn có chắc muốn xóa \"" + track.getTitle() + "\" không?")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        if (deleteConfirmed != null) deleteConfirmed.onDelete(track);
                    })
                    .setNegativeButton("Hủy", null)
                    .show();

            h.itemView.setOnTouchListener((v, event) -> {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        handler.postDelayed(holdAction, 2500);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        handler.removeCallbacks(holdAction);
                        break;
                }
                return false;
            });
        }

        // Khi item được click
        h.itemView.setOnClickListener(v -> {
            if (click != null) click.onClick(track);
        });
    }

    @Override
    public int getItemCount() { return tracks != null ? tracks.size() : 0; }

    static class VH extends RecyclerView.ViewHolder {
        ImageView cover;
        TextView title, artist;
        ImageButton like;

        VH(View v) {
            super(v);
            cover = v.findViewById(R.id.ivTrackCoverUpload);
            title = v.findViewById(R.id.tvTrackTitleUpload);
            artist = v.findViewById(R.id.tvTrackSourceUpload);
            like = v.findViewById(R.id.btnLikeTrack);
        }
    }
}

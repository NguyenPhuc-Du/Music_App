package com.example.sound4you.ui.profile;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.sound4you.R;
import com.example.sound4you.data.model.Track;
import java.util.List;

public class TrackAdapterLike extends RecyclerView.Adapter<TrackAdapterLike.VH> {

    public interface OnItemClick { void onClick(Track track); }
    public interface OnLikeClick { void onLikeClick(Track track, boolean isLiked); }

    private final Context context;
    private final List<Track> tracks;
    private OnItemClick click;
    private OnLikeClick likeClick;

    public TrackAdapterLike(Context context, List<Track> tracks) {
        this.context = context;
        this.tracks = tracks;
    }

    public void setOnItemClick(OnItemClick listener) { this.click = listener; }
    public void setOnLikeClick(OnLikeClick listener) { this.likeClick = listener; }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_card_love, parent, false);
        return new VH(v);
    }

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
            cover = v.findViewById(R.id.ivTrackCoverLove);
            title = v.findViewById(R.id.tvTrackTitleLove);
            artist = v.findViewById(R.id.tvTrackSourceLove);
            like = v.findViewById(R.id.btnLikeTrack);
        }
    }
}

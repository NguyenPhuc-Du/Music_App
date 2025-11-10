package com.example.sound4you.ui.stream;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.sound4you.R;
import com.example.sound4you.data.model.Track;

public class NowPlayingBar extends ConstraintLayout{
    private TextView tvTrackTitle;
    private TextView tvTrackArtist;
    private ImageButton btnPlayPause;
    private ImageButton btnLike;
    private ImageButton btnFollow;
    private ProgressBar progressBar;

    private OnNowPlayingActionListener listener;

    public NowPlayingBar (Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.now_playing_bar, this, true);

        tvTrackTitle = findViewById(R.id.tvTrackTitleNowPlaying);
        tvTrackArtist = findViewById(R.id.tvTrackSourceNowPlaying);
        btnPlayPause = findViewById(R.id.btnPlayPause);
        btnLike = findViewById(R.id.btnLikeNowPlaying);
        btnFollow = findViewById(R.id.btnFollowNowPlaying);
        progressBar = findViewById(R.id.progressCircle);

        btnPlayPause.setOnClickListener(v -> {
            if (listener != null) {
                listener.onPlayPauseClicked();
            }
        });

        btnLike.setOnClickListener(v -> {
            if (listener != null) {
                listener.onLikeClicked();
            }
        });

        btnFollow.setOnClickListener(v -> {
            if (listener != null) {
                listener.onFollowClicked();
            }
        });
    }

    public void setOnNowPlayingActionListener(OnNowPlayingActionListener listener) {
        this.listener = listener;
    }

    public void updateTrackInfo(Track track) {
        tvTrackTitle.setText(track.getTitle());
        tvTrackArtist.setText((track.getArtist()));
    }

    public void updateProcess(int process) {
        progressBar.setProgress(process);
    }

    public void setPlaying(boolean isPlaying) {
        btnPlayPause.setImageResource(isPlaying ? R.drawable.ic_pause : R.drawable.ic_play);
    }

    public ImageButton getLikeButton() {
        return btnLike;
    }

    public ImageButton getFollowButton() {
        return btnFollow;
    }

    public interface OnNowPlayingActionListener {
        void onPlayPauseClicked();
        void onLikeClicked();
        void onFollowClicked();
    }
}

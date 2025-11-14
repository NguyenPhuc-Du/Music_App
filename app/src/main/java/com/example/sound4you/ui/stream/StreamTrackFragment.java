package com.example.sound4you.ui.stream;

import com.bumptech.glide.Glide;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sound4you.R;
import com.example.sound4you.data.model.Track;
import com.example.sound4you.utils.MediaPlayerManager;

public class StreamTrackFragment extends Fragment{

    private TextView tvTitle;
    private TextView tvArtist;
    private ImageView ivBackground;
    private ImageView ivPlayPause;
    private ImageView ivArtistAvatar;
    private FrameLayout playPause;
    private MediaPlayerManager playerManager;
    private Handler handler = new Handler();

    private NowPlayingBar nowPlayingBar;

    private Track currentTrack;

    public static StreamTrackFragment newInstance(Track track) {
        StreamTrackFragment fragment = new StreamTrackFragment();
        Bundle args = new Bundle();
        args.putSerializable("track", track);
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stream_track, container, false);

        tvTitle = view.findViewById(R.id.tvTrackTitleStream);
        tvArtist = view.findViewById(R.id.tvTrackUserNameStream);
        ivBackground = view.findViewById(R.id.ivTrackCoverBackgroundStream);
        ivPlayPause = view.findViewById(R.id.ivPlayPauseStream);
        ivArtistAvatar = view.findViewById(R.id.ivTrackUserAvatarStream);
        playPause = view.findViewById(R.id.gradientOverlayStream);

        playerManager = MediaPlayerManager.getInstance();

        if (!playerManager.isPlaying()) {
            ivPlayPause.setVisibility(View.VISIBLE);
        }

        if (getArguments() != null) {
            currentTrack = (Track) getArguments().getSerializable("track");
            if (currentTrack != null) {
                tvTitle.setText(currentTrack.getTitle());
                tvArtist.setText(currentTrack.getArtist());

                Glide.with(requireContext())
                        .load(currentTrack.getCoverUrl())
                        .placeholder(R.drawable.ic_music_placeholder)
                        .error(R.drawable.ic_music_placeholder)
                        .centerCrop()
                        .into(ivBackground);

                Glide.with(requireContext())
                        .load(currentTrack.getArtistProfilePicture())
                        .placeholder(R.drawable.ic_avatar_placeholder)
                        .error(R.drawable.ic_avatar_placeholder)
                        .centerCrop()
                        .into(ivArtistAvatar);
            }
        }

        playPause.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                float y = event.getY();
                int viewHeight = v.getHeight();

                float ignoreZone = dpToPx(150);
                if (y > viewHeight - ignoreZone) {
                    return false;
                }

                playerManager.toggle();
                ivPlayPause.animate()
                        .alpha(playerManager.isPlaying() ? 0f : 1f)
                        .setDuration(200)
                        .withEndAction(() ->
                                ivPlayPause.setVisibility(playerManager.isPlaying() ? View.INVISIBLE : View.VISIBLE)
                        )
                        .start();
                nowPlayingBar.setPlaying(playerManager.isPlaying());
                return true; // Đã xử lý touch
            }
            return false;
        });

        return view;
    }

    private float dpToPx(float dp) {
        return dp * getResources().getDisplayMetrics().density;
    }

    public void setNowPlayingBar(NowPlayingBar nowPlayingBar) {
        this.nowPlayingBar = nowPlayingBar;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacksAndMessages(null);
    }
}

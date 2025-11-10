package com.example.sound4you.utils;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.Log;
import java.io.IOException;

public class MediaPlayerManager {
    private static final String TAG = "MediaPlayerManager";
    private static MediaPlayerManager instance;
    private MediaPlayer mediaPlayer;
    private boolean isPrepared = false;
    private String currentUrl = null;

    private MediaPlayerManager() {}

    public static synchronized MediaPlayerManager getInstance() {
        if (instance == null) {
            instance = new MediaPlayerManager();
        }
        return instance;
    }

    public void play(String url, Runnable onPrepared) {
        if (url == null || url.isEmpty()) {
            Log.w(TAG, "play() called with null/empty url");
            return;
        }

        try {
            if (mediaPlayer != null && isPrepared && url.equals(currentUrl)) {
                if (!mediaPlayer.isPlaying()) mediaPlayer.start();
                if (onPrepared != null) onPrepared.run();
                return;
            }

            release();
            mediaPlayer = new MediaPlayer();
            currentUrl = url;
            isPrepared = false;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build());
            } else {
                mediaPlayer.setAudioStreamType(android.media.AudioManager.STREAM_MUSIC);
            }

            mediaPlayer.setLooping(false);

            mediaPlayer.setOnPreparedListener(mp -> {
                isPrepared = true;
                try {
                    mp.start();
                    if (onPrepared != null) onPrepared.run();
                } catch (IllegalStateException e) {
                    Log.e(TAG, "Error starting player", e);
                }
            });

            mediaPlayer.setOnErrorListener((mp, what, extra) -> {
                Log.e(TAG, "MediaPlayer error: what=" + what + ", extra=" + extra);
                isPrepared = false;
                new Thread(() -> {
                    try {
                        Thread.sleep(1000);
                        play(url, onPrepared);
                    } catch (InterruptedException ignored) {}
                }).start();
                return true;
            });

            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();

        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Error preparing player: " + e.getMessage());
        }
    }

    /** Dừng hoặc tiếp tục phát */
    public void toggle() {
        if (mediaPlayer == null) return;
        try {
            if (mediaPlayer.isPlaying()) mediaPlayer.pause();
            else if (isPrepared) mediaPlayer.start();
        } catch (IllegalStateException e) {
            Log.e(TAG, "toggle failed", e);
        }
    }

    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }

    public int getCurrentPosition() {
        if (mediaPlayer != null && isPrepared) {
            try { return mediaPlayer.getCurrentPosition(); }
            catch (IllegalStateException ignored) {}
        }
        return 0;
    }

    public int getDuration() {
        if (mediaPlayer != null && isPrepared) {
            try { return mediaPlayer.getDuration(); }
            catch (IllegalStateException ignored) {}
        }
        return 0;
    }

    public void seekTo(int positionMs) {
        if (mediaPlayer != null && isPrepared) {
            try { mediaPlayer.seekTo(positionMs); }
            catch (IllegalStateException ignored) {}
        }
    }

    public void release() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.stop();
                mediaPlayer.release();
            } catch (Exception ignored) {}
            mediaPlayer = null;
        }
        isPrepared = false;
        currentUrl = null;
    }
}

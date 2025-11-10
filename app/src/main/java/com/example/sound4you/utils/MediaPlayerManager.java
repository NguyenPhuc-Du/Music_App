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

    private MediaPlayerManager() {}

    public static synchronized MediaPlayerManager getInstance() {
        if (instance == null) instance = new MediaPlayerManager();
        return instance;
    }

    public void play(String url, Runnable onPrepared) {
        if (url == null || url.isEmpty()) {
            Log.w(TAG, "play() called with null or empty URL");
            return;
        }

        Log.d(TAG, "play() URL = " + url);

        release();
        mediaPlayer = new MediaPlayer();
        isPrepared = false;

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mediaPlayer.setAudioAttributes(
                        new AudioAttributes.Builder()
                                .setUsage(AudioAttributes.USAGE_MEDIA)
                                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                .build()
                );
            } else {
                mediaPlayer.setAudioStreamType(android.media.AudioManager.STREAM_MUSIC);
            }

            mediaPlayer.setOnPreparedListener(mp -> {
                isPrepared = true;
                mp.start();
                Log.d(TAG, "MediaPlayer prepared & started");
                if (onPrepared != null) onPrepared.run();
            });

            mediaPlayer.setOnErrorListener((mp, what, extra) -> {
                Log.e(TAG, "MediaPlayer error: what=" + what + ", extra=" + extra + ", url=" + url);
                isPrepared = false;
                return true;
            });

            mediaPlayer.setOnCompletionListener(mp -> {
                Log.d(TAG, "Playback completed");
            });

            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();
            Log.d(TAG, "prepareAsync() called");

        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Unexpected error: " + e.getMessage());
        }
    }

    public void toggle() {
        if (mediaPlayer == null) return;
        try {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                Log.d(TAG, "Paused");
            } else if (isPrepared) {
                mediaPlayer.start();
                Log.d(TAG, "Resumed");
            }
        } catch (IllegalStateException e) {
            Log.e(TAG, "toggle() invalid state", e);
        }
    }

    public boolean isPlaying() {
        try {
            return mediaPlayer != null && mediaPlayer.isPlaying();
        } catch (IllegalStateException e) {
            return false;
        }
    }

    public int getDuration() {
        try {
            return (mediaPlayer != null && isPrepared) ? mediaPlayer.getDuration() : 0;
        } catch (Exception e) { return 0; }
    }

    public int getCurrentPosition() {
        try {
            return (mediaPlayer != null && isPrepared) ? mediaPlayer.getCurrentPosition() : 0;
        } catch (Exception e) { return 0; }
    }

    public void release() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.stop();
                mediaPlayer.release();
                Log.d(TAG, "MediaPlayer released");
            } catch (Exception ignored) {}
            mediaPlayer = null;
        }
        isPrepared = false;
    }
}

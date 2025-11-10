package com.example.sound4you.ui.stream;

import com.example.sound4you.data.model.Track;

import java.util.List;

public interface LikeStreamView {
    void onLikeChanged(boolean liked);
    void onLikeStatusChecked(boolean liked);
    void onTracksUpdated(List<Track> tracks);
    void onError(String msg);
}

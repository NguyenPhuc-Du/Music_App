package com.example.sound4you.ui.track;

import com.example.sound4you.data.model.Track;
import java.util.List;

public interface TrackView {
    void showLoading();

    void hideLoading();

    void onTracksLoaded(List<Track> tracks);

    void onError(String msg);
}

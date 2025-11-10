package com.example.sound4you.ui.feed;

import com.example.sound4you.data.model.Track;
import java.util.List;

public interface FeedView {
    void showFeed(List<Track> tracks);
    void showError(String message);
    void showLoading();
    void hideLoading();
}

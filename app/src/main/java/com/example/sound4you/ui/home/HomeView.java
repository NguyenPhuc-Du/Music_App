package com.example.sound4you.ui.home;

import com.example.sound4you.data.model.Track;
import java.util.List;

public interface HomeView {
    void showTracks(List<Track> tracks);
    void showLoading(boolean isLoading);
    void showError(String message);
}

package com.example.sound4you.ui.search;

import com.example.sound4you.data.model.Genre;
import com.example.sound4you.data.model.Track;
import com.example.sound4you.data.model.User;

import java.util.List;

public interface SearchView {
    void showTracks(List<Track> tracks);
    void showUsers(List<User> users);
    void showError(String err);
    void showLoading();
    void hideLoading();
}

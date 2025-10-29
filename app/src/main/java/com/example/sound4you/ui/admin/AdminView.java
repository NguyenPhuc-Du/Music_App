package com.example.sound4you.ui.admin;

import com.example.sound4you.data.model.Track;
import com.example.sound4you.data.model.User;
import java.util.List;
import java.util.Map;

public interface AdminView {
    void onUsersLoaded(List<User> users);
    void onTracksLoaded(List<Track> tracks);
    void onStatsLoaded(Map<String, Integer> stats);
    void onError(String message);

    void onMessage(String message);
}

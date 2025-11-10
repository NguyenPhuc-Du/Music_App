package com.example.sound4you.ui.admin;

import com.example.sound4you.data.model.Track;
import java.util.List;

public interface ManageTrackView {
    void onTracksLoaded(List<Track> tracks);
    void onTrackApproved(int id);
    void onTrackDeleted(int id);
    void onError(String msg);
}

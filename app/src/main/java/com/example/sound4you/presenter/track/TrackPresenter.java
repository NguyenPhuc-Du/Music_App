package com.example.sound4you.presenter.track;

public interface TrackPresenter {
    void loadTracksByUserLimited(int userId, int limit);
    void loadLikedTracks(int userId);
    void deleteTrackById(int trackId);
}

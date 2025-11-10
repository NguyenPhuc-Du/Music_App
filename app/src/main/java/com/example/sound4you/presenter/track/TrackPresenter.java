package com.example.sound4you.presenter.track;

public interface TrackPresenter {
    void loadTracksByFirebaseLimited(String firebaseUid, int limit);
    void loadTracksByUserLimited(int userId, int limit);
    void loadLikedTracks(String firebaseUid);
    void deleteTrackById(int trackId);
}

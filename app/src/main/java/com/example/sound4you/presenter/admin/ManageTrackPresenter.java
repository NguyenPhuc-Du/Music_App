package com.example.sound4you.presenter.admin;

public interface ManageTrackPresenter {
    void loadTracks();
    void approveTrack(int id);
    void deleteTrack(int id);
}

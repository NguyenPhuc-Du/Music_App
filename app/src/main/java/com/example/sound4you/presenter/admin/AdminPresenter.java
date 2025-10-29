package com.example.sound4you.presenter.admin;



public interface AdminPresenter {
    void loadUsers();
    void deleteUser(int id);
    void loadTracks();
    void deleteTrack(int id);
    void loadStats();
}


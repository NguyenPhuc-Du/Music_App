package com.example.sound4you.presenter.profile;

import com.example.sound4you.data.model.User;

public interface ProfilePresenter {
    void loadProfileByFirebase(String firebaseUid);
    void loadProfileById(int id);
    void updateProfileByFirebase(String firebaseUid, User user);
}

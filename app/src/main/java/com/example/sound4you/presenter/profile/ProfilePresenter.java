package com.example.sound4you.presenter.profile;

import com.example.sound4you.data.model.User;

public interface ProfilePresenter {
    void loadProfile(int userId);
    void updateProfile(int userId, User user);
}

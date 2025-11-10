package com.example.sound4you.ui.profile;

import com.example.sound4you.data.model.User;

public interface ProfileView {
    void showLoading();
    void hideLoading();
    void onProfileLoaded(User user);
    void onProfileUpdated(User user);
    void onError(String msg);
}

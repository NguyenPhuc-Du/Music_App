package com.example.sound4you.ui.follow;

import com.example.sound4you.data.model.User;

import java.util.List;

public interface FollowView {
    void onFollowersLoaded(List<User> followers);
    void onFollowingLoaded(List<User> following);
    void onError(String msg);
}

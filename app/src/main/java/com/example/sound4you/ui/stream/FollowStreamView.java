package com.example.sound4you.ui.stream;

public interface FollowStreamView {
    void onFollowChanged(boolean following);
    void onFollowStatusChecked(boolean isFollowing);
    void onFollowCountLoaded(int followers, int following);
    void onError(String msg);
}

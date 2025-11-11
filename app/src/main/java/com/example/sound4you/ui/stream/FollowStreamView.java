package com.example.sound4you.ui.stream;

public interface FollowStreamView {
    void onFollowChanged(boolean following);
    void onFollowStatusChecked(boolean followed);
    void onError(String msg);
    void onFollowCountLoaded(int followers, int following);
}

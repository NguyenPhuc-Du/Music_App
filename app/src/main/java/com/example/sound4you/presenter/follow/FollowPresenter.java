package com.example.sound4you.presenter.follow;

public interface FollowPresenter {
    void followUser(int userId, int followingId, boolean isFollowing);
    void checkFollowed(int userId, int otherUserId);
    void loadFollowers(int userId);
    void loadFollowing(int userId);
    void countFollowers(int userId);
    void countFollowing(int userId);
}

package com.example.sound4you.presenter.follow;

public interface FollowPresenter {
    void followUser(String firebaseUid, int followingId, boolean isFollowing);
    void checkFollowed(String firebaseUid, int userId);
    void loadFollowersByFirebase(String firebaseUid);
    void loadFollowingByFirebase(String firebaseUid);
}

package com.example.sound4you.presenter.like;

import com.example.sound4you.data.model.Track;

import java.util.List;

public interface LikePresenter {
    void likeTrack(String firebaseUid, int track_id, boolean isLiked);
    void checkLiked(String firebaseUid, int track_id);
    void checkLikes(String firebaseUid, List<Track> tracks);
}

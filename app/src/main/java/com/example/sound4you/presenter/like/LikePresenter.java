package com.example.sound4you.presenter.like;

import com.example.sound4you.data.model.Track;

import java.util.List;

public interface LikePresenter {
    void likeTrack(int userId, int trackId, boolean isLiked);
    void checkLiked(int userId, int trackId);
    void checkLikes(int userId, List<Track> tracks);
}


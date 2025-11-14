package com.example.sound4you.data.repository;

import com.example.sound4you.data.dao.LikeDao;
import com.example.sound4you.data.model.CheckLikesRequest;
import com.example.sound4you.utils.ApiClient;
import com.example.sound4you.data.model.Track;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class LikeRepository {
    private final LikeDao likeDao;

    public LikeRepository () {
        likeDao = ApiClient.getClient().create(LikeDao.class);
    }

    public void likeTrack(int userId, int trackId, Callback<Map<String, Object>> callback) {
        Map<String, Object> data = new HashMap<>();
        data.put("user_id", userId);
        data.put("track_id", trackId);
        likeDao.likeTrack(data).enqueue(callback);
    }

    public void checkLiked(int userId, int trackId, Callback<Map<String, Boolean>> callback) {
        Map<String, Object> data = new HashMap<>();
        data.put("user_id", userId);
        data.put("track_id", trackId);
        likeDao.checkLiked(data).enqueue(callback);
    }

    public void checkLikes(int userId, List<Integer> trackIds, Callback<List<Track>> callback) {
        CheckLikesRequest request = new CheckLikesRequest(userId, trackIds);
        likeDao.checkLikes(request).enqueue(callback);
    }
}

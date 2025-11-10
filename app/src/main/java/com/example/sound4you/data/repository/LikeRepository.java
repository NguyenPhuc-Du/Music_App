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

    public void likeTrack(Map<String, Object> data, Callback<Map<String, Object>> callback) {
        likeDao.likeTrack(data).enqueue(callback);
    }

    public void checkLiked(String firebaseUid, int trackId, Callback<Map<String, Boolean>> callback) {
        Map<String, Object> data = new HashMap<>();
        data.put("firebaseUid", firebaseUid);
        data.put("trackId", trackId);

        likeDao.checkLiked(data).enqueue(callback);
    }

    public void checkLikes(String firebaseUid, List<Integer> trackIds, Callback<List<Track>> callback) {
        CheckLikesRequest request = new CheckLikesRequest(firebaseUid, trackIds);
        Call<List<Track>> call = likeDao.checkLikes(request);
        call.enqueue(callback);
    }
}

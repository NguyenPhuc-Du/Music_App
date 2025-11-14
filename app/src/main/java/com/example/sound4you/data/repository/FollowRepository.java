package com.example.sound4you.data.repository;

import com.example.sound4you.data.dao.FollowDao;
import com.example.sound4you.data.model.User;
import com.example.sound4you.utils.ApiClient;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import retrofit2.Call;
import retrofit2.Callback;

public class FollowRepository {

    private final FollowDao followDao;

    public FollowRepository() {
        followDao = ApiClient.getClient().create(FollowDao.class);
    }

    public void followUser(int userId, int followingId, Callback<Map<String, Object>> callback) {
        Map<String, Object> body = new HashMap<>();
        body.put("user_id", userId);
        body.put("following_id", followingId);
        followDao.followUser(body).enqueue(callback);
    }

    public void checkFollowed(int userId, int otherId, Callback<Map<String, Boolean>> callback) {
        Map<String, Object> body = new HashMap<>();
        body.put("user_id", userId);
        body.put("other_id", otherId);
        followDao.checkFollowed(body).enqueue(callback);
    }

    public void getFollowers(int userId, Callback<List<User>> callback) {
        Map<String, Object> body = new HashMap<>();
        body.put("user_id", userId);
        followDao.getFollowersByUserId(body).enqueue(callback);
    }

    public void getFollowing(int userId, Callback<List<User>> callback) {
        Map<String, Object> body = new HashMap<>();
        body.put("user_id", userId);
        followDao.getFollowingByUserId(body).enqueue(callback);
    }

    public void countFollowers(int userId, Callback<Map<String, Integer>> callback) {
        Map<String, Object> body = new HashMap<>();
        body.put("user_id", userId);
        followDao.countFollowers(body).enqueue(callback);
    }

    public void countFollowing(int userId, Callback<Map<String, Integer>> callback) {
        Map<String, Object> body = new HashMap<>();
        body.put("user_id", userId);
        followDao.countFollowing(body).enqueue(callback);
    }
}

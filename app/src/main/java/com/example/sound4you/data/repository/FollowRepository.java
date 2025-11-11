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

    public void followUser(Map<String, Object> data, Callback<Map<String, Object>> callback) {
        followDao.followUser(data).enqueue(callback);
    }

    public void checkFollowed(String firebaseUid, int userId, Callback<Map<String, Boolean>> callback) {
        Map<String, Object> data = new HashMap<>();
        data.put("firebaseUid", firebaseUid);
        data.put("userId", userId);
        followDao.checkFollowed(data).enqueue(callback);
    }

    public void getFollowersByFirebase(String firebaseUid, Callback<List<User>> callback) {
        Map<String, Object> data = new HashMap<>();
        data.put("firebaseUid", firebaseUid);
        followDao.getFollowersByFirebase(data).enqueue(callback);
    }

    public void getFollowingByFirebase(String firebaseUid, Callback<List<User>> callback) {
        Map<String, Object> data = new HashMap<>();
        data.put("firebaseUid", firebaseUid);
        followDao.getFollowingByFirebase(data).enqueue(callback);
    }

    public void countFollowers(String firebaseUid, Callback<Map<String, Integer>> callback) {
        Map<String, Object> body = new HashMap<>();
        body.put("firebaseUid", firebaseUid);
        followDao.countFollowers(body).enqueue(callback);
    }

    public void countFollowing(String firebaseUid, Callback<Map<String, Integer>> callback) {
        Map<String, Object> body = new HashMap<>();
        body.put("firebaseUid", firebaseUid);
        followDao.countFollowing(body).enqueue(callback);
    }
}

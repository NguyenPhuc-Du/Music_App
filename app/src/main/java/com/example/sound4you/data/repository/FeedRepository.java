package com.example.sound4you.data.repository;

import com.example.sound4you.utils.ApiClient;
import com.example.sound4you.data.dao.FeedDao;
import com.example.sound4you.data.model.Track;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;

public class FeedRepository {
    private final FeedDao feedDao;

    public FeedRepository() {
        feedDao = ApiClient.getClient().create(FeedDao.class);
    }
    public void getFeed(Callback<List<Track>> callback) {
        Call<List<Track>> call = feedDao.getFeed();
        call.enqueue(callback);
    }

    public void getFeedFollowing(String firebaseId,Callback<List<Track>> callback) {
        Call<List<Track>> call = feedDao.getFeedFollowing(firebaseId);
        call.enqueue(callback);
    }
}

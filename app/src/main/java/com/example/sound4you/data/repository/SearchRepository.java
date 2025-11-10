package com.example.sound4you.data.repository;

import com.example.sound4you.data.model.Track;
import com.example.sound4you.data.model.Genre;
import com.example.sound4you.data.model.User;
import com.example.sound4you.data.dao.SearchDao;
import com.example.sound4you.utils.ApiClient;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;

import java.util.Map;
import java.util.HashMap;

public class SearchRepository {
    private final SearchDao searchDao;

    public SearchRepository() {
        searchDao = ApiClient.getClient().create(SearchDao.class);
    }

    public void searchTracks(String query, Callback<List<Track>> callback) {
        Call<List<Track>> call = searchDao.searchTracks(query);
        call.enqueue(callback);
    }

    public void searchUsers(String query, Callback<List<User>> callback) {
        Call<List<User>> call = searchDao.searchUsers(query);
        call.enqueue(callback);
    }

    public void searchGenres(String query, String genre, Callback<List<Track>> callback) {
        Map<String, String> data = new HashMap<>();
        data.put("query", query);
        data.put("genre", genre);

        Call<List<Track>> call = searchDao.searchGenres(data);
        call.enqueue(callback);
    }
}

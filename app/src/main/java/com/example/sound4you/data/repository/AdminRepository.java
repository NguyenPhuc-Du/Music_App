package com.example.sound4you.data.repository;

import com.example.sound4you.data.dao.AdminDao;
import com.example.sound4you.data.model.Track;
import com.example.sound4you.data.model.User;
import com.example.sound4you.utils.ApiClient;

import java.util.List;
import java.util.Map;
import retrofit2.Call;

public class AdminRepository {
    private final AdminDao api;

    public AdminRepository() {
        api = ApiClient.getClient().create(AdminDao.class);
    }

    public Call<List<User>> getAllUsers() {
        return api.getAllUsers();
    }

    public Call<Map<String, String>> deleteUser(int id) {
        return api.deleteUser(id);
    }

    public Call<List<Track>> getAllTracks() {
        return api.getAllTracks();
    }

    public Call<Map<String, String>> deleteTrack(int id) {
        return api.deleteTrack(id);
    }

    public Call<Map<String, Integer>> getStatistics() {
        return api.getStatistics();
    }
}

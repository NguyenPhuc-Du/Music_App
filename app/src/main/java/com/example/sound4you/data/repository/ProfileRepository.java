package com.example.sound4you.data.repository;

import com.example.sound4you.data.dao.ProfileDao;
import com.example.sound4you.data.model.User;
import com.example.sound4you.utils.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;

public class ProfileRepository {
    private final ProfileDao profileDao;

    public ProfileRepository() {
        profileDao = ApiClient.getClient().create(ProfileDao.class);
    }

    public void getUser(int id, Callback<User> callback) {
        profileDao.getUserById(id).enqueue(callback);
    }

    public void updateUser(int id, User user, Callback<User> callback) {
        profileDao.updateUser(id, user).enqueue(callback);
    }
}

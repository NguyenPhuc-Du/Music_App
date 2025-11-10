package com.example.sound4you.data.repository;

import com.example.sound4you.data.dao.ProfileDao;
import com.example.sound4you.data.model.User;
import com.example.sound4you.utils.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;

public class ProfileRepository {
    private final ProfileDao profileDao;

    public ProfileRepository () {
        profileDao = ApiClient.getClient().create(ProfileDao.class);
    }

    public void getUserFirebase(String firebaseUid, Callback<User> callback) {
        Call<User> call = profileDao.getUserByFirebase(firebaseUid);
        call.enqueue(callback);
    }

    public void getUserId(int userId, Callback<User> callback) {
        Call<User> call = profileDao.getUserId(userId);
        call.enqueue(callback);
    }

    public void updateUser(String firebaseUid, User user, Callback<User> callback) {
        Call<User> call = profileDao.updateUser(firebaseUid, user);
        call.enqueue(callback);
    }
}

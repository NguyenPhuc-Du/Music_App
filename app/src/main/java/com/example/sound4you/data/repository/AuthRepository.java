package com.example.sound4you.data.repository;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.sound4you.data.model.User;
import com.example.sound4you.data.dao.UserDao;
import com.example.sound4you.utils.ApiClient;

import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;

public class AuthRepository {
    private UserDao userDao;
    private SharedPreferences sharedPreferences;

    public AuthRepository(Context context) {
        userDao = ApiClient.getClient().create(UserDao.class);
    }

    public void register(User user, Callback<Map<String, String>> callback) {
        userDao.register(user).enqueue(callback);
    }

    public void login(User user, Callback<Map<String, Object>> callback) {
        userDao.login(user).enqueue(callback);
    }

    public void resetPassword(Map<String, String> data, Callback<Map<String, String>> callback) {
        userDao.resetPassword(data).enqueue(callback);
    }

}

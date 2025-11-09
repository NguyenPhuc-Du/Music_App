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
        sharedPreferences = context.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
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

    public void saveToken(String token) {
        sharedPreferences.edit().putString("token", token).apply();
    }

    public String getToken() {
        return sharedPreferences.getString("token", null);
    }

    public void clearToken() {
        sharedPreferences.edit().remove("Token").apply();
    }

    public void saveUserRole(int role) {
        sharedPreferences.edit().putInt("UserRole", role).apply();
    }

    public int getUserRole() {
        return sharedPreferences.getInt("UserRole", -1);
    }

}

package com.example.sound4you.data.dao;

import com.example.sound4you.data.model.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Body;
import retrofit2.http.Path;

public interface ProfileDao {

    @GET("api/users/{id}")
    Call<User> getUserById(@Path("id") int id);

    @PUT("api/users/{id}")
    Call<User> updateUser(@Path("id") int id, @Body User user);
}

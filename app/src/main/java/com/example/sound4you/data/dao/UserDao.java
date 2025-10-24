package com.example.sound4you.data.dao;

import com.example.sound4you.data.model.User;
import java.util.Map;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserDao {
    @POST("api/auth/register")
    Call<Map<String, String>> register(@Body User user);

    @POST("api/auth/login")
    Call<Map<String, Object>> login(@Body User user);

    @POST("api/auth/resetPassword")
    Call<Map<String, String>> resetPassword(@Body Map<String, String> data);
}

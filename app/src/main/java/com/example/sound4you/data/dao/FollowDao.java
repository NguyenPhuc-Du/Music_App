package com.example.sound4you.data.dao;

import java.util.Map;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.*;

import com.example.sound4you.data.model.User;

public interface FollowDao {

    @POST("api/follow/")
    Call<Map<String, Object>> followUser(@Body Map<String, Object> data);

    @POST("api/follow/isFollowed")
    Call<Map<String, Boolean>> checkFollowed(@Body Map<String, Object> body);

    @POST("api/follow/following")
    Call<List<User>> getFollowingByFirebase(@Body Map<String, Object> body);

    @POST("api/follow/followers")
    Call<List<User>> getFollowersByFirebase(@Body Map<String, Object> body);
}

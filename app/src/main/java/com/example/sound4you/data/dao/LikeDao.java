package com.example.sound4you.data.dao;

import java.util.Map;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.*;

import com.example.sound4you.data.model.CheckLikesRequest;
import com.example.sound4you.data.model.Track;

public interface LikeDao {
    @POST("api/like/")
    Call<Map<String, Object>> likeTrack(@Body Map<String, Object> data);

    @POST("api/like/isLiked")
    Call<Map<String, Boolean>> checkLiked(@Body Map<String, Object> body);

    @POST("api/like/liked")
    Call<List<Track>> checkLikes(@Body CheckLikesRequest request);
}

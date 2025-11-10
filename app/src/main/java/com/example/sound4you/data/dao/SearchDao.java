package com.example.sound4you.data.dao;

import com.example.sound4you.data.model.Track;
import com.example.sound4you.data.model.User;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Body;

import java.util.Map;

public interface SearchDao {
    @GET("api/search/tracks")
    Call<List<Track>> searchTracks(@Query("query") String query);

    @GET("api/search/users")
    Call<List<User>> searchUsers(@Query("query") String query);

    @POST("api/search/genres")
    Call<List<Track>> searchGenres(@Body Map<String, String> data);

}

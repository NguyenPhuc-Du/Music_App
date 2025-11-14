package com.example.sound4you.data.dao;

import com.example.sound4you.data.model.Track;
import com.example.sound4you.data.model.HomeResponse;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

public interface TrackDao {

    @GET("api/tracks/home")
    Call<HomeResponse> getHomeTracks();

    @GET("api/tracks/uploads/user/{id}/{limit}")
    Call<List<Track>> getTrackByUserLimited(
            @Path("id") int id,
            @Path("limit") int limit
    );

    @GET("api/tracks/likes/{userId}")
    Call<List<Track>> getLikedTrack(
            @Path("userId") int userId
    );

    @Multipart
    @POST("api/tracks/upload")
    Call<ResponseBody> uploadTrack(
            @Part("user_id") RequestBody userId,
            @Part("title") RequestBody title,
            @Part("genre") RequestBody genre,
            @Part("duration") RequestBody duration,
            @Part MultipartBody.Part music,
            @Part MultipartBody.Part cover
    );

    @DELETE("api/tracks/{id}")
    Call<Void> deleteTrack(
            @Path("id") int id
    );
}

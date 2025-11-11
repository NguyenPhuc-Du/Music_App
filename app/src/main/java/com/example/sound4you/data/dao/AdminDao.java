package com.example.sound4you.data.dao;

import com.example.sound4you.data.model.Track;
import com.example.sound4you.data.model.User;
import com.example.sound4you.data.model.StatisticsResponse;
import com.example.sound4you.data.model.PendingCountResponse;
import com.example.sound4you.data.model.ApprovedCountResponse;
import com.example.sound4you.data.model.UserCountResponse;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

public interface AdminDao {

    @GET("api/admin/users")
    Call<List<User>> getAllUsers();

    @DELETE("api/admin/users/{id}")
    Call<ResponseBody> deleteUser(@Path("id") int id);

    @GET("api/admin/tracks")
    Call<List<Track>> getAllTracks();

    @DELETE("api/admin/tracks/{id}")
    Call<ResponseBody> deleteTrack(@Path("id") int id);

    @GET("api/admin/tracks-with-user")
    Call<List<Track>> getTracksWithUser();

    @PUT("api/admin/tracks/approve/{id}")
    Call<ResponseBody> approveTrack(@Path("id") int id);

    @GET("api/admin/tracks/pending/numberPendingMusic")
    Call<PendingCountResponse> getPendingMusicCount();

    @GET("api/admin/tracks/approved/numberApprovedMusic")
    Call<ApprovedCountResponse> getApprovedMusicCount();

    @GET("api/admin/users/numberUserCount")
    Call<UserCountResponse> getUserCount();

    @GET("api/admin/genre/getGenreLikePercentage")
    Call<List<StatisticsResponse>> getGenreLikePercentage();
}

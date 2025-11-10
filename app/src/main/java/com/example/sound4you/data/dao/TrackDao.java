package com.example.sound4you.data.dao;

import com.example.sound4you.data.model.Track;
import com.example.sound4you.data.model.HomeResponse;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.POST;
import retrofit2.http.Body;
import retrofit2.http.Query;

public interface TrackDao {

    @GET("api/tracks/home")
    Call<HomeResponse> getHomeTracks();

    @GET("api/tracks/uploads/firebase/{firebaseUid}/{limit}")
    Call<List<Track>> getTrackByFirebaseLimited (@Path("firebaseUid") String firebaseUid, @Path("limit") int limit);

    @GET("api/tracks/uploads/user/{id}/{limit}")
    Call<List<Track>> getTrackByUserLimited (@Path("id") int id, @Path("limit") int limit);

    @GET("api/tracks/likes/{firebaseUid}")
    Call<List<Track>> getLikedTrack (@Path("firebaseUid") String firebaseUid);

    @Multipart
    @POST("api/tracks/upload")
    Call<String> uploadTrack(
            @Part("firebaseUid") RequestBody firebaseUid,
            @Part("title") RequestBody title,
            @Part("genre") RequestBody genre,
            @Part MultipartBody.Part music,
            @Part MultipartBody.Part cover
    );

    @DELETE("api/tracks/{id}")
    Call<Void> deleteTrack(@Path("id") int id);
}


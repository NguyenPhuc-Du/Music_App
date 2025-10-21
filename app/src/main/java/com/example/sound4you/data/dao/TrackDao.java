package com.example.sound4you.data.dao;

import com.example.sound4you.data.model.Track;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.POST;
import retrofit2.http.Body;

public interface TrackDao {

    @GET("Track")
    Call<List<Track>> getAllTracks();

    @GET("Track/{id}")
    Call<Track> getTrackByID(@Path("id") int id);

    @POST("Track")
    Call<Track> createTrack(@Body Track track);
}

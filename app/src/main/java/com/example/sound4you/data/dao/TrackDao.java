package com.example.sound4you.data.dao;

import com.example.sound4you.data.model.Track;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.POST;
import retrofit2.http.Body;

public interface TrackDao {

    @GET("api/Track")
    Call<List<Track>> getAllTracks();

    @GET("api/Track/{id}")
    Call<Track> getTrackByID(@Path("id") int id);

    @POST("api/Track")
    Call<Track> createTrack(@Body Track track);
}

package com.example.sound4you.data.dao;

import com.example.sound4you.data.model.Track;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface FeedDao {
    @GET("api/feed/")
    Call<List<Track>> getFeed();

    @GET("api/feed/following/{firebase_id}")
    Call<List<Track>> getFeedFollowing(@Path("firebase_id") String firebaseUid);
}

package com.example.sound4you.data.repository;

import com.example.sound4you.data.dao.TrackDao;
import com.example.sound4you.data.model.Track;
import com.example.sound4you.utils.ApiClient;
import com.example.sound4you.data.model.HomeResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class TrackRepository {
    private final TrackDao trackDao;

    public TrackRepository() {
        trackDao = ApiClient.getClient().create(TrackDao.class);
    }

    public void getHomeTracks(Callback<HomeResponse> callBack) {
        Call<HomeResponse> call = trackDao.getHomeTracks();
        call.enqueue(callBack);
    }

    public void getTrackByFirebaseLimited(String firebaseUid, int limit, Callback<List<Track>> callback) {
        Call<List<Track>> call = trackDao.getTrackByFirebaseLimited(firebaseUid, limit);
        call.enqueue(callback);
    }

    public void getTrackByUserLimited(int id, int limit, Callback<List<Track>> callback) {
        Call<List<Track>> call = trackDao.getTrackByUserLimited(id, limit);
        call.enqueue(callback);
    }

    public void getLikedTrack(String firebaseUid, Callback<List<Track>> callback) {
        Call<List<Track>> call = trackDao.getLikedTrack(firebaseUid);
        call.enqueue(callback);
    }

    public void uploadTrack(RequestBody uid, RequestBody title, RequestBody genre,
                            MultipartBody.Part music, MultipartBody.Part cover,
                            Callback<String> callback) {
        trackDao.uploadTrack(uid, title, genre, music, cover).enqueue(callback);
    }

    public void deleteTrack(int id, Callback<Void> callback) {
        Call<Void> call = trackDao.deleteTrack(id);
        call.enqueue(callback);
    }
}

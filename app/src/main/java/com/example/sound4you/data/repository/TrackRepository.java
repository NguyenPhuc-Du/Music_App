package com.example.sound4you.data.repository;

import com.example.sound4you.data.dao.TrackDao;
import com.example.sound4you.data.model.Track;
import com.example.sound4you.data.model.HomeResponse;
import com.example.sound4you.utils.ApiClient;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class TrackRepository {

    private final TrackDao trackDao;

    public TrackRepository() {
        trackDao = ApiClient.getClient().create(TrackDao.class);
    }

    public void getHomeTracks(Callback<HomeResponse> callBack) {
        trackDao.getHomeTracks().enqueue(callBack);
    }

    public void getTrackByUserLimited(int id, int limit, Callback<List<Track>> callback) {
        trackDao.getTrackByUserLimited(id, limit).enqueue(callback);
    }

    public void getLikedTrack(int userId, Callback<List<Track>> callback) {
        trackDao.getLikedTrack(userId).enqueue(callback);
    }

    public void uploadTrack(
            RequestBody userId,
            RequestBody title,
            RequestBody genre,
            RequestBody duration,
            MultipartBody.Part music,
            MultipartBody.Part cover,
            Callback<ResponseBody> callback
    ) {
        trackDao.uploadTrack(userId, title, genre, duration, music, cover).enqueue(callback);
    }

    public void deleteTrack(int id, Callback<Void> callback) {
        trackDao.deleteTrack(id).enqueue(callback);
    }
}

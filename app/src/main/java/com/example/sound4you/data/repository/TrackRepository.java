package com.example.sound4you.data.repository;

import com.example.sound4you.data.dao.TrackDao;
import com.example.sound4you.data.model.Track;
import com.example.sound4you.utils.ApiClient;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;

public class TrackRepository {
    private final TrackDao trackDao;

    public TrackRepository() {
        trackDao = ApiClient.getClient().create(TrackDao.class);
    }

    public void getAllTracks(Callback<List<Track>> callBack) {
        Call<List<Track>> call = trackDao.getAllTracks();
        call.enqueue(callBack);
    }

    public void getTrackByID(int id, Callback<Track> callBack) {
        Call<Track> call = trackDao.getTrackByID(id);
        call.enqueue(callBack);
    }

    public void createTrack(Track track, Callback<Track> callBack) {
        Call<Track> call = trackDao.createTrack(track);
        call.enqueue(callBack);
    }
}

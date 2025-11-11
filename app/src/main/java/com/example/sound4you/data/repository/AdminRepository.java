package com.example.sound4you.data.repository;

import com.example.sound4you.data.dao.AdminDao;
import com.example.sound4you.data.model.Track;
import com.example.sound4you.data.model.User;
import com.example.sound4you.data.model.StatisticsResponse;
import com.example.sound4you.data.model.PendingCountResponse;
import com.example.sound4you.data.model.ApprovedCountResponse;
import com.example.sound4you.data.model.UserCountResponse;
import com.example.sound4you.utils.ApiClient;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class AdminRepository {

    private final AdminDao dao;

    public AdminRepository() {
        dao = ApiClient.getClient().create(AdminDao.class);
    }

    // Users
    public Call<List<User>> getUsers() { return dao.getAllUsers(); }
    public Call<ResponseBody> deleteUser(int id) { return dao.deleteUser(id); }

    // Tracks
    public Call<List<Track>> getTracks() { return dao.getAllTracks(); }
    public Call<List<Track>> getTracksWithUser() { return dao.getTracksWithUser(); }
    public Call<ResponseBody> deleteTrack(int id) { return dao.deleteTrack(id); }
    public Call<ResponseBody> approveTrack(int id) { return dao.approveTrack(id); }

    // Dashboard counts
    public Call<PendingCountResponse> getPendingCount() { return dao.getPendingMusicCount(); }
    public Call<ApprovedCountResponse> getApprovedCount() { return dao.getApprovedMusicCount(); }
    public Call<UserCountResponse> getUserCount() { return dao.getUserCount(); }

    // Genre statistics
    public Call<List<StatisticsResponse>> getGenreStats() { return dao.getGenreLikePercentage(); }
}

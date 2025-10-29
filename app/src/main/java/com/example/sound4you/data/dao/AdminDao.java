package com.example.sound4you.data.dao;

import com.example.sound4you.data.model.User;
import com.example.sound4you.data.model.Track;
import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.http.*;

public interface AdminDao {

    //Lấy danh sách người dùng
    @GET("/api/admin/users")
    Call<List<User>> loadUsers();

    //Xóa người dùng theo ID
    @DELETE("/api/admin/users/{id}")
    Call<Map<String, String>> deleteUser(@Path("id") int id);

    //Lấy danh sách bài nhạc
    @GET("/api/admin/tracks")
    Call<List<Track>> loadTracks();

    //Xóa bài nhạc theo ID
    @DELETE("/api/admin/tracks/{id}")
    Call<Map<String, String>> deleteTrack(@Path("id") int id);

    //Lấy thống kê hệ thống
    @GET("/api/admin/stats")
    Call<Map<String, Integer>> loadStats();
}

// Tên file: ApiService.java
package com.example.sound4you.utils; // Hoặc package bạn muốn

import com.example.sound4you.data.model.Track;
import com.example.sound4you.data.model.User;
import com.example.sound4you.utils.LoginRequest;
import com.example.sound4you.utils.LoginResponse;
import com.example.sound4you.utils.UserResponse; // Dùng cho ví dụ GET

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    /**
     * API Đăng nhập
     * Gửi đi một đối tượng LoginRequest
     * Nhận về một đối tượng LoginResponse
     */
    @POST("api/admin/login") // Đường dẫn đầy đủ = BASE_URL + "api/admin/login"
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

    /**
     * API Lấy danh sách người dùng (Ví dụ từ trước)
     */
    @GET("api/admin/users") // Giả sử route trong adminRoutes.js là "/users"
    Call<List<User>> getUsers(); // <-- Sửa UserResponse thành List<User>

    // === THÊM DÒNG NÀY VÀO ===
    // (Giả sử đường dẫn là /api/admin/tracks)
    @GET("api/admin/tracks")
    Call<List<Track>> getTracks();

    // === THÊM PHƯƠNG THỨC NỚI NÀY VÀO ===
    // API lấy MỘT bài hát theo ID
    @GET("api/admin/tracks-with-user")
    Call<List<Track>> getTracksWithUser();

    // ⭐ THÊM HÀM NÀY
    @PUT("api/admin/tracks/approve/{id}")
    Call<ResponseBody> approveTrack(@Path("id") int trackId);

    // ⭐ THÊM HÀM NÀY
    @DELETE("api/admin/tracks/{id}")
    Call<ResponseBody> deleteTrack(@Path("id") int trackId);

    @DELETE("api/admin/users/{id}")
    Call<ResponseBody> deleteUser(@Path("id") int userId);

    @GET("api/admin/tracks/pending/numberPendingMusic") // <-- Đảm bảo đường dẫn này khớp với router Node.js
    Call<PendingCountResponse> getPendingMusicCount();

    @GET("api/admin/tracks/approved/numberApprovedMusic") // <-- Đảm bảo đường dẫn này khớp với router Node.js
    Call<ApprovedCountResponse> getApprovedMusicCount();

    @GET("api/admin/users/numberUserCount") // <-- Đảm bảo đường dẫn này khớp với router Node.js
    Call<UserCountResponse> getNumberUserCount();

    @GET("api/admin/genre/getGenreLikePercentage")
    Call<List<StatisticsResponse>> getGenreLikePercentage();
}
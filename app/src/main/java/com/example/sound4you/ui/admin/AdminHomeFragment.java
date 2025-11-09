package com.example.sound4you.ui.admin;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sound4you.R;
import com.example.sound4you.utils.ApiClient;
import com.example.sound4you.utils.ApiService;
import com.example.sound4you.utils.ApprovedCountResponse;
import com.example.sound4you.utils.PendingCountResponse;
import com.example.sound4you.utils.UserCountResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminHomeFragment extends Fragment {

    private static final String TAG = "AdminHomeFragment";

    // 1. Khai báo
    private TextView tvPendingCount, tvApprovedCount, tvUserCount;
    private ApiService apiService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 2. Sử dụng layout riêng cho fragment này
        return inflater.inflate(R.layout.fragment_admin_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 3. Ánh xạ TextView
        // (ID này sẽ được tạo ở bước 2 bên dưới)
        tvPendingCount = view.findViewById(R.id.number_of_pending);

        tvApprovedCount = view.findViewById(R.id.number_of_approved);

        tvUserCount = view.findViewById(R.id.number_of_users);

        // 4. Khởi tạo ApiService
        apiService = ApiClient.getService();

        // 5. Gọi hàm tải số lượng
        fetchPendingCount();

        fetchApprovedCount();

        fetchUserCount();
    }

    /**
     * Hàm gọi API để lấy số lượng nhạc chờ duyệt
     */
    private void fetchPendingCount() {
        Log.d(TAG, "Đang tải số lượng pending...");
        tvPendingCount.setText("Đang tải..."); // Hiển thị trạng thái

        // 1. Tạo cuộc gọi
        // (Hãy chắc chắn bạn đã thêm 'getPendingMusicCount' vào ApiService)
        Call<PendingCountResponse> call = apiService.getPendingMusicCount();

        // 2. Thực thi bất đồng bộ
        call.enqueue(new Callback<PendingCountResponse>() {
            @Override
            public void onResponse(Call<PendingCountResponse> call, Response<PendingCountResponse> response) {
                // Đảm bảo fragment vẫn còn "attached" (chưa bị hủy)
                if (!isAdded() || getContext() == null) {
                    return;
                }

                if (response.isSuccessful() && response.body() != null) {
                    // 3. Lấy số lượng từ response
                    int count = response.body().getPending_count();

                    // 4. Cập nhật lên TextView
                    tvPendingCount.setText(String.valueOf(count));
                    Log.d(TAG, "Số lượng bài hát chờ duyệt: " + count);

                } else {
                    Log.e(TAG, "Tải số lượng thất bại. Code: " + response.code());
                    tvPendingCount.setText("Lỗi");
                    Toast.makeText(getContext(), "Không thể tải số lượng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PendingCountResponse> call, Throwable t) {
                if (!isAdded() || getContext() == null) {
                    return;
                }
                Log.e(TAG, "Lỗi kết nối (fetchPendingCount): " + t.getMessage());
                tvPendingCount.setText("Lỗi mạng");
                Toast.makeText(getContext(), "Lỗi kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchApprovedCount() {
        Log.d(TAG, "Đang tải số lượng approved...");
        tvApprovedCount.setText("Đang tải..."); // Hiển thị trạng thái

        // 1. Tạo cuộc gọi
        // (Hãy chắc chắn bạn đã thêm 'getPendingMusicCount' vào ApiService)
        Call<ApprovedCountResponse> call = apiService.getApprovedMusicCount();

        // 2. Thực thi bất đồng bộ
        call.enqueue(new Callback<ApprovedCountResponse>() {
            @Override
            public void onResponse(Call<ApprovedCountResponse> call, Response<ApprovedCountResponse> response) {
                // Đảm bảo fragment vẫn còn "attached" (chưa bị hủy)
                if (!isAdded() || getContext() == null) {
                    return;
                }

                if (response.isSuccessful() && response.body() != null) {
                    // 3. Lấy số lượng từ response
                    int count = response.body().getApproved_count();

                    // 4. Cập nhật lên TextView
                    tvApprovedCount.setText(String.valueOf(count));
                    Log.d(TAG, "Số lượng bài hát đã duyệt: " + count);

                } else {
                    Log.e(TAG, "Tải số lượng thất bại. Code: " + response.code());
                    tvApprovedCount.setText("Lỗi");
                    Toast.makeText(getContext(), "Không thể tải số lượng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApprovedCountResponse> call, Throwable t) {
                if (!isAdded() || getContext() == null) {
                    return;
                }
                Log.e(TAG, "Lỗi kết nối (fetchPendingCount): " + t.getMessage());
                tvPendingCount.setText("Lỗi mạng");
                Toast.makeText(getContext(), "Lỗi kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchUserCount() {
        Log.d(TAG, "Đang tải số lượng user...");
        tvUserCount.setText("Đang tải..."); // Hiển thị trạng thái

        // 1. Tạo cuộc gọi
        // (Hãy chắc chắn bạn đã thêm 'getPendingMusicCount' vào ApiService)
        Call<UserCountResponse> call = apiService.getNumberUserCount();

        // 2. Thực thi bất đồng bộ
        call.enqueue(new Callback<UserCountResponse>() {
            @Override
            public void onResponse(Call<UserCountResponse> call, Response<UserCountResponse> response) {
                // Đảm bảo fragment vẫn còn "attached" (chưa bị hủy)
                if (!isAdded() || getContext() == null) {
                    return;
                }

                if (response.isSuccessful() && response.body() != null) {
                    // 3. Lấy số lượng từ response
                    int count = response.body().getUser_count();

                    // 4. Cập nhật lên TextView
                    tvUserCount.setText(String.valueOf(count));
                    Log.d(TAG, "Số lượng người dùng: " + count);

                } else {
                    Log.e(TAG, "Tải số lượng thất bại. Code: " + response.code());
                    tvUserCount.setText("Lỗi");
                    Toast.makeText(getContext(), "Không thể tải số lượng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserCountResponse> call, Throwable t) {
                if (!isAdded() || getContext() == null) {
                    return;
                }
                Log.e(TAG, "Lỗi kết nối (fetchUserCount): " + t.getMessage());
                tvUserCount.setText("Lỗi mạng");
                Toast.makeText(getContext(), "Lỗi kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
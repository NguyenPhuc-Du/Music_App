package com.example.sound4you.ui.admin;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sound4you.R;
import com.example.sound4you.data.model.User;
import com.example.sound4you.utils.ApiClient;
import com.example.sound4you.utils.ApiService;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageUserFragment extends Fragment implements ItemClickListener {
    private static final String TAG = "ManageUserFragment"; // Thêm TAG để debug
    private RecyclerView recyclerView;
    private List<UserItem> userItemList; // List cho Adapter (UI Model)
    private UserAdapter userAdapter;
    private ApiService apiService; // Thêm ApiService

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. Khởi tạo ApiService MỘT LẦN
        apiService = ApiClient.getService();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_manage_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 2. Thiết lập RecyclerView
        recyclerView = view.findViewById(R.id.manage_list_user);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // 3. Khởi tạo list và adapter RỖNG
        userItemList = new ArrayList<>();
        userAdapter = new UserAdapter(userItemList);
        recyclerView.setAdapter(userAdapter);
        userAdapter.setOnUserActionListener(this);

        // 4. GỌI API để lấy dữ liệu thật
        fetchUsers();
    }

    /**
     * Hàm này gọi API, lấy List<User> và "chuyển đổi" nó
     * thành List<UserItem> cho adapter.
     */
    private void fetchUsers() {
        Log.d(TAG, "Đang tải danh sách người dùng...");

        // 1. SỬA LẠI ĐÂY: Phải là List<User>
        Call<List<User>> call = apiService.getUsers();

        // 2. SỬA LẠI ĐÂY: Phải là List<User>
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                Log.d("API_DELETE", "Response code: " + response.code());

                if (response.isSuccessful() && response.body() != null) {

                    // 3. SỬA LẠI ĐÂY: Không cần myResponse nữa,
                    // response.body() CHÍNH LÀ danh sách
                    List<User> apiUserList = response.body();

                    Log.d(TAG, "Tải thành công " + apiUserList.size() + " users.");

                    // 4. Code bên dưới giữ nguyên (đã đúng)
                    userItemList.clear();

                    for (User apiUser : apiUserList) {
                        int imageResId = R.drawable.ic_logo;

                        UserItem uiItem = new UserItem(
                                apiUser.getId(),
                                apiUser.getUsername(),
                                apiUser.getEmail(),
                                apiUser.getProfile_picture(),
                                apiUser.getRole()
                        );
                        userItemList.add(uiItem);
                    }

                    userAdapter.notifyDataSetChanged();

                } else {
                    Log.e(TAG, "Tải thất bại. Code: " + response.code());
                    if (getContext() != null) {
                        Toast.makeText(getContext(), "Không thể tải danh sách người dùng", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.e(TAG, "Lỗi kết nối: " + t.getMessage());
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Lỗi kết nối mạng", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Override cho vui =)))
    @Override
    public void onApproveClick(int position) {}

    @Override
    public void onDeleteClick(int position) {
        int realPosition = userAdapter.getAdapterPositionFromItem(position);
        if (realPosition == RecyclerView.NO_POSITION) return;

        UserItem clickedItem = userItemList.get(realPosition);
        int userId = clickedItem.getId();

        Call<ResponseBody> call = apiService.deleteUser(userId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    // CẬP NHẬT LIST VÀ ADAPTER BẰNG realPosition
                    userItemList.remove(realPosition);
                    userAdapter.notifyItemRemoved(realPosition);

                    Toast.makeText(getContext(), "Deleted: " + clickedItem.getUserName(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Deleted user ID: " + userId);
                } else {
                    Toast.makeText(getContext(), "Lỗi: Không thể xóa người dùng", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Lỗi delete. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối mạng", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Lỗi onFailure (delete): " + t.getMessage());
            }
        });
    }
}
package com.example.sound4you.presenter.admin;

import com.example.sound4you.data.model.User;
import com.example.sound4you.data.repository.AdminRepository;
import com.example.sound4you.ui.admin.ManageUserView;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageUserPresenterImpl implements ManageUserPresenter {
    private final ManageUserView view;
    private final AdminRepository repo;

    public ManageUserPresenterImpl(ManageUserView view) {
        this.view = view;
        this.repo = new AdminRepository();
    }

    @Override
    public void loadUsers() {
        repo.getUsers().enqueue(new Callback<List<User>>() {
            @Override public void onResponse(Call<List<User>> call, Response<List<User>> res) {
                if (res.isSuccessful() && res.body() != null)
                    view.onUsersLoaded(res.body());
                else view.onError("Không thể tải danh sách người dùng");
            }
            @Override public void onFailure(Call<List<User>> call, Throwable t) {
                view.onError("Lỗi mạng: " + t.getMessage());
            }
        });
    }

    @Override
    public void deleteUser(int id) {
        repo.deleteUser(id).enqueue(new Callback<ResponseBody>() {
            @Override public void onResponse(Call<ResponseBody> c, Response<ResponseBody> r) {
                if (r.isSuccessful()) view.onUserDeleted(id);
                else view.onError("Xóa thất bại");
            }
            @Override public void onFailure(Call<ResponseBody> c, Throwable t) {
                view.onError("Lỗi mạng khi xóa user");
            }
        });
    }
}

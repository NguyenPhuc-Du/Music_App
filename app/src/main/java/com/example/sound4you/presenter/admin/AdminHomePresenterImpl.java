package com.example.sound4you.presenter.admin;

import com.example.sound4you.data.model.UserCountResponse;
import com.example.sound4you.data.model.ApprovedCountResponse;
import com.example.sound4you.data.model.PendingCountResponse;
import com.example.sound4you.data.repository.AdminRepository;
import com.example.sound4you.ui.admin.AdminHomeView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminHomePresenterImpl implements AdminHomePresenter {
    private final AdminHomeView view;
    private final AdminRepository repo;

    public AdminHomePresenterImpl(AdminHomeView view) {
        this.view = view;
        this.repo = new AdminRepository();
    }

    @Override
    public void loadDashboard() {
        repo.getPendingCount().enqueue(new Callback<PendingCountResponse>() {
            @Override public void onResponse(Call<PendingCountResponse> call, Response<PendingCountResponse> res) {
                if (res.isSuccessful() && res.body() != null)
                    view.showPendingCount(res.body().getPending_count());
                else view.onError("Không thể tải số lượng bài chờ duyệt");
            }
            @Override public void onFailure(Call<PendingCountResponse> c, Throwable t) {
                view.onError("Lỗi mạng: " + t.getMessage());
            }
        });

        repo.getApprovedCount().enqueue(new Callback<ApprovedCountResponse>() {
            @Override public void onResponse(Call<ApprovedCountResponse> call, Response<ApprovedCountResponse> res) {
                if (res.isSuccessful() && res.body() != null)
                    view.showApprovedCount(res.body().getApproved_count());
                else view.onError("Không thể tải số lượng bài đã duyệt");
            }
            @Override public void onFailure(Call<ApprovedCountResponse> c, Throwable t) {
                view.onError("Lỗi mạng: " + t.getMessage());
            }
        });

        repo.getUserCount().enqueue(new Callback<UserCountResponse>() {
            @Override public void onResponse(Call<UserCountResponse> call, Response<UserCountResponse> res) {
                if (res.isSuccessful() && res.body() != null)
                    view.showUserCount(res.body().getUser_count());
                else view.onError("Không thể tải số lượng người dùng");
            }
            @Override public void onFailure(Call<UserCountResponse> c, Throwable t) {
                view.onError("Lỗi mạng: " + t.getMessage());
            }
        });
    }
}

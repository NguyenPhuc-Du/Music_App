package com.example.sound4you.presenter.admin;


import com.example.sound4you.data.model.Track;
import com.example.sound4you.data.model.User;
import com.example.sound4you.data.repository.AdminRepository;
import com.example.sound4you.ui.admin.AdminView;

import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminPresenter {
    private final AdminRepository repo;
    private final AdminView view;

    public AdminPresenter(AdminView view) {
        this.repo = new AdminRepository();
        this.view = view;
    }

    public void loadUsers() {
        repo.getAllUsers().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    view.onUsersLoaded(response.body());
                } else {
                    view.onError("Failed to load users");
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                view.onError(t.getMessage());
            }
        });
    }

    public void loadTracks() {
        repo.getAllTracks().enqueue(new Callback<List<Track>>() {
            @Override
            public void onResponse(Call<List<Track>> call, Response<List<Track>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    view.onTracksLoaded(response.body());
                } else {
                    view.onError("Failed to load tracks");
                }
            }

            @Override
            public void onFailure(Call<List<Track>> call, Throwable t) {
                view.onError(t.getMessage());
            }
        });
    }

    public void loadStats() {
        repo.getStatistics().enqueue(new Callback<Map<String, Integer>>() {
            @Override
            public void onResponse(Call<Map<String, Integer>> call, Response<Map<String, Integer>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    view.onStatsLoaded(response.body());
                } else {
                    view.onError("Failed to load stats");
                }
            }

            @Override
            public void onFailure(Call<Map<String, Integer>> call, Throwable t) {
                view.onError(t.getMessage());
            }
        });
    }
}


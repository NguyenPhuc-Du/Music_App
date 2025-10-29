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

public class AdminPresenterImpl implements AdminPresenter {
    private final AdminRepository repo;
    private final AdminView view;

    public AdminPresenterImpl(AdminView view) {
        this.repo = new AdminRepository();
        this.view = view;
    }

    public void loadUsers() {
        repo.loadUsers().enqueue(new Callback<List<User>>() {
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
    @Override
    public void deleteUser(int id) {
        repo.deleteUser(id).enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    view.onMessage(response.body().get("message"));
                } else {
                    view.onError("Failed to delete user");
                }
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                view.onError(t.getMessage());
            }
        });
    }

    public void loadTracks() {
        repo.loadTracks().enqueue(new Callback<List<Track>>() {
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
    @Override
    public void deleteTrack(int id) {
        repo.deleteTrack(id).enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    view.onMessage(response.body().get("message"));
                } else {
                    view.onError("Failed to delete track");
                }
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                view.onError(t.getMessage());
            }
        });
    }
    public void loadStats() {
        repo.loadStats().enqueue(new Callback<Map<String, Integer>>() {
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


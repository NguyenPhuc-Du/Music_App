package com.example.sound4you.presenter.admin;

import com.example.sound4you.data.repository.AdminRepository;
import com.example.sound4you.ui.admin.GenreStatsView;
import com.example.sound4you.data.model.StatisticsResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GenreStatsPresenterImpl implements GenreStatsPresenter {
    private final GenreStatsView view;
    private final AdminRepository repo;

    public GenreStatsPresenterImpl(GenreStatsView view) {
        this.view = view;
        this.repo = new AdminRepository();
    }

    @Override
    public void loadGenreStats() {
        repo.getGenreStats().enqueue(new Callback<List<StatisticsResponse>>() {
            @Override public void onResponse(Call<List<StatisticsResponse>> c, Response<List<StatisticsResponse>> r) {
                if (r.isSuccessful() && r.body() != null)
                    view.onStatsLoaded(r.body());
                else view.onError("Không thể tải thống kê thể loại");
            }

            @Override public void onFailure(Call<List<StatisticsResponse>> c, Throwable t) {
                view.onError("Lỗi mạng: " + t.getMessage());
            }
        });
    }
}

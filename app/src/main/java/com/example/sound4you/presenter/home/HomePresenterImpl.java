package com.example.sound4you.presenter.home;

import com.example.sound4you.data.model.Track;
import com.example.sound4you.data.repository.TrackRepository;
import com.example.sound4you.ui.home.HomeView;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomePresenterImpl implements HomePresenter{
    private final HomeView homeView;
    private final TrackRepository trackRepository;

    public HomePresenterImpl (HomeView homeview) {
        this.homeView = homeview;
        this.trackRepository = new TrackRepository();
    }

    @Override
    public void loadTracks() {
        homeView.showLoading(true);

        trackRepository.getAllTracks(new Callback<List<Track>>() {
            @Override
            public void onResponse(Call<List<Track>> call, Response<List<Track>> response) {
                homeView.showLoading(false);
                if (response.isSuccessful() && response.body() != null) {
                    homeView.showTracks(response.body());
                }
                else {
                    homeView.showError("Lỗi không thể tải danh sách bài");
                }
            }

            @Override
            public void onFailure(Call<List<Track>> call, Throwable t) {
                homeView.showLoading(false);
                homeView.showError("Lỗi mạng");
            }
        });
    }
}

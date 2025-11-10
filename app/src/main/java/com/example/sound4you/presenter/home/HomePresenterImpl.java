package com.example.sound4you.presenter.home;

import com.example.sound4you.data.model.Track;
import com.example.sound4you.data.model.HomeSection;
import com.example.sound4you.data.repository.TrackRepository;
import com.example.sound4you.ui.home.HomeView;
import com.example.sound4you.data.model.HomeResponse;

import java.util.ArrayList;
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
        loadHomeSections();
    }

    public void loadHomeSections() {
        homeView.showLoading(true);

        trackRepository.getHomeTracks(new Callback<HomeResponse>() {
            @Override
            public void onResponse(Call<HomeResponse> call, Response<HomeResponse> response) {
                homeView.showLoading(false);
                if (response.isSuccessful() && response.body() != null) {
                    HomeResponse data = response.body();

                    List<HomeSection> sections = new ArrayList<>();
                    if (data.getYouMayLike() != null && !data.getYouMayLike().isEmpty()) {
                        sections.add(new HomeSection("You May Like", data.getYouMayLike(), 0)); // grid 2 cột
                    }
                    if (data.getNewReleases() != null && !data.getNewReleases().isEmpty()) {
                        sections.add(new HomeSection("New Releases", data.getNewReleases(), 1)); // list ngang
                    }
                    if (data.getTopHits() != null && !data.getTopHits().isEmpty()) {
                        sections.add(new HomeSection("Top Hits", data.getTopHits(), 1)); // list ngang
                    }

                    if (sections.isEmpty()) {
                        homeView.showError("Chưa có dữ liệu");
                    }
                    else {
                        homeView.showSections(sections);
                    }
                } else {
                    homeView.showError("Lỗi không thể tải danh sách home");
                }
            }

            @Override
            public void onFailure(Call<HomeResponse> call, Throwable t) {
                homeView.showLoading(false);
                homeView.showError("Lỗi mạng");
            }
        });
    }
}

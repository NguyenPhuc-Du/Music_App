package com.example.sound4you.presenter.feed;

import com.example.sound4you.data.model.Track;
import com.example.sound4you.ui.feed.FeedView;
import com.example.sound4you.data.repository.FeedRepository;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedFollowingPresenterImpl implements FeedFollowingPresenter{
    private FeedView view;
    private FeedRepository feedRepository;

    public FeedFollowingPresenterImpl(FeedView view) {
        this.view = view;
        feedRepository = new FeedRepository();
    }

    @Override
    public void loadFollowingFeed(String firebaseId) {
        view.showLoading();

        feedRepository.getFeedFollowing(firebaseId, new Callback<List<Track>>() {
            @Override
            public void onResponse(Call<List<Track>> call, Response<List<Track>> response) {
                view.hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    view.showFeed(response.body());
                }
                else {
                    view.showError("Không tải được feed theo dõi");
                }
            }

            @Override
            public void onFailure(Call<List<Track>> call, Throwable t) {
                view.hideLoading();
                view.showError("Lỗi mạng: " + t.getMessage());
            }
        });
    }
}

package com.example.sound4you.presenter.profile;

import com.example.sound4you.data.model.User;
import com.example.sound4you.data.repository.ProfileRepository;
import com.example.sound4you.presenter.follow.FollowPresenterImpl;
import com.example.sound4you.ui.profile.ProfileView;
import com.example.sound4you.ui.stream.FollowStreamView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfilePresenterImpl implements ProfilePresenter {

    private final ProfileRepository repo;
    private final ProfileView view;
    private final FollowPresenterImpl followPresenter;

    public ProfilePresenterImpl(ProfileView view) {
        this.repo = new ProfileRepository();
        this.view = view;


        this.followPresenter = new FollowPresenterImpl((FollowStreamView) view);
    }

    @Override
    public void loadProfile(int userId) {
        view.showLoading();

        repo.getUser(userId, new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                view.hideLoading();

                if (response.isSuccessful() && response.body() != null) {

                    User user = response.body();
                    view.onProfileLoaded(user);
                    followPresenter.countFollowers(userId);
                    followPresenter.countFollowing(userId);

                } else view.onError("Không lấy được profile");
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                view.hideLoading();
                view.onError("Lỗi mạng: " + t.getMessage());
            }
        });
    }

    @Override
    public void updateProfile(int userId, User user) {
        view.showLoading();

        repo.updateUser(userId, user, new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                view.hideLoading();

                if (response.isSuccessful())
                    view.onProfileUpdated(response.body());
                else
                    view.onError("Cập nhật thất bại");
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                view.hideLoading();
                view.onError("Lỗi mạng: " + t.getMessage());
            }
        });
    }
}

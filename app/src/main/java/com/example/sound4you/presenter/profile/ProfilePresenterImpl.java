package com.example.sound4you.presenter.profile;

import com.example.sound4you.data.model.User;
import com.example.sound4you.data.repository.ProfileRepository;
import com.example.sound4you.ui.profile.ProfileView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfilePresenterImpl implements ProfilePresenter{
    private final ProfileRepository profileRepository;
    private final ProfileView view;

    public ProfilePresenterImpl(ProfileView view) {
        this.profileRepository = new ProfileRepository();
        this.view = view;
    }

    @Override
    public void loadProfileByFirebase(String firebaseUid) {
        view.showLoading();
        profileRepository.getUserFirebase(firebaseUid, new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                view.hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    view.onProfileLoaded(response.body());
                }
                else {
                    view.onError("Không thể tải thông tin người dùng");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                view.hideLoading();
                view.onError("Lỗi mạng: " + t.getMessage());
            }
        });
    }

    @Override
    public void loadProfileById(int userId) {
        view.showLoading();
        profileRepository.getUserId(userId, new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                view.hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    view.onProfileLoaded(response.body());
                }
                else {
                    view.onError("Không thể tải hồ sơ người dùng khác");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                view.hideLoading();
                view.onError("Lỗi mạng: " + t.getMessage());
            }
        });
    }

    @Override
    public void updateProfileByFirebase(String firebaseUid, User user) {
        view.showLoading();
        profileRepository.updateUser(firebaseUid, user, new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                view.hideLoading();
                if(response.isSuccessful() && response.body() != null) {
                    view.onProfileUpdated(response.body());
                }
                else {
                    view.onError("Cập nhật thất bại");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                view.hideLoading();
                view.onError("Lỗi mạng: " + t.getMessage());
            }
        });
    }
}

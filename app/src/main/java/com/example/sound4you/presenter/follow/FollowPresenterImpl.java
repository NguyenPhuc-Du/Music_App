package com.example.sound4you.presenter.follow;

import com.example.sound4you.data.model.User;
import com.example.sound4you.data.repository.FollowRepository;
import com.example.sound4you.ui.follow.FollowView;
import com.example.sound4you.ui.stream.FollowStreamView;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowPresenterImpl implements FollowPresenter {

    private FollowStreamView streamView;
    private FollowView view;
    private final FollowRepository followRepository;

    // Constructor dành cho PROFILE (FollowStreamView)
    public FollowPresenterImpl(FollowStreamView streamView) {
        this.streamView = streamView;
        this.followRepository = new FollowRepository();
    }

    // Constructor dành cho FOLLOW LIST VIEW
    public FollowPresenterImpl(FollowView view) {
        this.view = view;
        this.followRepository = new FollowRepository();
    }

    @Override
    public void followUser(int userId, int followingId, boolean isFollowing) {
        followRepository.followUser(userId, followingId, new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {

                if (response.isSuccessful() && streamView != null) {

                    boolean following = Boolean.TRUE.equals(response.body().get("following"));

                    streamView.onFollowChanged(following);

                    countFollowers(followingId);

                    countFollowing(userId);
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                if (streamView != null)
                    streamView.onError("Lỗi mạng: " + t.getMessage());
            }
        });
    }

    @Override
    public void checkFollowed(int userId, int otherUserId) {
        followRepository.checkFollowed(userId, otherUserId, new Callback<Map<String, Boolean>>() {
            @Override
            public void onResponse(Call<Map<String, Boolean>> call, Response<Map<String, Boolean>> response) {
                if (response.isSuccessful() && streamView != null)
                    streamView.onFollowStatusChecked(response.body().get("following"));
            }

            @Override
            public void onFailure(Call<Map<String, Boolean>> call, Throwable t) {
                if (streamView != null)
                    streamView.onError("Lỗi mạng: " + t.getMessage());
            }
        });
    }

    @Override
    public void loadFollowers(int userId) {
        followRepository.getFollowers(userId, new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (view != null && response.isSuccessful())
                    view.onFollowersLoaded(response.body());
                else if (view != null)
                    view.onError("Không thể tải followers");
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                if (view != null)
                    view.onError("Lỗi mạng: " + t.getMessage());
            }
        });
    }

    @Override
    public void loadFollowing(int userId) {
        followRepository.getFollowing(userId, new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (view != null && response.isSuccessful())
                    view.onFollowingLoaded(response.body());
                else if (view != null)
                    view.onError("Không thể tải following");
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                if (view != null)
                    view.onError("Lỗi mạng: " + t.getMessage());
            }
        });
    }

    @Override
    public void countFollowers(int userId) {
        followRepository.countFollowers(userId, new Callback<Map<String, Integer>>() {
            @Override
            public void onResponse(Call<Map<String, Integer>> call, Response<Map<String, Integer>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    int followers = response.body().get("followers");

                    if (streamView != null)
                        streamView.onFollowCountLoaded(followers, -1);

                    if (view != null)
                        view.onFollowCountLoaded(followers, -1);
                }
            }

            @Override
            public void onFailure(Call<Map<String, Integer>> call, Throwable t) {
                if (view != null)
                    view.onError("Lỗi tải followers");
            }
        });
    }

    @Override
    public void countFollowing(int userId) {
        followRepository.countFollowing(userId, new Callback<Map<String, Integer>>() {
            @Override
            public void onResponse(Call<Map<String, Integer>> call, Response<Map<String, Integer>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    int following = response.body().get("following");

                    if (streamView != null)
                        streamView.onFollowCountLoaded(-1, following);

                    if (view != null)
                        view.onFollowCountLoaded(-1, following);
                }
            }

            @Override
            public void onFailure(Call<Map<String, Integer>> call, Throwable t) {
                if (view != null)
                    view.onError("Lỗi tải following");
            }
        });
    }
}

package com.example.sound4you.presenter.follow;

import android.util.Log;

import com.example.sound4you.data.model.User;
import com.example.sound4you.ui.stream.FollowStreamView;
import com.example.sound4you.ui.follow.FollowView;
import com.example.sound4you.data.repository.FollowRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowPresenterImpl implements FollowPresenter {
    private FollowStreamView streamView;
    private FollowView view;
    private final FollowRepository followRepository;

    public FollowPresenterImpl(FollowStreamView streamView) {
        this.streamView = streamView;
        this.followRepository = new FollowRepository();
    }

    public FollowPresenterImpl (FollowView view) {
        this.view = view;
        this.followRepository = new FollowRepository();
    }

    @Override
    public void followUser(String firebaseUid, int followingId, boolean isFollowing) {
        Map<String, Object> data = new HashMap<>();
        data.put("firebaseUid", firebaseUid);
        data.put("following_id", followingId);

        followRepository.followUser(data, new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Object followingObj = response.body().get("following");
                    boolean following = followingObj != null && Boolean.parseBoolean(followingObj.toString());
                    streamView.onFollowChanged(following);
                } else {
                    streamView.onError("Không nhận được phản hồi từ máy chủ");
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                Log.e("FollowPresenter", "Lỗi mạng: " + t.getMessage());
                streamView.onError("Lỗi mạng: " + t.getMessage());
            }
        });
    }

    @Override
    public void checkFollowed(String firebaseUid, int user_id) {
        followRepository.checkFollowed(firebaseUid, user_id, new Callback<Map<String, Boolean>>() {
            @Override
            public void onResponse(Call<Map<String, Boolean>> call, Response<Map<String, Boolean>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    boolean following = response.body().get("following");
                    streamView.onFollowChanged(following);
                }
                else {
                    streamView.onError("Không nhận được phản hồi từ máy chủ");
                }
            }

            @Override
            public void onFailure(Call<Map<String, Boolean>> call, Throwable t) {
                Log.e("FollowPresenter", "Lỗi mạng: " + t.getMessage());
                streamView.onError("Lỗi mạng: " + t.getMessage());
            }
        });
    }

    @Override
    public void loadFollowersByFirebase(String firebaseUid) {
        followRepository.getFollowersByFirebase(firebaseUid, new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    view.onFollowersLoaded(response.body());
                } else {
                    view.onError("Không thể tải danh sách followers");
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                view.onError("Lỗi mạng: " + t.getMessage());
            }
        });
    }

    @Override
    public void loadFollowingByFirebase(String firebaseUid) {
        followRepository.getFollowingByFirebase(firebaseUid, new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    view.onFollowingLoaded(response.body());
                } else {
                    view.onError("Không thể tải danh sách following");
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                view.onError("Lỗi mạng: " + t.getMessage());
            }
        });
    }
}

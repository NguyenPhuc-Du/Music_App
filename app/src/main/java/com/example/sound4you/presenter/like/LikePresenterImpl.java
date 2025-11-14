package com.example.sound4you.presenter.like;

import android.util.Log;

import com.example.sound4you.data.model.Track;
import com.example.sound4you.ui.stream.LikeStreamView;
import com.example.sound4you.data.repository.LikeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LikePresenterImpl implements LikePresenter {
    private final LikeRepository likeRepository;
    private final LikeStreamView view;

    public LikePresenterImpl(LikeStreamView view) {
        this.likeRepository = new LikeRepository();
        this.view = view;
    }

    @Override
    public void likeTrack(int userId, int trackId, boolean isLiked) {
        likeRepository.likeTrack(userId, trackId, new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Boolean liked = (Boolean) response.body().get("liked");
                    view.onLikeChanged(liked != null && liked);
                } else {
                    view.onError("Không nhận được phản hồi từ máy chủ");
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                view.onError("Lỗi mạng: " + t.getMessage());
            }
        });
    }

    @Override
    public void checkLiked(int userId, int trackId) {
        likeRepository.checkLiked(userId, trackId, new Callback<Map<String, Boolean>>() {
            @Override
            public void onResponse(Call<Map<String, Boolean>> call, Response<Map<String, Boolean>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Boolean liked = response.body().get("liked");
                    view.onLikeStatusChecked(liked != null && liked);
                } else {
                    view.onError("Không thể kiểm tra trạng thái like");
                }
            }

            @Override
            public void onFailure(Call<Map<String, Boolean>> call, Throwable t) {
                view.onError("Lỗi mạng: " + t.getMessage());
            }
        });
    }

    @Override
    public void checkLikes(int userId, List<Track> tracks) {
        if (tracks == null || tracks.isEmpty()) { return; }

        List<Integer> trackIds = new ArrayList<>();
        for (Track track : tracks) trackIds.add(track.getId());

        likeRepository.checkLikes(userId, trackIds, new Callback<List<Track>>() {
            @Override
            public void onResponse(Call<List<Track>> call, Response<List<Track>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Track> updatedTracks = response.body();
                    for (Track track : tracks) {
                        for (Track t : updatedTracks) {
                            if (track.getId() == t.getId()) {
                                track.setLiked(t.isLiked());
                            }
                        }
                    }
                    view.onTracksUpdated(tracks);
                } else {
                    view.onError("Không thể kiểm tra liked");
                }
            }

            @Override
            public void onFailure(Call<List<Track>> call, Throwable t) {
                view.onError("Lỗi mạng: " + t.getMessage());
            }
        });
    }
}



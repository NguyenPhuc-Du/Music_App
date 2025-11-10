package com.example.sound4you.presenter.like;

import android.util.Log;

import com.example.sound4you.data.model.Track;
import com.example.sound4you.ui.stream.LikeStreamView;
import com.example.sound4you.data.repository.LikeRepository;

import java.util.ArrayList;
import java.util.HashMap;
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
    public void likeTrack(String firebaseUid, int trackId, boolean isLiked) {
        Map<String, Object> data = new HashMap<>();
        data.put("firebaseUid", firebaseUid);
        data.put("track_id", trackId);

        likeRepository.likeTrack(data, new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Boolean liked = (Boolean) response.body().get("liked");
                    if (liked != null) {
                        view.onLikeChanged(liked);
                    } else {
                        view.onError("Invalid response format");
                    }
                } else {
                    view.onError("Không nhận được phản hồi từ máy chủ");
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                Log.e("LikePresenter", "Lỗi mạng: " + t.getMessage());
                view.onError("Lỗi mạng: " + t.getMessage());
            }
        });
    }

    @Override
    public void checkLiked(String firebaseUid, int trackId) {
        likeRepository.checkLiked(firebaseUid, trackId, new Callback<Map<String, Boolean>>() {
            @Override
            public void onResponse(Call<Map<String, Boolean>> call, Response<Map<String, Boolean>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Boolean liked = response.body().get("liked");
                    if (liked != null) {
                        view.onLikeStatusChecked(liked);
                    } else {
                        view.onError("Invalid response format");
                    }
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
    public void checkLikes(String firebaseUid, List<Track> tracks) {
        if (tracks == null || tracks.isEmpty()) {
            view.onError("No tracks to check");
            return;
        }

        List<Integer> trackIds = new ArrayList<>();
        for (Track track : tracks) {
            trackIds.add(track.getId());
        }

        likeRepository.checkLikes(firebaseUid, trackIds, new Callback<List<Track>>() {
            @Override
            public void onResponse(Call<List<Track>> call, Response<List<Track>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Track> updatedTracks = response.body();
                    for (Track track : tracks) {
                        for (Track updatedTrack : updatedTracks) {
                            if (track.getId() == updatedTrack.getId()) {
                                track.setLiked(updatedTrack.isLiked());
                                break;
                            }
                        }
                    }

                    view.onTracksUpdated(tracks);
                } else {
                    view.onError("Không thể kiểm tra trạng thái like");
                }
            }

            @Override
            public void onFailure(Call<List<Track>> call, Throwable t) {
                view.onError("Lỗi mạng: " + t.getMessage());
            }
        });
    }
}


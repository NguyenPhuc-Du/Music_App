package com.example.sound4you.presenter.track;

import com.example.sound4you.data.repository.TrackRepository;
import com.example.sound4you.ui.track.TrackView;
import com.example.sound4you.data.model.Track;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrackPresenterImpl implements TrackPresenter{
    private final TrackRepository trackRepository;
    private final TrackView view;

    public TrackPresenterImpl(TrackView view) {
        this.trackRepository = new TrackRepository();
        this.view = view;
    }

    @Override
    public void loadTracksByFirebaseLimited(String firebaseUid, int limit) {
        view.showLoading();
        trackRepository.getTrackByFirebaseLimited(firebaseUid, limit, new Callback<List<Track>>() {
            @Override
            public void onResponse(Call<List<Track>> call, Response<List<Track>> response) {
                view.hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    view.onTracksLoaded(response.body());
                }
                else {
                    view.onError("Không thể tải tracks");
                }
            }

            @Override
            public void onFailure(Call<List<Track>> call, Throwable t) {
                view.hideLoading();
                view.onError("Lỗi mạng: " + t.getMessage());
            }
        });
    }

    @Override
    public void loadTracksByUserLimited(int userId, int limit) {
        view.showLoading();
        trackRepository.getTrackByUserLimited(userId, limit, new Callback<List<Track>>() {
            @Override
            public void onResponse(Call<List<Track>> call, Response<List<Track>> response) {
                view.hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    view.onTracksLoaded(response.body());
                }
                else {
                    view.onError("Không thể tải tracks");
                }
            }

            @Override
            public void onFailure(Call<List<Track>> call, Throwable t) {
                view.hideLoading();
                view.onError("Lỗi mạng: " + t.getMessage());
            }
        });
    }

    @Override
    public void loadLikedTracks(String firebaseUid) {
        view.showLoading();
        trackRepository.getLikedTrack(firebaseUid, new Callback<List<Track>>() {
            @Override
            public void onResponse(Call<List<Track>> call, Response<List<Track>> response) {
                view.hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    view.onTracksLoaded(response.body());
                }
                else {
                    view.onError("Không thể tải liked tracks");
                }
            }

            @Override
            public void onFailure(Call<List<Track>> call, Throwable t) {
                view.hideLoading();
                view.onError("Lỗi mạng: " + t.getMessage());
            }
        });
    }

    @Override
    public void deleteTrackById(int trackId) {
        trackRepository.deleteTrack(trackId, new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    view.onError("Đã xóa bài hát");
                }
                else {
                    view.onError("Xóa thất bại");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                view.onError("Lỗi mạng: " + t.getMessage());
            }
        });
    }
}

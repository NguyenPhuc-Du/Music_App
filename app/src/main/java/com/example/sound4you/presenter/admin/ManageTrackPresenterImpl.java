package com.example.sound4you.presenter.admin;

import com.example.sound4you.data.model.Track;
import com.example.sound4you.data.repository.AdminRepository;
import com.example.sound4you.ui.admin.ManageTrackView;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageTrackPresenterImpl implements ManageTrackPresenter {
    private final ManageTrackView view;
    private final AdminRepository repo;

    public ManageTrackPresenterImpl(ManageTrackView view) {
        this.view = view;
        this.repo = new AdminRepository();
    }

    @Override
    public void loadTracks() {
        repo.getTracksWithUser().enqueue(new Callback<List<Track>>() {
            @Override public void onResponse(Call<List<Track>> call, Response<List<Track>> res) {
                if (res.isSuccessful() && res.body() != null)
                    view.onTracksLoaded(res.body());
                else view.onError("Không thể tải danh sách bài hát");
            }
            @Override public void onFailure(Call<List<Track>> call, Throwable t) {
                view.onError("Lỗi mạng: " + t.getMessage());
            }
        });
    }

    @Override
    public void approveTrack(int id) {
        repo.approveTrack(id).enqueue(new Callback<ResponseBody>() {
            @Override public void onResponse(Call<ResponseBody> c, Response<ResponseBody> r) {
                if (r.isSuccessful()) view.onTrackApproved(id);
                else view.onError("Duyệt thất bại");
            }
            @Override public void onFailure(Call<ResponseBody> c, Throwable t) {
                view.onError("Lỗi mạng khi duyệt bài hát");
            }
        });
    }

    @Override
    public void deleteTrack(int id) {
        repo.deleteTrack(id).enqueue(new Callback<ResponseBody>() {
            @Override public void onResponse(Call<ResponseBody> c, Response<ResponseBody> r) {
                if (r.isSuccessful()) view.onTrackDeleted(id);
                else view.onError("Xóa thất bại");
            }
            @Override public void onFailure(Call<ResponseBody> c, Throwable t) {
                view.onError("Lỗi mạng khi xóa bài hát");
            }
        });
    }
}

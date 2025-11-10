package com.example.sound4you.presenter.search;

import com.example.sound4you.data.repository.SearchRepository;
import com.example.sound4you.data.model.User;
import com.example.sound4you.data.model.Genre;
import com.example.sound4you.data.model.Track;
import com.example.sound4you.ui.search.SearchView;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchPresenterImpl implements SearchPresenter{
    private final SearchRepository searchRepository;
    private final SearchView view;

    public SearchPresenterImpl (SearchView view) {
        this.searchRepository = new SearchRepository();
        this.view = view;
    }

    @Override
    public void searchTracks(String query) {
        view.showLoading();
        searchRepository.searchTracks(query, new Callback<List<Track>>() {
            @Override
            public void onResponse(Call<List<Track>> call, Response<List<Track>> response) {
                view.hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    view.showTracks(response.body());
                }
                else {
                     view.showError("Không tìm thấy bài hát nào.");
                }
            }

            @Override
            public void onFailure(Call<List<Track>> call, Throwable t) {
                view.hideLoading();
                view.showError("Lỗi mạng: " + t.getMessage());
            }
        });
    }

    @Override
    public void searchUsers(String query) {
        view.showLoading();
        searchRepository.searchUsers(query, new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                view.hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    view.showUsers(response.body());
                }
                else {
                    view.showError("Không tìm thấy người dùng nào.");
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                view.hideLoading();
                view.showError("Lỗi mạng: " + t.getMessage());
            }
        });
    }

    @Override
    public void searchGenres(String query, String genreId) {
        view.showLoading();
        searchRepository.searchGenres(query, genreId, new Callback<List<Track>>() {
            @Override
            public void onResponse(Call<List<Track>> call, Response<List<Track>> response) {
                view.hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    view.showTracks(response.body());
                }
                else {
                    view.showError("Không có bài hát thuộc thể loại này.");
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

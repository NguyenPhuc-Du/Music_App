package com.example.sound4you.presenter.genre;

import com.example.sound4you.data.repository.GenreRepository;
import com.example.sound4you.data.model.Genre;
import com.example.sound4you.ui.genre.GenreView;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GenrePresenterImpl implements GenrePresenter{
    private final GenreRepository genreRepository;
    private final GenreView view;

    public GenrePresenterImpl(GenreView view) {
        this.genreRepository = new GenreRepository();
        this.view = view;
    }

    @Override
    public void loadGenres() {
        genreRepository.getAllGenre(new Callback<List<Genre>>() {
            @Override
            public void onResponse(Call<List<Genre>> call, Response<List<Genre>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    view.onGenresLoaded(response.body());
                }
                else {
                    view.onGenreError("Không lấy được danh sách thể loại");
                }
            }

            @Override
            public void onFailure(Call<List<Genre>> call, Throwable t) {
                view.onGenreError("Lỗi mạng: " + t.getMessage());
            }
        });
    }
}

package com.example.sound4you.data.repository;

import com.example.sound4you.data.model.Genre;
import com.example.sound4you.data.dao.GenreDao;
import com.example.sound4you.utils.ApiClient;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;

public class GenreRepository {
    private final GenreDao genreDao;

    public GenreRepository() {
        genreDao = ApiClient.getClient().create(GenreDao.class);
    }

   public void getAllGenre(Callback<List<Genre>> callback) {
        Call<List<Genre>> call = genreDao.getAllGenres();
        call.enqueue(callback);
   }
}

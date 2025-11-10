package com.example.sound4you.data.dao;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

import com.example.sound4you.data.model.Genre;

public interface GenreDao {
    @GET("api/genre/")
    Call<List<Genre>> getAllGenres();
}

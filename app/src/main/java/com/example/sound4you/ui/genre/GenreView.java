package com.example.sound4you.ui.genre;

import com.example.sound4you.data.model.Genre;
import java.util.List;

public interface GenreView {
    void onGenresLoaded(List<Genre> genres);
    void onGenreError(String error);
}

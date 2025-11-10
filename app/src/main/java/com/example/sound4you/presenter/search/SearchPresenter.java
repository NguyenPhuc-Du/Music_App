package com.example.sound4you.presenter.search;

public interface SearchPresenter {
    void searchTracks(String query);
    void searchUsers(String query);
    void searchGenres(String query, String genreId);
}

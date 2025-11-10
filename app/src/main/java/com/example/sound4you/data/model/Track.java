package com.example.sound4you.data.model;

public class Track {
    private int id;
    private int userID;
    private int genreID;
    private String artist;
    private String title;
    private String audioURL;
    private String coverURL;
    private int view;
    private int delete;
    private String createdAt;
    private int duration;

    public Track(int id, int userID, String artist, int genreID, String title, String audioURL, String coverURL, int view, int delete, String createdAt, int duration) {
        this.id = id;
        this.userID = userID;
        this.genreID = genreID;
        this.artist = artist;
        this.title = title;
        this.audioURL = audioURL;
        this.coverURL = coverURL;
        this.view = view;
        this.delete = delete;
        this.createdAt = createdAt;
        this.duration = duration;
    }

    public int getID() {
        return id;
    }

    public int getUserID() {
        return userID;
    }

    public int getGenreID() {
        return genreID;
    }

    public String getArtist() {
        return artist;
    }

    public String getTitle() {
        return title;
    }

    public String getAudioURL() {
        return audioURL;
    }

    public String getCoverURL() {
        return coverURL;
    }

    public int getView() {
        return view;
    }

    public int getDelete() {
        return delete;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public int getDuration() {
        return duration;
    }

    public void setID(int id) {
        this.id = id;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setGenreID(int genreID) {
        this.genreID = genreID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAudioURL(String audioURL) {
        this.audioURL = audioURL;
    }

    public void setCoverURL(String coverURL) {
        this.coverURL = coverURL;
    }

    public void setView(int view) {
        this.view = view;
    }

    public void setDelete(int delete) {
        this.delete = delete;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public int getUser_id() {
        return userID;
    }

    public String getAudio_url() {
        return audioURL;
    }
}

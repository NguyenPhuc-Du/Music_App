package com.example.sound4you.ui.admin;

import java.io.Serializable;

public class TrackItem implements Serializable {
    private int id;
    private int user_id;
    private int is_verified;
    private String artist;
    private String artistProfilePicture;
    private String title;
    private String audio_url;
    private String cover_url;

    public TrackItem(int id, int user_id, String artist, String artistProfilePicture, String title, String audio_url, String cover_url, int is_verified) {
        this.id = id;
        this.user_id = user_id;
        this.artist = artist;
        this.artistProfilePicture = artistProfilePicture;
        this.title = title;
        this.audio_url = audio_url;
        this.cover_url = cover_url;
        this.is_verified = is_verified;
    }

    public int getId() {
        return id;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getArtist() {
        return artist;
    }

    public String getTitle() {
        return title;
    }

    public String getCover_url() {
        return cover_url;
    }

    public String getAudio_url() {
        return audio_url;
    }

    public String getArtistProfilePicture() {
        return artistProfilePicture;
    }

    public void setIs_verified(int is_verified) {
        this.is_verified = is_verified;
    }

    public int getIs_verified() {
        return is_verified;
    }
}

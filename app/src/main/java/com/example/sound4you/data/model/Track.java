package com.example.sound4you.data.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Track implements Serializable {
    private int id;

    @SerializedName("user_id")
    private int userId;

    @SerializedName("genre_id")
    private int genreId;

    private String artist;

    @SerializedName("artistProfilePicture")
    private String artistProfilePicture;

    @SerializedName("is_verified")
    private int isVerified;

    private boolean liked;
    private String title;

    @SerializedName("audio_url")
    private String audioUrl;

    @SerializedName("cover_url")
    private String coverUrl;

    @SerializedName("is_deleted")
    private int isDeleted;

    @SerializedName("created_at")
    private String createdAt;

    private int duration;

    // --- Getter & Setter ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getGenreId() { return genreId; }
    public void setGenreId(int genreId) { this.genreId = genreId; }

    public String getArtist() { return artist; }
    public void setArtist(String artist) { this.artist = artist; }

    public String getArtistProfilePicture() { return artistProfilePicture; }
    public void setArtistProfilePicture(String artistProfilePicture) { this.artistProfilePicture = artistProfilePicture; }

    public int getIsVerified() { return isVerified; }
    public void setIsVerified(int isVerified) { this.isVerified = isVerified; }

    public boolean isLiked() { return liked; }
    public void setLiked(boolean liked) { this.liked = liked; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAudioUrl() { return audioUrl; }
    public void setAudioUrl(String audioUrl) { this.audioUrl = audioUrl; }

    public String getCoverUrl() { return coverUrl; }
    public void setCoverUrl(String coverUrl) { this.coverUrl = coverUrl; }

    public int getIsDeleted() { return isDeleted; }
    public void setIsDeleted(int isDeleted) { this.isDeleted = isDeleted; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }
}

package com.example.sound4you.data.model;

public class User {
    private int id;
    private String username;
    private String email;
    private String password;
    private int role;
    private String bio;
    private String profilePictureURL;
    private String createdAt;

    public User(int id, String username, String email, String password, int role, String bio, String profilePictureURL, String createdAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.bio = bio;
        this.profilePictureURL = profilePictureURL;
        this.createdAt = createdAt;
    }

    public int getID() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public int getRole() {
        return role;
    }

    public String getBio() {
        return bio;
    }

    public String getProfilePictureURL() {
        return profilePictureURL;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}

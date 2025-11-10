package com.example.sound4you.data.model;

public class User {
    private int id;
    private String username;
    private String email;
    private String password;
    private String firebaseUid;
    private String profile_picture;
    private String bio;
    private int followers;
    private int following;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public int getId() {
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

    public String getFirebaseUid() {
        return firebaseUid;
    }

    public String getBio() {
        return bio;
    }

    public int getFollowing() {
        return following;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFirebaseUid(String firebaseUid) {
        this.firebaseUid = firebaseUid;
    }

    public void setBio(String newBio) {
        this.bio = newBio;
    }

    public void setUsername(String newName) {
        this.username = newName;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }
}

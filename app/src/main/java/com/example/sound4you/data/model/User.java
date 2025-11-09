package com.example.sound4you.data.model;

import com.google.gson.annotations.SerializedName;

public class User {
    private String username;

    @SerializedName("email")
    private String email;
    private String password;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public int getRole() {
        return role;
    }

    // Getters và Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

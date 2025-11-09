// Tên file: UserResponse.java
package com.example.sound4you.utils;

import com.example.sound4you.data.model.User;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserResponse {

    @SerializedName("status")
    private String status;

    @SerializedName("users")
    private List<User> users;

    // Getters và Setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}

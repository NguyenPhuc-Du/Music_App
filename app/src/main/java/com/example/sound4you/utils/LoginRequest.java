package com.example.sound4you.utils;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {

    // Tên biến phải khớp với tên JSON mà server Node.js mong đợi
    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    // Constructor để tạo đối tượng dễ dàng
    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getters và Setters (Gson có thể cần)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
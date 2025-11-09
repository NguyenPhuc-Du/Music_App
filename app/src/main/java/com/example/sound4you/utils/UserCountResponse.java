package com.example.sound4you.utils;

import com.google.gson.annotations.SerializedName;

public class UserCountResponse {
    @SerializedName("user_count")
    private int user_count;

    public int getUser_count() {
        return user_count;
    }
}

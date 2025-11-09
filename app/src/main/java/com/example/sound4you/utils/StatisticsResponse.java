package com.example.sound4you.utils;

import com.google.gson.annotations.SerializedName;

public class StatisticsResponse {
    @SerializedName("title")
    private String title;

    @SerializedName("percentage")
    private float percentage; // Dùng float cho biểu đồ

    // Getters
    public String getTitle() {
        return title;
    }

    public float getPercentage() {
        return percentage;
    }
}

package com.example.sound4you.utils;

import com.google.gson.annotations.SerializedName;

public class ApprovedCountResponse {

    @SerializedName("approved_count")
    private int approved_count;

    public int getApproved_count() {
        return approved_count;
    }
}

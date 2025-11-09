// File: PendingCountResponse.java
package com.example.sound4you.utils; // (Hoặc bất cứ đâu bạn để model)

import com.google.gson.annotations.SerializedName;

public class PendingCountResponse {

    @SerializedName("pending_count")
    private int pending_count;

    public int getPending_count() {
        return pending_count;
    }
}
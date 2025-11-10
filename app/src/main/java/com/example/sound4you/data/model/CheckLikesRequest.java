package com.example.sound4you.data.model;

import java.util.List;

public class CheckLikesRequest {
    private String firebaseUid;
    private List<Integer> trackIds;

    // Constructor
    public CheckLikesRequest(String firebaseUid, List<Integer> trackIds) {
        this.firebaseUid = firebaseUid;
        this.trackIds = trackIds;
    }

    // Getters and setters
    public String getFirebaseUid() {
        return firebaseUid;
    }

    public void setFirebaseUid(String firebaseUid) {
        this.firebaseUid = firebaseUid;
    }

    public List<Integer> getTrackIds() {
        return trackIds;
    }

    public void setTrackIds(List<Integer> trackIds) {
        this.trackIds = trackIds;
    }
}


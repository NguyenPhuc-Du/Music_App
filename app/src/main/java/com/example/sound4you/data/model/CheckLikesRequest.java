package com.example.sound4you.data.model;

import java.util.List;

public class CheckLikesRequest {
    private int user_id;
    private List<Integer> trackIds;

    public CheckLikesRequest(int user_id, List<Integer> trackIds) {
        this.user_id = user_id;
        this.trackIds = trackIds;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public List<Integer> getTrackIds() {
        return trackIds;
    }

    public void setTrackIds(List<Integer> trackIds) {
        this.trackIds = trackIds;
    }
}
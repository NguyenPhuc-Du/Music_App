package com.example.sound4you.data.model;
import java.util.List;

public class HomeResponse {
    private List<Track> youMayLike;
    private List<Track> newReleases;
    private List<Track> topHits;

    public List<Track> getYouMayLike() {
        return youMayLike;
    }
    public List<Track> getNewReleases() {
        return newReleases;
    }
    public List<Track> getTopHits() {
        return topHits;
    }
}


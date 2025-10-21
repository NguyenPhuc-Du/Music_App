package com.example.sound4you.data.model;

import com.example.sound4you.data.model.Track;
import java.util.List;

public class HomeSection {
    private String title;
    private List<Track> tracks;
    private int layoutType;

    public HomeSection(String title, List<Track> tracks, int layoutType) {
        this.title = title;
        this.tracks = tracks;
        this.layoutType = layoutType;
    }

    public String getTitle() {
        return title;
    }
    public List<Track> getTracks() {
        return tracks;
    }
    public int getLayoutType() {
        return layoutType;
    }
}

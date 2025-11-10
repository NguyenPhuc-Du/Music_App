package com.example.sound4you.data.model;

public class Genre {
    int id;
    String title;

    public Genre (int id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }
}

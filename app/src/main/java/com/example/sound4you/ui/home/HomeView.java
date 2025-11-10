package com.example.sound4you.ui.home;

import com.example.sound4you.data.model.Track;
import com.example.sound4you.data.model.HomeSection;
import java.util.List;

public interface HomeView {
    void showLoading(boolean isLoading);
    void showSections(List<HomeSection> sections);
    void showError(String message);
}

// ui/admin/interface/GenreStatsView.java
package com.example.sound4you.ui.admin;

import com.example.sound4you.data.model.StatisticsResponse;

import java.util.List;

public interface GenreStatsView {
    void onStatsLoaded(List<StatisticsResponse> stats);
    void onError(String msg);
}

package com.example.sound4you.ui.admin;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sound4you.R;
import com.example.sound4you.data.model.StatisticsResponse;
import com.example.sound4you.presenter.admin.GenreStatsPresenter;
import com.example.sound4you.presenter.admin.GenreStatsPresenterImpl;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

public class FragmentStats extends Fragment implements GenreStatsView {

    private PieChart pieChart;
    private GenreStatsPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stats, container, false);

        pieChart = view.findViewById(R.id.genre_pie_chart);
        presenter = new GenreStatsPresenterImpl(this);
        presenter.loadGenreStats();

        return view;
    }

    @Override
    public void onStatsLoaded(List<StatisticsResponse> stats) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        for (StatisticsResponse s : stats) {
            entries.add(new PieEntry((float) s.getPercentage(), s.getTitle()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Thể loại nhạc");
        dataSet.setColors(Color.parseColor("#FF7A00"), Color.parseColor("#FFBF00"),
                Color.parseColor("#007AFF"), Color.parseColor("#FF4081"),
                Color.parseColor("#4CAF50"));
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueTextSize(14f);

        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.getLegend().setEnabled(false);
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawEntryLabels(true);
        pieChart.animateY(1000);
        pieChart.invalidate();
    }


    @Override
    public void onError(String msg) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
    }
}

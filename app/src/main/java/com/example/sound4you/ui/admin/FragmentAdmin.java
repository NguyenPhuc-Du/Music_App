package com.example.sound4you.ui.admin;

// <-- Model mới

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sound4you.R;
import com.example.sound4you.utils.ApiClient;
import com.example.sound4you.utils.ApiService;
import com.example.sound4you.utils.StatisticsResponse;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentAdmin extends Fragment {

    private static final String TAG = "FragmentAdmin";
    private PieChart pieChart;
    private ApiService apiService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiService = ApiClient.getService();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin, container, false);
        pieChart = view.findViewById(R.id.genre_pie_chart);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d(TAG, "Đang tải dữ liệu biểu đồ...");
        pieChart.setCenterText("Đang tải...");

        // 1. Gọi API mới
        fetchTrackLikeStatistics();
    }

    /**
     * Hàm gọi API lấy thống kê % like
     */
    private void fetchTrackLikeStatistics() {
        Call<List<StatisticsResponse>> call = apiService.getGenreLikePercentage();

        call.enqueue(new Callback<List<StatisticsResponse>>() {
            @Override
            public void onResponse(Call<List<StatisticsResponse>> call, Response<List<StatisticsResponse>> response) {
                if (isAdded() && response.isSuccessful() && response.body() != null) {

                    List<StatisticsResponse> statsList = response.body();
                    Log.d(TAG, "Tải xong thống kê: " + statsList.size() + " mục.");

                    // 2. Nếu thành công, gọi hàm vẽ biểu đồ
                    setupLikePercentageChart(statsList);

                } else {
                    handleApiError("Lỗi tải thống kê % like");
                }
            }

            @Override
            public void onFailure(Call<List<StatisticsResponse>> call, Throwable t) {
                handleApiError("Lỗi mạng (Like Stats)");
            }
        });
    }

    /**
     * Hàm vẽ biểu đồ % lượt thích
     */
    private void setupLikePercentageChart(List<StatisticsResponse> statsList) {

        // 1. Chuyển đổi List<TrackLikeStats> thành ArrayList<PieEntry>
        ArrayList<PieEntry> entries = new ArrayList<>();
        for (StatisticsResponse stat : statsList) {
            // new PieEntry(giá trị %, "nhãn")
            entries.add(new PieEntry(stat.getPercentage(), stat.getTitle()));
        }

        // 2. Tạo DataSet
        PieDataSet dataSet = new PieDataSet(entries, "Tỉ lệ % Lượt thích");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS); // Dùng bộ màu sặc sỡ

        // 3. Tạo PieData
        PieData pieData = new PieData(dataSet);
        pieData.setValueFormatter(new PercentFormatter(pieChart)); // Hiển thị 15.0%
        pieData.setValueTextSize(12f);
        pieData.setValueTextColor(Color.WHITE);

        // 4. Cấu hình PieChart
        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.setUsePercentValues(true); // <-- Báo cho chart biết ta dùng %
        pieChart.setEntryLabelColor(Color.WHITE);
        pieChart.setCenterText("Lượt thích");
        pieChart.setCenterTextSize(18f);

        Legend legend = pieChart.getLegend();
        legend.setEnabled(false);

        // 6. Cấu hình Donut
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(58f);
        pieChart.setTransparentCircleRadius(61f);

        // 7. Làm mới
        pieChart.invalidate();
    }

    /**
     * Hàm xử lý lỗi chung
     */
    private void handleApiError(String message) {
        Log.e(TAG, message);
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
        pieChart.setCenterText("Lỗi");
        pieChart.setCenterTextColor(Color.RED);
        pieChart.invalidate();
    }
}
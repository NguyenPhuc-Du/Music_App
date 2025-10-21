package com.example.sound4you.ui.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.sound4you.data.model.HomeSection;
import com.example.sound4you.data.model.Track;
import com.example.sound4you.R;
import com.example.sound4you.data.repository.TrackRepository;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private RecyclerView rvHome;
    private TrackRepository trackRepository;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        rvHome = view.findViewById(R.id.rvHome);
        rvHome.setLayoutManager(new LinearLayoutManager(getContext()));

        trackRepository = new TrackRepository();
        loadTracks();

        return view;
    }

    private void loadTracks () {
        trackRepository.getAllTracks(new Callback<List<Track>>() {
            @Override
            public void onResponse(Call<List<Track>> call, Response<List<Track>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Track> trackList = response.body();

                    List<HomeSection> sections = new ArrayList<>();
                    sections.add(new HomeSection("You likes", trackList.subList(0, 4), 0));
                    sections.add(new HomeSection("New Releases", trackList.subList(0, 6), 1));
                    sections.add(new HomeSection("Top Hits", trackList.subList(0, 6), 1));
                    sections.add(new HomeSection("You may like", trackList.subList(0, 6), 1));

                    rvHome.setAdapter(new HomeAdapter(requireContext(), sections));
                }
                else {
                    Toast.makeText(getContext(), "Lỗi không thể tải danh sách bài", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Track>> call, Throwable t) {
                Toast.makeText(requireContext(), "Lỗi mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
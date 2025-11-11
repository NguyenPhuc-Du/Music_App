package com.example.sound4you.ui.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sound4you.R;
import com.example.sound4you.data.model.Track;
import com.example.sound4you.presenter.admin.ManageTrackPresenter;
import com.example.sound4you.presenter.admin.ManageTrackPresenterImpl;

import java.util.List;

public class ManageTrackFragment extends Fragment implements ManageTrackView {

    private RecyclerView recyclerView;
    private ManageTrackAdapter adapter;
    private ManageTrackPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_track, container, false);

        recyclerView = view.findViewById(R.id.manage_list_track);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        presenter = new ManageTrackPresenterImpl(this);
        presenter.loadTracks();

        return view;
    }

    @Override
    public void onTracksLoaded(List<Track> tracks) {
        if (tracks == null) tracks = java.util.Collections.emptyList();

        if (adapter == null) {
            adapter = new ManageTrackAdapter(tracks, new ManageTrackAdapter.TrackActionListener() {
                @Override
                public void onApproveClicked(int trackId) {
                    presenter.approveTrack(trackId);
                }

                @Override
                public void onDeleteClicked(int trackId) {
                    presenter.deleteTrack(trackId);
                }
            });
            recyclerView.setAdapter(adapter);
        } else {
            adapter = new ManageTrackAdapter(tracks, new ManageTrackAdapter.TrackActionListener() {
                @Override
                public void onApproveClicked(int trackId) { presenter.approveTrack(trackId); }
                @Override
                public void onDeleteClicked(int trackId) { presenter.deleteTrack(trackId); }
            });
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onTrackApproved(int id) {
        Toast.makeText(requireContext(), "Đã duyệt bài hát ID: " + id, Toast.LENGTH_SHORT).show();
        presenter.loadTracks();
    }

    @Override
    public void onTrackDeleted(int id) {
        Toast.makeText(requireContext(), "Đã xóa bài hát ID: " + id, Toast.LENGTH_SHORT).show();
        presenter.loadTracks();
    }

    @Override
    public void onError(String msg) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
    }
}

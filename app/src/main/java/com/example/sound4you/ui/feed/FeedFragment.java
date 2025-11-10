package com.example.sound4you.ui.feed;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.sound4you.MainActivity;
import com.example.sound4you.R;
import com.example.sound4you.data.model.Track;
import com.example.sound4you.presenter.feed.FeedPresenter;
import com.example.sound4you.presenter.feed.FeedPresenterImpl;

import java.util.List;
import java.util.ArrayList;

public class FeedFragment extends Fragment implements FeedView{
    private RecyclerView rvFeed;
    private FeedAdapter adapter;
    private final List<Track> data = new ArrayList<>();
    private FeedPresenter presenter;

    public FeedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_feed, container, false);

        rvFeed = v.findViewById(R.id.rvFeed);

        rvFeed.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new FeedAdapter(getContext(), data);
        rvFeed.setAdapter(adapter);

        presenter = new FeedPresenterImpl(this);

        presenter.loadPublicFeed();

        adapter.setOnFeedItemClickListener(track -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).showNowPlaying(track);
            }
        });

        return v;
    }

    @Override
    public void showFeed(List<Track> tracks) {
        rvFeed.setVisibility(View.VISIBLE);
        data.clear();
        data.addAll(tracks);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showError(String message) {
    }

    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {
    }

}
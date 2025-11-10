package com.example.sound4you.ui.feed;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.sound4you.R;
import com.example.sound4you.data.model.Track;
import com.example.sound4you.presenter.feed.FeedFollowingPresenter;
import com.example.sound4you.presenter.feed.FeedFollowingPresenterImpl;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;
import java.util.ArrayList;

public class FeedFollowingFragment extends Fragment implements FeedView{
    private RecyclerView rvFeedFollowing;
    private FeedFollowingAdapter adapter;
    private List<Track> data = new ArrayList<>();
    private FeedFollowingPresenter presenter;
    private String firebaseId;

    public FeedFollowingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_feed_following, container, false);

        rvFeedFollowing = v.findViewById(R.id.rvFeedFollowing);

        rvFeedFollowing.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new FeedFollowingAdapter(getContext(), data);
        rvFeedFollowing.setAdapter(adapter);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        firebaseId = user != null ? user.getUid() : null;

        presenter = new FeedFollowingPresenterImpl(this);

        presenter.loadFollowingFeed(firebaseId);

        return v;
    }

    @Override
    public void showFeed(List<Track> tracks) {
        rvFeedFollowing.setVisibility(View.VISIBLE);
        data.clear();
        data.addAll(tracks);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {
    }
}
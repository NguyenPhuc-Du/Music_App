package com.example.sound4you.ui.profile;

import android.os.Bundle;
import android.view.*;
import android.widget.Toast;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sound4you.MainActivity;
import com.example.sound4you.R;
import com.example.sound4you.data.model.Track;
import com.example.sound4you.presenter.like.LikePresenterImpl;
import com.example.sound4you.ui.stream.LikeStreamView;
import com.example.sound4you.presenter.track.TrackPresenterImpl;
import com.example.sound4you.ui.track.TrackView;

import java.util.List;

public class LikedTrackListFragment extends Fragment implements TrackView {

    private static final String ARG_USERID = "userId";

    public static LikedTrackListFragment newInstance(int userId) {
        Bundle b = new Bundle();
        b.putInt(ARG_USERID, userId);
        LikedTrackListFragment f = new LikedTrackListFragment();
        f.setArguments(b);
        return f;
    }

    private RecyclerView recyclerView;
    private TrackPresenterImpl trackPresenter;
    private LikePresenterImpl likePresenter;
    private int userId;
    private TrackAdapterLike adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_liked_track_list, container, false);

        recyclerView = v.findViewById(R.id.rvCommon);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).hideBottomNav();
        }

        userId = getArguments().getInt(ARG_USERID);

        trackPresenter = new TrackPresenterImpl(this);
        likePresenter = new LikePresenterImpl(new LikeStreamView() {
            @Override public void onLikeChanged(boolean liked) {}
            @Override public void onLikeStatusChecked(boolean liked) {}
            @Override public void onTracksUpdated(List<Track> tracks) {
                if (adapter != null) adapter.notifyDataSetChanged();
            }
            @Override public void onError(String msg) {
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });

        trackPresenter.loadLikedTracks(userId);
        return v;
    }

    @Override
    public void onTracksLoaded(List<Track> tracks) {

        adapter = new TrackAdapterLike(requireContext(), tracks);

        likePresenter.checkLikes(userId, tracks);

        adapter.setOnItemClick(track -> {
            if (getActivity() instanceof MainActivity)
                ((MainActivity) getActivity()).showNowPlaying(track);
        });

        adapter.setOnLikeClick((track, liked) ->
                likePresenter.likeTrack(userId, track.getId(), liked)
        );

        recyclerView.setAdapter(adapter);
    }

    @Override public void showLoading() {}
    @Override public void hideLoading() {}

    @Override
    public void onError(String msg) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).showBottomNav();
        }
    }
}


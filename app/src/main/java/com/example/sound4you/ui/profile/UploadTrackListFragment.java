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

public class UploadTrackListFragment extends Fragment implements TrackView {

    private static final String ARG_ID = "userId";
    private static final String ARG_SELF = "isSelf";

    public static UploadTrackListFragment newInstance(int userId, boolean isSelf) {
        Bundle b = new Bundle();
        b.putInt(ARG_ID, userId);
        b.putBoolean(ARG_SELF, isSelf);
        UploadTrackListFragment f = new UploadTrackListFragment();
        f.setArguments(b);
        return f;
    }

    private RecyclerView recyclerView;
    private TrackPresenterImpl trackPresenter;
    private LikePresenterImpl likePresenter;
    private int userId;
    private boolean isSelf;

    private TrackAdapterUpload adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_upload_track_list, container, false);

        recyclerView = v.findViewById(R.id.rvCommon);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (getActivity() instanceof MainActivity)
            ((MainActivity) getActivity()).hideBottomNav();

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

        userId = getArguments().getInt(ARG_ID);
        isSelf = getArguments().getBoolean(ARG_SELF);

        trackPresenter.loadTracksByUserLimited(userId, 20);

        return v;
    }

    @Override
    public void onTracksLoaded(List<Track> tracks) {

        adapter = new TrackAdapterUpload(requireContext(), tracks, isSelf);

        likePresenter.checkLikes(userId, tracks);

        adapter.setOnItemClick(track -> {
            if (getActivity() instanceof MainActivity)
                ((MainActivity) getActivity()).showNowPlaying(track);
        });

        adapter.setOnDeleteConfirmed(track -> {
            if (isSelf)
                trackPresenter.deleteTrackById(track.getId());
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
}

package com.example.sound4you.ui.profile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.*;
import android.widget.*;
import android.os.Bundle;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.sound4you.MainActivity;
import com.example.sound4you.R;
import com.example.sound4you.data.model.Track;
import com.example.sound4you.data.model.User;
import com.example.sound4you.presenter.like.LikePresenterImpl;
import com.example.sound4you.presenter.profile.ProfilePresenterImpl;
import com.example.sound4you.presenter.track.TrackPresenterImpl;
import com.example.sound4you.ui.auth.AuthActivity;
import com.example.sound4you.ui.follow.FollowerListFragment;
import com.example.sound4you.ui.follow.FollowingListFragment;
import com.example.sound4you.ui.follow.EditProfileFragment;
import com.example.sound4you.ui.stream.LikeStreamView;
import com.example.sound4you.ui.track.TrackView;
import com.google.firebase.auth.FirebaseAuth;
import java.util.List;

public class ProfileFragment extends Fragment implements ProfileView, TrackView {
    private ImageButton btnLogOut, btnEdit;
    private ImageView ivAvatar;
    private TextView tvName, tvBio, tvFollowers, tvFollowing;
    private RecyclerView rvUploads;
    private View uploadsHeader, likesHeader;
    private LinearLayout followerContainer, followingContainer;

    private ProfilePresenterImpl profilePresenter;
    private TrackPresenterImpl trackPresenter;
    private LikePresenterImpl likePresenter;

    private boolean isSelf;
    private int userId;
    private String firebaseUid;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        btnLogOut = v.findViewById(R.id.btnLogOut);
        btnEdit = v.findViewById(R.id.btnEditProfile);
        ivAvatar = v.findViewById(R.id.profilePicture);
        tvName = v.findViewById(R.id.tvNameUser);
        tvBio = v.findViewById(R.id.tvBioUser);
        tvFollowers = v.findViewById(R.id.tvNumberFollowers);
        tvFollowing = v.findViewById(R.id.tvNumberFollowing);
        rvUploads = v.findViewById(R.id.rvUploadTracks);
        uploadsHeader = v.findViewById(R.id.uploadTrackUserContainer);
        likesHeader = v.findViewById(R.id.loveTrackUserContainer);
        followerContainer = v.findViewById(R.id.followerContainer);
        followingContainer = v.findViewById(R.id.followingContainer);

        rvUploads.setLayoutManager(new LinearLayoutManager(getContext()));
        firebaseUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        profilePresenter = new ProfilePresenterImpl(this);
        trackPresenter = new TrackPresenterImpl(this);
        likePresenter = new LikePresenterImpl(new LikeStreamView() {
            @Override
            public void onLikeChanged(boolean liked) {}
            @Override
            public void onLikeStatusChecked(boolean liked) {}
            @Override
            public void onTracksUpdated(List<Track> tracks) {
                TrackAdapterUpload adapter = new TrackAdapterUpload(requireContext(), tracks, isSelf);
                rvUploads.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onError(String msg) {}
        });

        if (getArguments() != null && getArguments().containsKey("userId")) {
            userId = getArguments().getInt("userId");
            isSelf = false;
            profilePresenter.loadProfileById(userId);
            trackPresenter.loadTracksByUserLimited(userId, 3);
            btnEdit.setVisibility(View.GONE);
            likesHeader.setVisibility(View.GONE);
            requireActivity().findViewById(R.id.bottomNav).setVisibility(View.GONE);
        } else {
            isSelf = true;
            profilePresenter.loadProfileByFirebase(firebaseUid);
            btnEdit.setVisibility(View.VISIBLE);
        }

        uploadsHeader.setOnClickListener(v1 -> navigate(UploadTrackListFragment.newInstance(userId, isSelf)));
        likesHeader.setOnClickListener(v2 -> {
            if (isSelf) navigate(LikedTrackListFragment.newInstance(firebaseUid));
        });

        btnEdit.setOnClickListener(v3 -> navigate(EditProfileFragment.newInstance()));

        btnLogOut.setOnClickListener(v1 -> new AlertDialog.Builder(requireContext())
                .setTitle("Đăng xuất")
                .setMessage("Bạn có chắc muốn đăng xuất không?")
                .setPositiveButton("Đăng xuất", (d, w) -> {
                    FirebaseAuth.getInstance().signOut();
                    SharedPreferences prefs = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
                    prefs.edit().clear().apply();
                    Intent intent = new Intent(requireContext(), AuthActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    requireActivity().finish();
                })
                .setNegativeButton("Hủy", null)
                .show());

        followerContainer.setOnClickListener(v4 -> {
            navigate(FollowerListFragment.newInstance());
        });

        followingContainer.setOnClickListener(v5 -> {
            navigate(FollowingListFragment.newInstance());
        });

        return v;
    }

    private void navigate(Fragment f) {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.navHostFragment, f)
                .addToBackStack(null)
                .commit();
    }

    @Override public void showLoading() {}
    @Override public void hideLoading() {}
    @Override public void onError(String msg) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProfileLoaded(User user) {
        this.userId = user.getId();
        tvName.setText(user.getUsername());
        tvBio.setText(user.getBio());
        tvFollowers.setText(String.valueOf(user.getFollowers()));
        tvFollowing.setText(String.valueOf(user.getFollowing()));

        Glide.with(this)
                .load(user.getProfile_picture())
                .placeholder(R.drawable.ic_avatar_placeholder)
                .into(ivAvatar);

        if (isSelf) trackPresenter.loadTracksByFirebaseLimited(firebaseUid, 3);
    }

    @Override
    public void onTracksLoaded(List<Track> tracks) {
        TrackAdapterUpload adapter = new TrackAdapterUpload(requireContext(), tracks, isSelf);

        likePresenter.checkLikes(firebaseUid, tracks);

        adapter.setOnItemClick(track -> {
            if (getActivity() instanceof MainActivity)
                ((MainActivity) getActivity()).showNowPlaying(track);
        });

        adapter.setOnLikeClick((track, liked) -> {
            likePresenter.likeTrack(firebaseUid, track.getId(), liked);
        });

        rvUploads.setAdapter(adapter);
    }

    @Override
    public void onProfileUpdated(User user) {
        Toast.makeText(requireContext(), "Profile updated!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        requireActivity().findViewById(R.id.bottomNav).setVisibility(View.VISIBLE);
    }
}

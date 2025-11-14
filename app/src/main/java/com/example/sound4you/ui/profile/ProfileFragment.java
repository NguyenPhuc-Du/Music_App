package com.example.sound4you.ui.profile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.*;
import android.widget.*;
import android.os.Bundle;
import androidx.annotation.*;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.sound4you.MainActivity;
import com.example.sound4you.R;
import com.example.sound4you.data.model.Track;
import com.example.sound4you.data.model.User;
import com.example.sound4you.presenter.follow.FollowPresenterImpl;
import com.example.sound4you.presenter.like.LikePresenterImpl;
import com.example.sound4you.presenter.profile.ProfilePresenterImpl;
import com.example.sound4you.presenter.track.TrackPresenterImpl;
import com.example.sound4you.ui.auth.AuthActivity;
import com.example.sound4you.ui.follow.FollowerListFragment;
import com.example.sound4you.ui.follow.FollowingListFragment;
import com.example.sound4you.ui.stream.LikeStreamView;
import com.example.sound4you.ui.stream.FollowStreamView;
import com.example.sound4you.ui.track.TrackView;

import java.util.List;

public class ProfileFragment extends Fragment
        implements ProfileView, TrackView, FollowStreamView {

    private ImageButton btnLogOut, btnEdit;
    private ImageView ivAvatar;
    private TextView tvName, tvBio, tvFollowers, tvFollowing;
    private RecyclerView rvUploads;
    private View uploadsHeader, likesHeader;
    private LinearLayout followerContainer, followingContainer;
    private Button btnFollowUser;

    private ProfilePresenterImpl profilePresenter;
    private TrackPresenterImpl trackPresenter;
    private LikePresenterImpl likePresenter;
    private FollowPresenterImpl followPresenter;

    private boolean isSelf;
    private int userId;
    private int currentUserId;

    public static ProfileFragment newInstanceWithUserId(int userId) {
        ProfileFragment f = new ProfileFragment();
        Bundle args = new Bundle();
        args.putInt("userId", userId);
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
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
        btnFollowUser = v.findViewById(R.id.btnFollowUser);

        rvUploads.setLayoutManager(new LinearLayoutManager(getContext()));

        currentUserId = requireContext()
                .getSharedPreferences("AuthPreferences", Context.MODE_PRIVATE)
                .getInt("UserId", -1);

        profilePresenter = new ProfilePresenterImpl(this);
        trackPresenter = new TrackPresenterImpl(this);
        followPresenter = new FollowPresenterImpl(this);
        likePresenter = new LikePresenterImpl(new LikeStreamView() {
            @Override
            public void onTracksUpdated(List<Track> updatedTracks) {
                if (rvUploads.getAdapter() != null)
                    rvUploads.getAdapter().notifyDataSetChanged();
            }
            @Override public void onLikeChanged(boolean liked) {}
            @Override public void onLikeStatusChecked(boolean liked) {}
            @Override public void onError(String msg) {}
        });

        if (getArguments() != null && getArguments().containsKey("userId")) {
            userId = getArguments().getInt("userId");
            isSelf = (userId == currentUserId);

            if (!isSelf) {
                btnEdit.setVisibility(View.GONE);
                likesHeader.setVisibility(View.GONE);
                btnLogOut.setVisibility(View.GONE);
                btnFollowUser.setVisibility(View.VISIBLE);

                profilePresenter.loadProfile(userId);
                trackPresenter.loadTracksByUserLimited(userId, 3);

                followPresenter.checkFollowed(currentUserId, userId);

                btnFollowUser.setOnClickListener(v1 -> {
                    boolean currentlyFollowed =
                            btnFollowUser.getText().toString().equals("Followed");
                    followPresenter.followUser(currentUserId, userId, !currentlyFollowed);
                });
            }
        } else {
            isSelf = true;
            userId = currentUserId;

            profilePresenter.loadProfile(userId);
            followPresenter.loadFollowers(userId);
            followPresenter.loadFollowing(userId);
            btnEdit.setVisibility(View.VISIBLE);
            btnFollowUser.setVisibility(View.GONE);
        }

        uploadsHeader.setOnClickListener(
                v12 -> navigate(UploadTrackListFragment.newInstance(userId, isSelf))
        );

        likesHeader.setOnClickListener(
                v13 -> {
                    if (isSelf) {
                        navigate(LikedTrackListFragment.newInstance(userId));
                    }
                }
        );

        btnEdit.setOnClickListener(v14 ->
                navigate(EditProfileFragment.newInstance())
        );

        btnLogOut.setOnClickListener(v15 ->
                new AlertDialog.Builder(requireContext())
                        .setTitle("Đăng xuất")
                        .setMessage("Bạn có chắc muốn đăng xuất không?")
                        .setPositiveButton("Đăng xuất", (d, w) -> {
                            SharedPreferences prefs =
                                    requireContext().getSharedPreferences("AuthPreferences", Context.MODE_PRIVATE);
                            prefs.edit().clear().apply();

                            Intent i = new Intent(requireContext(), AuthActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                            requireActivity().finish();
                        })
                        .setNegativeButton("Hủy", null)
                        .show()
        );

        followerContainer.setOnClickListener(v16 ->
                navigate(FollowerListFragment.newInstance(userId))
        );
        followingContainer.setOnClickListener(v17 ->
                navigate(FollowingListFragment.newInstance(userId))
        );

        return v;
    }

    private void navigate(Fragment f) {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.navHostFragment, f)
                .addToBackStack(null)
                .commit();
    }

    private void updateFollowButton(boolean following) {
        if (following) {
            btnFollowUser.setText("Followed");
            btnFollowUser.setBackgroundTintList(
                    ContextCompat.getColorStateList(requireContext(), R.color.accent_orange)
            );
            btnFollowUser.setTextColor(
                    getResources().getColor(android.R.color.white)
            );
        } else {
            btnFollowUser.setText("Follow");
            btnFollowUser.setBackgroundTintList(
                    ContextCompat.getColorStateList(requireContext(), android.R.color.transparent)
            );
            btnFollowUser.setTextColor(
                    getResources().getColor(R.color.accent_orange)
            );
        }
    }

    @Override public void showLoading() {}
    @Override public void hideLoading() {}

    @Override
    public void onError(String msg) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProfileLoaded(User user) {
        tvName.setText(user.getUsername());
        tvBio.setText(user.getBio());
        tvFollowers.setText(String.valueOf(user.getFollowers()));
        tvFollowing.setText(String.valueOf(user.getFollowing()));

        Glide.with(this)
                .load(user.getProfile_picture())
                .placeholder(R.drawable.ic_avatar_placeholder)
                .into(ivAvatar);

        if (isSelf) {
            trackPresenter.loadTracksByUserLimited(userId, 3);
        }
    }

    @Override
    public void onTracksLoaded(List<Track> tracks) {
        TrackAdapterUpload adapter =
                new TrackAdapterUpload(requireContext(), tracks, isSelf);

        likePresenter.checkLikes(userId, tracks);

        adapter.setOnItemClick(track -> {
            if (getActivity() instanceof MainActivity)
                ((MainActivity) getActivity()).showNowPlaying(track);
        });

        adapter.setOnLikeClick((track, liked) ->
                likePresenter.likeTrack(userId, track.getId(), liked)
        );

        rvUploads.setAdapter(adapter);
    }

    @Override
    public void onProfileUpdated(User user) {
        Toast.makeText(requireContext(), "Profile updated!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFollowCountLoaded(int followers, int following) {
        if (followers >= 0)
            tvFollowers.setText(String.valueOf(followers));

        if (following >= 0)
            tvFollowing.setText(String.valueOf(following));
    }


    @Override
    public void onFollowChanged(boolean following) {
        updateFollowButton(following);
    }

    @Override
    public void onFollowStatusChecked(boolean followed) {
        updateFollowButton(followed);
    }
}

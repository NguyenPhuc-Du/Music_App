package com.example.sound4you;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import com.example.sound4you.data.model.Track;
import com.example.sound4you.ui.follow.FollowerListFragment;
import com.example.sound4you.ui.follow.FollowingListFragment;
import com.example.sound4you.ui.stream.NowPlayingBar;
import com.example.sound4you.ui.stream.StreamTrackFragment;
import com.example.sound4you.utils.MediaPlayerManager;
import com.example.sound4you.presenter.like.LikePresenterImpl;
import com.example.sound4you.ui.stream.LikeStreamView;
import com.example.sound4you.presenter.follow.FollowPresenterImpl;
import com.example.sound4you.ui.stream.FollowStreamView;
import com.example.sound4you.ui.profile.UploadTrackListFragment;
import com.example.sound4you.ui.profile.LikedTrackListFragment;
import com.example.sound4you.ui.upload.UploadFragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class MainActivity extends AppCompatActivity implements NowPlayingBar.OnNowPlayingActionListener {

    private BottomNavigationView bottomNavigationView;
    private NavController navController;

    private MediaPlayerManager mediaPlayerManager;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private NowPlayingBar nowPlayingBar;
    private View includeNowPlaying;
    private Track currentTrack;

    private boolean isLiked = false;
    private boolean isFollowing = false;
    private boolean isNowPlayingVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // NavHost
        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.navHostFragment);
        if (navHostFragment != null)
            navController = navHostFragment.getNavController();

        bottomNavigationView = findViewById(R.id.bottomNav);
        if (navController != null)
            NavigationUI.setupWithNavController(bottomNavigationView, navController);

        updateBottomNavVisibility();

        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            updateBottomNavVisibility();
            if (getSupportFragmentManager().getBackStackEntryCount() == 0 && isNowPlayingVisible) {
                restoreNowPlayingBar();
            }
        });

        includeNowPlaying = findViewById(R.id.includedNowPlayingBar);
        if (includeNowPlaying != null) {
            includeNowPlaying.setVisibility(View.GONE);
            nowPlayingBar = includeNowPlaying.findViewById(R.id.nowPlayingBar);
            if (nowPlayingBar != null) nowPlayingBar.setOnNowPlayingActionListener(this);
        }
        mediaPlayerManager = MediaPlayerManager.getInstance();

        if (nowPlayingBar != null) {
            nowPlayingBar.setOnClickListener(v -> {
                if (currentTrack != null) {
                    StreamTrackFragment fragment = StreamTrackFragment.newInstance(currentTrack);
                    fragment.setNowPlayingBar(nowPlayingBar);

                    getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(
                                    R.anim.slide_up, R.anim.fade_out,
                                    R.anim.fade_in, R.anim.slide_down
                            )
                            .replace(R.id.navHostFragment, fragment)
                            .addToBackStack("StreamTrack")
                            .commit();

                    hideNowPlayingBar();
                }
            });
        }

        // Xử lý nút Back chung
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                int backCount = getSupportFragmentManager().getBackStackEntryCount();
                if (backCount > 0) {
                    getSupportFragmentManager().popBackStack();
                } else {
                    finish();
                }
            }
        });

        startProgressBar();
    }

    private void updateBottomNavVisibility() {
        boolean hide = false;

        Fragment f = getSupportFragmentManager().findFragmentById(R.id.navHostFragment);
        if (f instanceof NavHostFragment) {
            if (navController != null && navController.getCurrentDestination() != null) {
                int destId = navController.getCurrentDestination().getId();
                if (destId == R.id.uploadFragment) {
                    hide = true;
                }
            }
        } else if (f != null) {
            if (f instanceof UploadTrackListFragment || f instanceof LikedTrackListFragment || f instanceof UploadFragment || f instanceof StreamTrackFragment || f instanceof FollowerListFragment || f instanceof FollowingListFragment) {
                hide = true;
            }
        }

        if (bottomNavigationView != null) bottomNavigationView.setVisibility(hide ? View.GONE : View.VISIBLE);
    }

    public void hideBottomNav() {
        if (bottomNavigationView != null) bottomNavigationView.setVisibility(View.GONE);
    }

    public void showBottomNav() {
        if (bottomNavigationView != null) bottomNavigationView.setVisibility(View.VISIBLE);
    }

    public void showNowPlaying(Track track) {
        if (track == null) return;

        if (includeNowPlaying == null || nowPlayingBar == null) {
            return;
        }

        String firebaseUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        LikePresenterImpl likePresenter = new LikePresenterImpl(new LikeStreamView() {
            @Override
            public void onLikeChanged(boolean liked) {
            }

            @Override
            public void onLikeStatusChecked(boolean liked) {
                isLiked = liked;
                ImageButton btnLike = nowPlayingBar.getLikeButton();
                if (btnLike != null) {
                    btnLike.setImageTintList(ColorStateList.valueOf(
                            liked ? Color.parseColor("#FF7A00") : Color.BLACK));
                }
            }

            @Override
            public void onTracksUpdated(List<Track> tracks) {}

            @Override
            public void onError(String msg) {
            }

        });

        FollowPresenterImpl followPresenter = new FollowPresenterImpl(new FollowStreamView() {
            @Override
            public void onFollowChanged(boolean following) {
            }

            @Override
            public void onFollowStatusChecked(boolean followed) {
                isFollowing = followed;
                ImageButton btnFollow = nowPlayingBar.getFollowButton();
                if (btnFollow != null) btnFollow.setImageResource(followed ? R.drawable.ic_followed : R.drawable.ic_follow);
            }

            @Override
            public void onError(String msg) {
            }
        });

        likePresenter.checkLiked(firebaseUid, track.getId());
        followPresenter.checkFollowed(firebaseUid, track.getUser_id());

        this.currentTrack = track;
        nowPlayingBar.updateTrackInfo(track);
        includeNowPlaying.setVisibility(View.VISIBLE);
        includeNowPlaying.setAlpha(0f);
        includeNowPlaying.animate()
                .translationY(0)
                .alpha(1f)
                .setDuration(250)
                .start();

        isNowPlayingVisible = true;

        mediaPlayerManager.play(track.getAudio_url(), () -> {
            if (nowPlayingBar != null) nowPlayingBar.setPlaying(true);
        });
    }

    public void hideNowPlayingBar() {
        if (isNowPlayingVisible && includeNowPlaying != null && includeNowPlaying.getVisibility() == View.VISIBLE) {
            includeNowPlaying.animate()
                    .translationY(includeNowPlaying.getHeight())
                    .alpha(0f)
                    .setDuration(200)
                    .withEndAction(() -> includeNowPlaying.setVisibility(View.GONE))
                    .start();
        }
    }

    public void restoreNowPlayingBar() {
        if (isNowPlayingVisible && includeNowPlaying != null && includeNowPlaying.getVisibility() == View.GONE) {
            includeNowPlaying.setVisibility(View.VISIBLE);
            includeNowPlaying.setAlpha(0f);
            includeNowPlaying.animate()
                    .translationY(0)
                    .alpha(1f)
                    .setDuration(250)
                    .start();
        }
    }

    public boolean isNowPlayingVisible() {
        return isNowPlayingVisible;
    }

    // Nút Play/Pause
    @Override
    public void onPlayPauseClicked() {
        if (currentTrack == null) {
            Toast.makeText(this, "Không có bài hát nào đang phát", Toast.LENGTH_SHORT).show();
            return;
        }
        mediaPlayerManager.toggle();
        if (nowPlayingBar != null) nowPlayingBar.setPlaying(mediaPlayerManager.isPlaying());
    }

    // Nút Like
    @Override
    public void onLikeClicked() {
        if (currentTrack == null) {
            Toast.makeText(this, "Không có bài hát nào để thích", Toast.LENGTH_SHORT).show();
            return;
        }

        isLiked = !isLiked;
        ImageButton btnLike = nowPlayingBar != null ? nowPlayingBar.getLikeButton() : null;
        if (btnLike != null) btnLike.setImageResource(R.drawable.ic_like);
        int color = isLiked ? Color.parseColor("#FF7A00") : Color.BLACK;
        if (btnLike != null) btnLike.setImageTintList(ColorStateList.valueOf(color));

        String firebaseUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        LikePresenterImpl likePresenter = new LikePresenterImpl(new LikeStreamView() {
            @Override
            public void onLikeChanged(boolean liked) {
                isLiked = liked;
            }

            @Override
            public void onLikeStatusChecked(boolean liked) {
            }

            @Override
            public void onTracksUpdated(List<Track> tracks) {}

            @Override
            public void onError(String msg) {
                isLiked = !isLiked;
                if (btnLike != null) btnLike.setImageResource(R.drawable.ic_like);
                int color = isLiked ? Color.parseColor("#FF7A00") : Color.BLACK;
                if (btnLike != null) btnLike.setImageTintList(ColorStateList.valueOf(color));
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
        if (firebaseUid != null) likePresenter.likeTrack(firebaseUid, currentTrack.getId(), !isLiked);
    }

    // Nút Follow
    @Override
    public void onFollowClicked() {
        if (currentTrack == null) {
            Toast.makeText(this, "Không có nghệ sĩ nào để theo dõi", Toast.LENGTH_SHORT).show();
            return;
        }

        String firebaseUid = null;
        if (FirebaseAuth.getInstance().getCurrentUser() != null) firebaseUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        int userId = currentTrack.getUser_id();
        FollowPresenterImpl followPresenter = new FollowPresenterImpl(new FollowStreamView() {
            @Override
            public void onFollowChanged(boolean following) {
                isFollowing = following;
                ImageButton btnFollow = nowPlayingBar != null ? nowPlayingBar.getFollowButton() : null;
                if (btnFollow != null) btnFollow.setImageResource(following ? R.drawable.ic_followed : R.drawable.ic_follow);
            }

            @Override
            public void onFollowStatusChecked(boolean followed) {
            }

            @Override
            public void onError(String msg) {
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
        if (firebaseUid != null) followPresenter.followUser(firebaseUid, userId, isFollowing);
    }

    // Cập nhật tiến trình nhạc
    private void startProgressBar() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayerManager != null && mediaPlayerManager.isPlaying()) {
                    int duration = mediaPlayerManager.getDuration();
                    if (duration > 0) {
                        int progress = mediaPlayerManager.getCurrentPosition() * 100 / duration;
                        if (nowPlayingBar != null) nowPlayingBar.updateProcess(progress);
                    }
                }
                handler.postDelayed(this, 300);
            }
        }, 300);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startProgressBar();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayerManager.release();
        handler.removeCallbacksAndMessages(null);
    }
}

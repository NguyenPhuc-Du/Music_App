package com.example.sound4you.ui.search;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.sound4you.MainActivity;
import com.example.sound4you.R;
import com.example.sound4you.data.model.Genre;
import com.example.sound4you.data.model.Track;
import com.example.sound4you.data.model.User;
import com.example.sound4you.presenter.follow.FollowPresenterImpl;
import com.example.sound4you.presenter.search.SearchPresenter;
import com.example.sound4you.presenter.search.SearchPresenterImpl;
import com.example.sound4you.presenter.genre.GenrePresenter;
import com.example.sound4you.presenter.genre.GenrePresenterImpl;
import com.example.sound4you.ui.genre.GenreView;
import com.example.sound4you.ui.profile.ProfileFragment;

import java.util.List;

public class SearchFragment extends Fragment implements SearchView, GenreView{
    private EditText editText;
    private RecyclerView rvResult;
    private ImageButton btnSearch;
    private Button btnTrack, btnUser, btnGenre;
    private LinearLayout genreContainer, genreList;
    private SearchPresenter searchPresenter;
    private GenrePresenter genrePresenter;

    private String currentMode = "track";
    private String lastQuery = "";

    private boolean wasNowPlayingVisible = false;

    private boolean isFollowed = false;
    private boolean isLiked = false;

    public SearchFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);

        View nowPlaying = requireActivity().findViewById(R.id.includedNowPlayingBar);
        wasNowPlayingVisible = nowPlaying.getVisibility() == View.VISIBLE;

        if (getActivity() instanceof com.example.sound4you.MainActivity) {
            ((com.example.sound4you.MainActivity) getActivity()).hideBottomNav();
            nowPlaying.setVisibility(View.GONE);
        } else {
            requireActivity().findViewById(R.id.bottomNav).setVisibility(View.GONE);
            nowPlaying.setVisibility(View.GONE);
        }

        editText = v.findViewById(R.id.etFindTrack);
        rvResult = v.findViewById(R.id.rvSearchResults);
        btnSearch = v.findViewById(R.id.btnSearch);
        btnTrack = v.findViewById(R.id.btnTabTrack);
        btnUser = v.findViewById(R.id.btnTabUser);
        btnGenre = v.findViewById(R.id.btnTabGenre);
        genreContainer = v.findViewById(R.id.genreContainer);
        genreList = v.findViewById(R.id.genreList);

        btnTrack.setVisibility(View.GONE);
        btnUser.setVisibility(View.GONE);
        btnGenre.setVisibility(View.GONE);

        rvResult.setLayoutManager(new LinearLayoutManager(getContext()));
        searchPresenter = new SearchPresenterImpl(this);
        genrePresenter = new GenrePresenterImpl(this);

        btnSearch.setOnClickListener(view -> {
            String query = editText.getText().toString().trim();
            lastQuery = query;

            if (query.isEmpty()) {
                return;
            }

            btnTrack.setVisibility(View.VISIBLE);
            btnUser.setVisibility(View.VISIBLE);
            btnGenre.setVisibility(View.VISIBLE);

            searchPresenter.searchTracks(lastQuery);
        });

        btnTrack.setOnClickListener(v1 -> switchTab("track"));
        btnUser.setOnClickListener(v2 -> switchTab("user"));
        btnGenre.setOnClickListener(v3 -> switchTab("genre"));

        return v;
    }

    private void switchTab (String tab) {
        String query = editText.getText().toString().trim();
        lastQuery = query;
        currentMode = tab;

        btnTrack.setBackgroundTintList(ColorStateList.valueOf(
                tab.equals("track") ? Color.parseColor("#FF7A00") : Color.parseColor("#333333")));
        if (tab.equals("track")) {
            searchPresenter.searchTracks(lastQuery);

        }

        btnUser.setBackgroundTintList(ColorStateList.valueOf(
                tab.equals("user") ? Color.parseColor("#FF7A00") : Color.parseColor("#333333")));
        if (tab.equals("user")) {
            searchPresenter.searchUsers(lastQuery);
        }

        btnGenre.setBackgroundTintList(ColorStateList.valueOf(
                tab.equals("genre") ? Color.parseColor("#FF7A00") : Color.parseColor("#333333")));

        if (tab.equals("genre")) {
            genreContainer.setVisibility(View.VISIBLE);
            genrePresenter.loadGenres();
        } else {
            genreContainer.setVisibility(View.GONE);
        }

    }

    @Override
    public void onGenresLoaded(List<Genre> genres) {
        if (!isAdded() || getContext() == null) return;
        genreList.removeAllViews();

        for (Genre g : genres) {
            Button btn = new Button(requireContext());
            btn.setText(g.getTitle());
            btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#333333")));

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
              LinearLayout.LayoutParams.WRAP_CONTENT,
              LinearLayout.LayoutParams.WRAP_CONTENT
            );

            lp.setMarginEnd(12);
            btn.setLayoutParams(lp);

            btn.setOnClickListener(v -> {
                genreContainer.setVisibility(View.GONE);
                searchPresenter.searchGenres(lastQuery, g.getTitle());
            });

            genreList.addView(btn);
        }
    }

    @Override
    public void showLoading() {}

    @Override
    public void hideLoading() {}

    @Override
    public void onGenreError(String msg) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showTracks(List<Track> tracks) {
        SearchAdapterTrack adapterTrack = new SearchAdapterTrack(requireContext(), tracks);

        adapterTrack.setOnItemClick(track -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).showNowPlaying(track);
            }
        });

        rvResult.setAdapter(adapterTrack);
    }

    @Override
    public void showUsers(List<User> users) {
        SearchAdapterUser adapter = new SearchAdapterUser(requireContext(), users);

        adapter.setOnUserClick(user -> {
            Bundle bundle = new Bundle();
            bundle.putInt("userId", user.getId());

            Fragment fragment = new ProfileFragment();
            fragment.setArguments(bundle);

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_right, R.anim.fade_out,
                            R.anim.fade_in, R.anim.slide_out_right)
                    .replace(R.id.navHostFragment, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        rvResult.setAdapter(adapter);
    }

    @Override
    public void showError(String msg) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (getActivity() instanceof com.example.sound4you.MainActivity) {
            if (wasNowPlayingVisible)
                ((com.example.sound4you.MainActivity) getActivity()).restoreNowPlayingBar();
        } else {
            if (wasNowPlayingVisible)
                requireActivity().findViewById(R.id.includedNowPlayingBar).setVisibility(View.VISIBLE);
        }
    }

}
package com.example.sound4you.ui.home;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.sound4you.MainActivity;
import com.example.sound4you.data.model.HomeSection;
import com.example.sound4you.R;
import com.example.sound4you.presenter.home.HomePresenterImpl;
import com.example.sound4you.ui.search.SearchFragment;
import com.example.sound4you.ui.upload.UploadFragment;
import java.util.List;

public class HomeFragment extends Fragment implements HomeView {

    private RecyclerView rvHome;
    private HomePresenterImpl presenter;

    public HomeFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        rvHome = view.findViewById(R.id.rvHome);
        rvHome.setLayoutManager(new LinearLayoutManager(getContext()));
        presenter = new HomePresenterImpl(this);
        presenter.loadHomeSections();

        ImageView ivUpload = view.findViewById(R.id.btnHomeUpload);
        ImageView ivSearch = view.findViewById(R.id.btnHomeSearch);

        ivUpload.setOnClickListener(v -> {
            MainActivity activity = (MainActivity) requireActivity();
            if (activity.isNowPlayingVisible()) {
                activity.hideNowPlayingBar();
            }

            UploadFragment uploadFragment = new UploadFragment();
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(
                            R.anim.slide_in_right, R.anim.fade_out,
                            R.anim.fade_in, R.anim.slide_out_right
                    )
                    .replace(R.id.navHostFragment, uploadFragment)
                    .addToBackStack("Upload")
                    .commit();
        });

        ivSearch.setOnClickListener(v -> {
            MainActivity activity = (MainActivity) requireActivity();
            if (activity.isNowPlayingVisible()) {
                activity.hideNowPlayingBar();
            }

            SearchFragment searchFragment = new SearchFragment();
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(
                            R.anim.slide_in_right, R.anim.fade_out,
                            R.anim.fade_in, R.anim.slide_out_right
                    )
                    .replace(R.id.navHostFragment, searchFragment)
                    .addToBackStack("Search")
                    .commit();
        });

        return view;
    }

    @Override
    public void showLoading(boolean isLoading) {}

    @Override
    public void showSections(List<HomeSection> sections) {
        rvHome.setAdapter(new HomeAdapter(requireContext(), sections, track -> {
            ((MainActivity) requireActivity()).showNowPlaying(track);
        }));
    }

    @Override
    public void showError(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}

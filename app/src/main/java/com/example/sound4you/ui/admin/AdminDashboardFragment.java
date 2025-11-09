package com.example.sound4you.ui.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.sound4you.R;
import com.example.sound4you.ui.auth.LoginFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminDashboardFragment extends Fragment {

    private static final String TAG = "AdminDashboardFragment";
    private BottomNavigationView bottomNavigationView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bottomNavigationView = view.findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_dashboard) {
                selectedFragment = new AdminHomeFragment();
            } else if (itemId == R.id.nav_tracks) {
                selectedFragment = new ManageTrackFragment();
            } else if (itemId == R.id.nav_users) {
                selectedFragment = new ManageUserFragment();
            } else if (itemId == R.id.nav_stats) {
                selectedFragment = new FragmentAdmin();
            } else if (itemId == R.id.nav_logout) {
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container_view, new LoginFragment())
                        .commit();
                return true;
            }

            if (selectedFragment != null) {
                replaceFragment(selectedFragment);
            }

            return true;
        });

        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_dashboard);
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.admin_fragment_container, fragment);
        fragmentTransaction.commit();
    }
}
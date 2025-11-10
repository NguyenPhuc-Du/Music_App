package com.example.sound4you.ui.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.sound4you.R;
import com.example.sound4you.ui.auth.AuthActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class AdminDashboardFragment extends Fragment {

    private BottomNavigationView bottomNavigation;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bottomNavigation = view.findViewById(R.id.bottom_navigation);

        // Ensure bottomNavigation exists before setting listener
        if (bottomNavigation != null) {
            bottomNavigation.setOnItemSelectedListener(item -> {
                Fragment selected = null;
                int id = item.getItemId();

                if (id == R.id.nav_dashboard) {
                    selected = new AdminHomeFragment();
                } else if (id == R.id.nav_tracks) {
                    selected = new ManageTrackFragment();
                } else if (id == R.id.nav_users) {
                    selected = new ManageUserFragment();
                } else if (id == R.id.nav_stats) {
                    selected = new FragmentAdmin();
                } else if (id == R.id.nav_logout) {
                    FirebaseAuth.getInstance().signOut();

                    Context ctx = requireContext();
                    SharedPreferences prefsAuth = ctx.getSharedPreferences("AuthPreferences", Context.MODE_PRIVATE);
                    prefsAuth.edit().clear().apply();

                    SharedPreferences prefsUser = ctx.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
                    prefsUser.edit().clear().apply();

                    Intent i = new Intent(ctx, AuthActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    return true;
                }

                if (selected != null) {
                    FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                    transaction.replace(R.id.admin_fragment_container, selected);
                    transaction.commit();
                }

                return true;
            });
        }

        // Ensure initial child fragment is loaded reliably
        if (savedInstanceState == null) {
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.replace(R.id.admin_fragment_container, new AdminHomeFragment());
            transaction.commit();
            if (bottomNavigation != null) bottomNavigation.setSelectedItemId(R.id.nav_dashboard);
        }
    }
}

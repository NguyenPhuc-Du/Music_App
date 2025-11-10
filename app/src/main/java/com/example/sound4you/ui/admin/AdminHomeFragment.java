package com.example.sound4you.ui.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sound4you.R;
import com.example.sound4you.presenter.admin.AdminHomePresenter;
import com.example.sound4you.presenter.admin.AdminHomePresenterImpl;

public class AdminHomeFragment extends Fragment implements AdminHomeView {

    private TextView txtPending, txtApproved, txtUsers;
    private AdminHomePresenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_home, container, false);

        txtPending = view.findViewById(R.id.number_of_pending);
        txtApproved = view.findViewById(R.id.number_of_approved);
        txtUsers = view.findViewById(R.id.number_of_users);

        presenter = new AdminHomePresenterImpl(this);
        presenter.loadDashboard();

        return view;
    }

    @Override
    public void showPendingCount(int count) {
        txtPending.setText(String.valueOf(count));
    }

    @Override
    public void showApprovedCount(int count) {
        txtApproved.setText(String.valueOf(count));
    }

    @Override
    public void showUserCount(int count) {
        txtUsers.setText(String.valueOf(count));
    }

    @Override
    public void onError(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}

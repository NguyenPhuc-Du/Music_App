package com.example.sound4you.ui.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sound4you.R;
import com.example.sound4you.data.model.User;
import com.example.sound4you.presenter.admin.ManageUserPresenter;
import com.example.sound4you.presenter.admin.ManageUserPresenterImpl;

import java.util.List;

public class ManageUserFragment extends Fragment implements ManageUserView {

    private RecyclerView recyclerView;
    private ManageUserAdapter adapter;
    private ManageUserPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_user, container, false);

        recyclerView = view.findViewById(R.id.manage_list_user);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        presenter = new ManageUserPresenterImpl(this);
        presenter.loadUsers();

        return view;
    }

    @Override
    public void onUsersLoaded(List<User> users) {
        if (users == null) users = java.util.Collections.emptyList();

        if (adapter == null) {
            adapter = new ManageUserAdapter(users, userId -> presenter.deleteUser(userId));
            recyclerView.setAdapter(adapter);
        } else {
            adapter = new ManageUserAdapter(users, userId -> presenter.deleteUser(userId));
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onUserDeleted(int id) {
        Toast.makeText(requireContext(), "Đã xóa người dùng ID: " + id, Toast.LENGTH_SHORT).show();
        presenter.loadUsers();
    }

    @Override
    public void onError(String msg) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
    }
}

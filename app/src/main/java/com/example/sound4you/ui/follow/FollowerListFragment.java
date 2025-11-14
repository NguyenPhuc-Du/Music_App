package com.example.sound4you.ui.follow;

import android.os.Bundle;
import android.view.*;
import android.widget.Toast;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sound4you.R;
import com.example.sound4you.data.model.User;
import com.example.sound4you.presenter.follow.FollowPresenterImpl;

import java.util.List;

public class FollowerListFragment extends Fragment implements FollowView {

    private static final String ARG_USER_ID = "userId";

    public static FollowerListFragment newInstance(int userId) {
        Bundle b = new Bundle();
        b.putInt(ARG_USER_ID, userId);
        FollowerListFragment f = new FollowerListFragment();
        f.setArguments(b);
        return f;
    }

    private int userId;
    private FollowPresenterImpl followPresenter;
    private RecyclerView rvFollowers;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_follower_list, container, false);

        rvFollowers = v.findViewById(R.id.rvFollowList);
        rvFollowers.setLayoutManager(new LinearLayoutManager(getContext()));

        userId = getArguments().getInt(ARG_USER_ID);

        followPresenter = new FollowPresenterImpl(this);
        followPresenter.loadFollowers(userId);

        return v;
    }

    @Override
    public void onFollowersLoaded(List<User> followers) {
        rvFollowers.setAdapter(new FollowAdapter(requireContext(), followers));
    }

    @Override
    public void onFollowingLoaded(List<User> following) {}

    @Override
    public void onError(String msg) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFollowCountLoaded(int followers, int following) {
    }
}


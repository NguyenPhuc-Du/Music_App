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
import com.google.firebase.auth.FirebaseAuth;
import java.util.List;

public class FollowingListFragment extends Fragment implements FollowView {

    private RecyclerView recyclerView;
    private FollowPresenterImpl presenter;
    private String firebaseUid;

    public static FollowingListFragment newInstance() {
        return new FollowingListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_following_list, container, false);
        recyclerView = v.findViewById(R.id.rvFollowList);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        firebaseUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        presenter = new FollowPresenterImpl(this);
        presenter.loadFollowingByFirebase(firebaseUid);

        return v;
    }

    @Override
    public void onFollowingLoaded(List<User> following) {
        recyclerView.setAdapter(new FollowAdapter(requireContext(), following));
    }

    @Override
    public void onFollowersLoaded(List<User> followers) {}

    @Override
    public void onError(String msg) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
    }
}

package com.example.sound4you.ui.follow;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.Log;
import android.view.*;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sound4you.R;
import com.example.sound4you.data.model.User;
import com.example.sound4you.presenter.follow.FollowPresenterImpl;
import com.example.sound4you.ui.profile.ProfileFragment;
import com.example.sound4you.ui.stream.FollowStreamView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FollowAdapter extends RecyclerView.Adapter<FollowAdapter.ViewHolder> {

    private final Context context;
    private final List<User> userList;

    public FollowAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_user_following, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = userList.get(position);
        String firebaseUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        holder.tvName.setText(user.getUsername());

        Glide.with(context)
                .load(user.getProfile_picture())
                .placeholder(R.drawable.ic_avatar_placeholder)
                .into(holder.ivAvatar);

        FollowPresenterImpl followPresenter = new FollowPresenterImpl(new FollowStreamView() {
            @Override
            public void onFollowChanged(boolean following) {
                updateButtonStyle(holder.btnFollow, following);
            }

            @Override
            public void onFollowStatusChecked(boolean followed) {
                updateButtonStyle(holder.btnFollow, followed);
            }

            @Override
            public void onFollowCountLoaded(int followers, int following) {}

            @Override
            public void onError(String msg) {
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }
        });

        followPresenter.checkFollowed(firebaseUid, user.getId());

        holder.btnFollow.setOnClickListener(v -> {
            boolean isCurrentlyFollowed = holder.btnFollow.getText().toString().equals("Followed");
            followPresenter.followUser(firebaseUid, user.getId(), !isCurrentlyFollowed);
        });

        holder.ivAvatar.setOnClickListener(v -> {
            if (!(context instanceof FragmentActivity)) return;
            FragmentActivity activity = (FragmentActivity) context;

            ProfileFragment fragment = ProfileFragment.newInstanceWithUserId(user.getId());
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.navHostFragment, fragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

    private void updateButtonStyle(MaterialButton button, boolean isFollowing) {
        if (isFollowing) {
            button.setText("Followed");
            button.setTextColor(context.getColor(android.R.color.white));
            button.setBackgroundTintList(ColorStateList.valueOf(context.getColor(R.color.accent_orange)));
        } else {
            button.setText("Follow");
            button.setTextColor(context.getColor(R.color.accent_orange));
            button.setStrokeColor(ColorStateList.valueOf(context.getColor(R.color.accent_orange)));
            button.setStrokeWidth(2);
            button.setBackgroundTintList(ColorStateList.valueOf(context.getColor(android.R.color.transparent)));
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView ivAvatar;
        TextView tvName, tvFollowerCount;
        MaterialButton btnFollow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.ivUserAvatar);
            tvName = itemView.findViewById(R.id.tvUserName);
            btnFollow = itemView.findViewById(R.id.btnFollow);
        }
    }
}

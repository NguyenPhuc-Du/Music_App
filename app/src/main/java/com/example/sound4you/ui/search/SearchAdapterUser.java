package com.example.sound4you.ui.search;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sound4you.R;
import com.example.sound4you.data.model.User;
import com.example.sound4you.presenter.follow.FollowPresenterImpl;
import com.example.sound4you.ui.stream.FollowStreamView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class SearchAdapterUser extends RecyclerView.Adapter<SearchAdapterUser.ViewHolder> {

    public interface OnUserClick {
        void OnClick(User user);
    }

    private final Context context;
    private final List<User> userList;
    private OnUserClick userClick;

    public SearchAdapterUser(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    public void setOnUserClick(OnUserClick userClick) {
        this.userClick = userClick;
    }

    @NonNull
    @Override
    public SearchAdapterUser.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_search_users, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapterUser.ViewHolder holder, int position) {
        User user = userList.get(position);

        SharedPreferences prefs = context.getSharedPreferences("AuthPreferences", Context.MODE_PRIVATE);
        int currentUserId = prefs.getInt("UserId", -1);

        holder.tvUsername.setText(user.getUsername());

        Glide.with(context)
                .load(user.getProfile_picture())
                .placeholder(R.drawable.ic_avatar_placeholder)
                .into(holder.ivUserAvatar);

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

        followPresenter.checkFollowed(currentUserId, user.getId());

        holder.btnFollow.setOnClickListener(v -> {
            boolean isFollowed = holder.btnFollow.getText().toString().equals("Following");

            holder.btnFollow.animate()
                    .scaleX(0.9f).scaleY(0.9f)
                    .setDuration(120)
                    .withEndAction(() ->
                            holder.btnFollow.animate().scaleX(1f).scaleY(1f).setDuration(120).start()
                    ).start();

            followPresenter.followUser(currentUserId, user.getId(), !isFollowed);
        });

        // --- CLICK PROFILE ---
        holder.itemView.setOnClickListener(v -> {
            if (userClick != null) userClick.OnClick(user);
        });
    }

    private void updateButtonStyle(MaterialButton button, boolean isFollowing) {
        if (isFollowing) {
            button.setText("Following");
            button.setTextColor(context.getColor(android.R.color.white));
            button.setBackgroundTintList(ColorStateList.valueOf(context.getColor(R.color.accent_orange)));
            button.setStrokeWidth(0);
        } else {
            button.setText("Follow");
            button.setTextColor(context.getColor(R.color.accent_orange));
            button.setStrokeColor(ColorStateList.valueOf(context.getColor(R.color.accent_orange)));
            button.setStrokeWidth(2);
            button.setBackgroundTintList(ColorStateList.valueOf(Color.TRANSPARENT));
        }
    }

    @Override
    public int getItemCount() {
        return userList == null ? 0 : userList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView ivUserAvatar;
        TextView tvUsername;
        MaterialButton btnFollow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivUserAvatar = itemView.findViewById(R.id.ivUserAvatar);
            tvUsername = itemView.findViewById(R.id.tvUserName);
            btnFollow = itemView.findViewById(R.id.btnFollow);
        }
    }
}

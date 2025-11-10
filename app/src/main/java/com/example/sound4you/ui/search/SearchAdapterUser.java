package com.example.sound4you.ui.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sound4you.R;
import com.example.sound4you.data.model.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class SearchAdapterUser extends RecyclerView.Adapter<SearchAdapterUser.SearchUserViewHolder>{
    public interface OnFollowClick {
        void onFollow(User user);
    }
    public interface OnUserClick {
        void OnClick(User user);
    }

    private final Context context;
    private final List<User> data;
    private OnFollowClick followClick;
    private OnUserClick userClick;

    public SearchAdapterUser (Context context, List<User> data) {
        this.context = context;
        this.data = data;
    }

    public void setONFollowClick(OnFollowClick followClick) {
        this.followClick = followClick;
    }

    public void setOnUserClick(OnUserClick userClick) {
        this.userClick = userClick;
    }

    @NonNull
    @Override
    public SearchUserViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_search_users, parent, false);
        return new SearchUserViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchUserViewHolder viewHolder, int position) {
        User user = data.get(position);
        viewHolder.tvUsername.setText(user.getUsername());
        viewHolder.tvUserFollowerCount.setText(String.format("%d followers", user.getFollowers()));

        Glide.with(context)
                .load(user.getProfile_picture())
                .placeholder(R.drawable.ic_avatar_placeholder)
                .into(viewHolder.ivUserAvatar);

        viewHolder.itemView.setOnClickListener(v -> {
            if (userClick != null) {
                userClick.OnClick(user);
            }
        });

        viewHolder.btnFollow.setOnClickListener(v -> {
            if (followClick != null) {
                followClick.onFollow(user);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    static class SearchUserViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView ivUserAvatar;
        TextView tvUsername;
        TextView tvUserFollowerCount;
        MaterialButton btnFollow;

        SearchUserViewHolder(@NonNull View v) {
            super(v);
            ivUserAvatar = v.findViewById(R.id.ivTrackUserAvatar);
            tvUsername = v.findViewById(R.id.tvUserName);
            tvUserFollowerCount = v.findViewById(R.id.tvUserFollowerCount);
            btnFollow = v.findViewById(R.id.btnFollow);
        }
    }
}

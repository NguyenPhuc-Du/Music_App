package com.example.sound4you.ui.follow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.sound4you.R;
import com.example.sound4you.data.model.User;
import java.util.List;

public class FollowAdapter extends RecyclerView.Adapter<FollowAdapter.ViewHolder> {

    private final Context context;
    private final List<User> userList;
    private OnFollowClickListener listener;

    public interface OnFollowClickListener {
        void onFollowClick(User user, boolean isFollowing);
    }

    public FollowAdapter(Context context, List<User> userList, OnFollowClickListener listener) {
        this.context = context;
        this.userList = userList;
        this.listener = listener;
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
        holder.tvName.setText(user.getUsername());
        holder.tvFollowerCount.setText(user.getFollowers() + " followers");

        Glide.with(context)
                .load(user.getProfile_picture())
                .placeholder(R.drawable.ic_avatar_placeholder)
                .into(holder.ivAvatar);

        // Hiển thị trạng thái nút follow
        boolean isFollowing = user.getFollowers() > 0;
        updateButtonStyle(holder.btnFollow, isFollowing);

        holder.btnFollow.setOnClickListener(v -> {
            boolean newState = !isFollowing;
            updateButtonStyle(holder.btnFollow, newState);
            if (listener != null) listener.onFollowClick(user, newState);
        });
    }

    private void updateButtonStyle(MaterialButton button, boolean isFollowing) {
        if (isFollowing) {
            button.setText("Following");
            button.setBackgroundTintList(context.getColorStateList(R.color.red_follow));
        } else {
            button.setText("Follow");
            button.setBackgroundTintList(context.getColorStateList(R.color.purple_500));
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
            tvFollowerCount = itemView.findViewById(R.id.tvUserFollowerCount);
            btnFollow = itemView.findViewById(R.id.btnFollow);
        }
    }
}

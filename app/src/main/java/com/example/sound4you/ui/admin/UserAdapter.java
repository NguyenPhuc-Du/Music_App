package com.example.sound4you.ui.admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sound4you.R;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<UserItem> listUsers;
    public ItemClickListener onUserActionListener;

    public void setOnUserActionListener(ItemClickListener onUserActionListener) {
        this.onUserActionListener = onUserActionListener;
    }

    public UserAdapter(List<UserItem> listUsers) {
        this.listUsers = listUsers;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_user, parent, false);

        return new UserViewHolder(view, onUserActionListener);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        UserItem userItem = listUsers.get(position);

        String avatarUrl = userItem.getUserProfilePicture();

        if (avatarUrl == null || avatarUrl.trim().isEmpty() || avatarUrl.equals("NULL")) {
            Glide.with(holder.itemView.getContext())
                    .load(R.drawable.ic_logo) // ảnh mặc định
                    .into(holder.logoUser);
        } else {
            Glide.with(holder.itemView.getContext())
                    .load(avatarUrl)
                    .placeholder(R.drawable.ic_logo)
                    .error(R.drawable.ic_logo)
                    .into(holder.logoUser);
        }

        holder.nameUser.setText(userItem.getUserName());
        holder.emailUser.setText(userItem.getEmailTitle());

        String role = "";
        if (userItem.getRole() == 1) {
            role = "admin";
        } else if (userItem.getRole() == 2) {
            role = "user";
        } else {
            role = "Chưa được phân quyền";
        }

        holder.roleUser.setText(role);
    }

    @Override
    public int getItemCount() {
        return listUsers.size();
    }

    public int getAdapterPositionFromItem(int position) {
        return position;
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        ImageView logoUser;
        TextView nameUser, emailUser, roleUser;
        Button deleteBtn;

        public UserViewHolder(@NonNull View itemView, final ItemClickListener onUserActionListener) {
            super(itemView);
            logoUser = itemView.findViewById(R.id.logo_user);
            nameUser = itemView.findViewById(R.id.name_title);
            emailUser = itemView.findViewById(R.id.email_title);
            roleUser = itemView.findViewById(R.id.status_user);

            deleteBtn = itemView.findViewById(R.id.delete_user_btn);

            deleteBtn.setOnClickListener(v -> {
                if (onUserActionListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onUserActionListener.onDeleteClick(position);
                    }
                }
            });
        }
    }

}

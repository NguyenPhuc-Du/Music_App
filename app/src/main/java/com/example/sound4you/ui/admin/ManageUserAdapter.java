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
import com.example.sound4you.data.model.User;

import java.util.ArrayList;
import java.util.List;

public class ManageUserAdapter extends RecyclerView.Adapter<ManageUserAdapter.UserViewHolder> {
    private final List<User> listUsers;
    private final UserActionListener listener;

    public interface UserActionListener {
        void onDeleteClicked(int userId);
    }

    public ManageUserAdapter(List<User> listUsers, UserActionListener listener) {
        this.listUsers = listUsers != null ? listUsers : new ArrayList<>();
        this.listener = listener;
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = listUsers.get(position);

        String avatarUrl = user.getProfile_picture();

        if (avatarUrl == null || avatarUrl.trim().isEmpty() || "NULL".equalsIgnoreCase(avatarUrl)) {
            Glide.with(holder.itemView.getContext()).load(R.drawable.ic_logo).into(holder.logoUser);
        } else {
            Glide.with(holder.itemView.getContext()).load(avatarUrl).placeholder(R.drawable.ic_logo).error(R.drawable.ic_logo).into(holder.logoUser);
        }

        holder.nameUser.setText(user.getUsername() != null ? user.getUsername() : "Unknown");
        holder.emailUser.setText(user.getEmail() != null ? user.getEmail() : "");
        String role = user.getRole() == 1 ? "admin" : user.getRole() == 2 ? "user" : "Chưa được phân quyền";
        holder.roleUser.setText(role);

        holder.deleteBtn.setOnClickListener(v -> {
            if (listener != null && user != null) listener.onDeleteClicked(user.getId());
        });
    }

    @Override
    public long getItemId(int position) {
        try { return listUsers.get(position) != null ? listUsers.get(position).getId() : position; }
        catch (Exception e) { return position; }
    }

    @Override
    public int getItemCount() {
        return listUsers.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        ImageView logoUser;
        TextView nameUser, emailUser, roleUser;
        Button deleteBtn;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            logoUser = itemView.findViewById(R.id.logo_user);
            nameUser = itemView.findViewById(R.id.name_title);
            emailUser = itemView.findViewById(R.id.email_title);
            roleUser = itemView.findViewById(R.id.status_user);
            deleteBtn = itemView.findViewById(R.id.delete_user_btn);
        }
    }

}
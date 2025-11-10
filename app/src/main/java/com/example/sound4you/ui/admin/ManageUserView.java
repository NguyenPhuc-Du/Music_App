package com.example.sound4you.ui.admin;

import com.example.sound4you.data.model.User;
import java.util.List;

public interface ManageUserView {
    void onUsersLoaded(List<User> users);
    void onUserDeleted(int id);
    void onError(String msg);
}

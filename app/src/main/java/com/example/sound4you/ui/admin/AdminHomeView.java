package com.example.sound4you.ui.admin;

public interface AdminHomeView {
    void showPendingCount(int count);
    void showApprovedCount(int count);
    void showUserCount(int count);
    void onError(String msg);
}

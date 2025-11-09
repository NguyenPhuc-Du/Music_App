package com.example.sound4you.ui.auth;

public interface AuthView {
    void showLoading();
    void hideLoading();
    void onSuccess(String message);
    void onError(String error);
}

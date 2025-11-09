package com.example.sound4you.presenter.auth;

public interface PasswordForgettingPresenter {
    void sendVerificationCode(String email);
    void resetPassword(String email, String code, String newPassword);
}

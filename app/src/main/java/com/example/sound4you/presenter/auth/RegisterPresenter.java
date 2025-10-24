package com.example.sound4you.presenter.auth;

public interface RegisterPresenter {
    void register(String username, String email, String password, String code);
    void sendVerificationCode(String email);
}

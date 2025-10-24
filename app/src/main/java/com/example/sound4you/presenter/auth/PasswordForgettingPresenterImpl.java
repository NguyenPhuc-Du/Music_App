package com.example.sound4you.presenter.auth;

import android.content.Context;
import com.example.sound4you.data.model.Email;
import com.example.sound4you.data.model.User;
import com.example.sound4you.data.repository.AuthRepository;
import com.example.sound4you.data.repository.EmailRepository;
import com.example.sound4you.ui.auth.AuthView;

import java.util.HashMap;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PasswordForgettingPresenterImpl implements PasswordForgettingPresenter{
    private AuthView authView;
    private EmailRepository emailRepository;
    private AuthRepository authRepository;

    public PasswordForgettingPresenterImpl(AuthView authview, Context context) {
        this.authView = authview;
        this.emailRepository = new EmailRepository(context);
        this.authRepository = new AuthRepository(context);
    }

    @Override
    public void sendVerificationCode(String email) {
        authView.showLoading();
        Email emailObj = new Email(email, "resetPassword");
        emailRepository.sendCode(emailObj, new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                authView.hideLoading();
                if (response.isSuccessful()) {
                    authView.onSuccess("Mã xác minh đã được gửi đến email của bạn.");
                }
                else {
                    authView.onError("Gửi mã xác minh thất bại! Vui lòng thử lại.");
                }
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                authView.hideLoading();
                authView.onError("Lỗi mạng");
            }
        });
    }

    @Override
    public void resetPassword(String email, String code, String newPassword) {
        authView.showLoading();
        Email emailObj = new Email(email, code, "resetPassword");
        emailRepository.verifyCode(emailObj, new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                if (response.isSuccessful()) {
                    Map<String, String> data = new HashMap<>();
                    data.put("email", email);
                    data.put("newPassword", newPassword);
                    authRepository.resetPassword(data, new Callback<Map<String, String>>() {
                        @Override
                        public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                            authView.hideLoading();
                            if (response.isSuccessful()) {
                                authView.onSuccess("Đặt lại mật khẩu thành công.");
                            }
                            else {
                                authView.onError("Đặt lại mật khẩu thất bại! Vui lòng thử lại.");
                            }
                        }

                        @Override
                        public void onFailure(Call<Map<String, String>> call, Throwable t) {
                            authView.hideLoading();
                            authView.onError("Lỗi mạng");
                        }
                    });
                }
                else {
                    authView.hideLoading();
                    authView.onError("Mã xác minh không hợp lệ hoặc đã hết hạn.");
                }
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                authView.hideLoading();
                authView.onError("Lỗi mạng");
            }
        });
    }
}

package com.example.sound4you.presenter.auth;

import android.content.Context;

import com.example.sound4you.data.model.User;
import com.example.sound4you.data.model.Email;
import com.example.sound4you.data.repository.AuthRepository;
import com.example.sound4you.data.repository.EmailRepository;
import com.example.sound4you.ui.auth.AuthView;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterPresenterImpl implements RegisterPresenter {
    private final AuthView authView;
    private final AuthRepository authRepository;
    private final EmailRepository emailRepository;
    private final Context context;

    public RegisterPresenterImpl(AuthView authView, Context context) {
        this.authView = authView;
        this.authRepository = new AuthRepository(context);
        this.emailRepository = new EmailRepository(context);
        this.context = context;
    }

    @Override
    public void register(String username, String email, String password, String code) {
        authView.showLoading();

        Email emailObj = new Email(email, code, "register");

        emailRepository.verifyCode(emailObj, new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> res) {
                if (!res.isSuccessful()) {
                    authView.hideLoading();
                    authView.onError("Mã xác minh không hợp lệ!");
                    return;
                }

                User user = new User(username, email, password);
                authRepository.register(user, new Callback<Map<String, String>>() {
                    @Override
                    public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> res2) {
                        authView.hideLoading();
                        if (res2.isSuccessful()) {
                            authView.onSuccess("Đăng ký thành công!");
                        } else {
                            authView.onError("Lỗi server!");
                        }
                    }

                    @Override
                    public void onFailure(Call<Map<String, String>> call, Throwable t) {
                        authView.hideLoading();
                        authView.onError("Lỗi mạng!");
                    }
                });
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                authView.hideLoading();
                authView.onError("Lỗi mạng!");
            }
        });
    }

    @Override
    public void sendVerificationCode(String email) {
        authView.showLoading();

        Email emailObj = new Email(email, "register");
        emailRepository.sendCode(emailObj, new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                authView.hideLoading();
                if (!response.isSuccessful()) {
                    authView.onError("Gửi mã xác minh thất bại! Vui lòng thử lại.");
                }
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                authView.hideLoading();
                authView.onError("Không thể kết nối đến máy chủ.");
            }
        });
    }
}

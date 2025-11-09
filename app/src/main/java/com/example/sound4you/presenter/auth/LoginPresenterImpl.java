package com.example.sound4you.presenter.auth;

import android.content.Context;

import com.example.sound4you.data.model.User;
import com.example.sound4you.data.repository.AuthRepository;
import com.example.sound4you.ui.auth.AuthView;

import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginPresenterImpl implements LoginPresenter{
    private final AuthView authView;
    private final AuthRepository authRepository;

    public LoginPresenterImpl(AuthView authView, Context context) {
        this.authView = authView;
        this.authRepository = new AuthRepository(context);
    }

    @Override
    public void login(String email, String password) {
        authView.showLoading();
        User user = new User(email, password);

        authRepository.login(user, new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                authView.hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    Map<String, Object> data = response.body();
                    Object token = data.get("token");
                    Map<String, Object> userData = (Map<String, Object>) data.get("user");

                    if (token != null && userData != null) {
                        int role = ((int) userData.get("role"));
                        authRepository.saveToken(token.toString());
                        authRepository.saveUserRole(role);

                        if (role == 1) {
                            authView.onSuccess("admin");
                        }
                        else {
                            authView.onSuccess("user");
                        }
                    }
                    else {
                        authView.onError("Đăng nhập thất bại!");
                    }
                }
                else {
                    authView.onError("Sai email hoặc mật khẩu");
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                authView.hideLoading();
                authView.onError("Lỗi mạng");
            }
        });
    }
}

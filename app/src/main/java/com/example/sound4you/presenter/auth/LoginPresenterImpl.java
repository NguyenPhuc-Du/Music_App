package com.example.sound4you.presenter.auth;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.sound4you.data.model.User;
import com.example.sound4you.data.repository.AuthRepository;
import com.example.sound4you.ui.auth.AuthView;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginPresenterImpl implements LoginPresenter {
    private final AuthView authView;
    private final AuthRepository authRepository;
    private final Context context;
    public LoginPresenterImpl(AuthView authView, Context context) {
        this.authView = authView;
        this.authRepository = new AuthRepository(context);
        this.context = context;
    }

    @Override
    public void login(String email, String password) {
        authView.showLoading();

        User user = new User(email, password);
        authRepository.login(user, new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> res) {
                authView.hideLoading();

                if (!res.isSuccessful() || res.body() == null) {
                    authView.onError("Sai email hoặc mật khẩu!");
                    return;
                }

                Map<String, Object> data = res.body();

                String token = (String) data.get("token");
                Map<String, Object> userData = (Map<String, Object>) data.get("user");

                int userId = ((Double) userData.get("id")).intValue();
                int roleInt = ((Double) userData.get("role")).intValue();
                String role = roleInt == 1 ? "admin" : "user";

                SharedPreferences prefs = context.getSharedPreferences("AuthPreferences", Context.MODE_PRIVATE);
                prefs.edit()
                        .putString("Token", token)
                        .putInt("UserId", userId)
                        .putString("Role", role)
                        .apply();

                authView.onSuccess(role);
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                authView.hideLoading();
                authView.onError("Lỗi mạng, vui lòng thử lại");
            }
        });
    }
}

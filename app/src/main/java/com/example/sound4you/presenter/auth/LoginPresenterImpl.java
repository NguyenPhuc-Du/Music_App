package com.example.sound4you.presenter.auth;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.sound4you.data.model.User;
import com.example.sound4you.data.repository.AuthRepository;
import com.example.sound4you.ui.auth.AuthView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginPresenterImpl implements LoginPresenter {
    private final AuthView authView;
    private final AuthRepository authRepository;
    private final Context context;
    private final FirebaseAuth firebaseAuth;

    public LoginPresenterImpl(AuthView authView, Context context) {
        this.authView = authView;
        this.authRepository = new AuthRepository(context);
        this.context = context;
        this.firebaseAuth = FirebaseAuth.getInstance();
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
                    String token = data.get("token").toString();
                    Map<String, Object> userData = (Map<String, Object>) data.get("user");
                    int role = ((Double) userData.get("role")).intValue();

                    SharedPreferences prefs = context.getSharedPreferences("AuthPreferences", Context.MODE_PRIVATE);
                    prefs.edit()
                            .putString("Token", token)
                            .putString("Email", email)
                            .putString("Role", role == 1 ? "admin" : "user")
                            .apply();

                    authRepository.saveToken(token);
                    authRepository.saveUserRole(role);

                    authView.onSuccess(role == 1 ? "admin" : "user");
                } else {
                    authView.onError("Sai email hoặc mật khẩu!");
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                authView.hideLoading();
                authView.onError("Lỗi mạng, vui lòng thử lại sau!");
            }
        });
    }
}

package com.example.sound4you.presenter.auth;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.sound4you.data.model.User;
import com.example.sound4you.data.repository.AuthRepository;
import com.example.sound4you.ui.auth.AuthView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
                    Object tokenObj = data.get("token");
                    Map<String, Object> userData = (Map<String, Object>) data.get("user");

                    if (tokenObj != null && userData != null) {
                        String token = tokenObj.toString();
                        Object roleObj = userData.get("role");
                        int role = 0;
                        if (roleObj instanceof Double) {
                            role = ((Double) roleObj).intValue();
                        } else if (roleObj instanceof Integer) {
                            role = (Integer) roleObj;
                        }

                        int finalRole = role;

                        firebaseAuth.signInWithEmailAndPassword(email, password)
                                .addOnSuccessListener((AuthResult result) -> {
                                    FirebaseUser firebaseUser = result.getUser();
                                    if (firebaseUser != null) {
                                        String uid = firebaseUser.getUid();

                                        SharedPreferences prefs = context.getSharedPreferences("AuthPreferences", Context.MODE_PRIVATE);
                                        prefs.edit()
                                                .putString("Token", token)
                                                .putString("UID", uid)
                                                .putString("Role", finalRole == 1 ? "admin" : "user")
                                                .apply();

                                        authRepository.saveToken(token);
                                        authRepository.saveUserRole(finalRole);

                                        if (finalRole == 1) {
                                            authView.onSuccess("admin");
                                        } else {
                                            authView.onSuccess("user");
                                        }
                                    } else {
                                        authView.onError("Không thể xác thực Firebase!");
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    authView.onError("Sai email hoặc mật khẩu (Firebase)!");
                                });
                    } else {
                        authView.onError("Đăng nhập thất bại! Thiếu dữ liệu phản hồi.");
                    }
                } else {
                    authView.onError("Sai email hoặc mật khẩu (Server)!");
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

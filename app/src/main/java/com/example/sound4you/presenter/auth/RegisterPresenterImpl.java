package com.example.sound4you.presenter.auth;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.sound4you.data.model.User;
import com.example.sound4you.data.model.Email;
import com.example.sound4you.data.repository.AuthRepository;
import com.example.sound4you.data.repository.EmailRepository;
import com.example.sound4you.ui.auth.AuthView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterPresenterImpl implements RegisterPresenter {
    private final AuthView authView;
    private final AuthRepository authRepository;
    private final EmailRepository emailRepository;
    private final Context context;
    private final FirebaseAuth firebaseAuth;

    public RegisterPresenterImpl(AuthView authView, Context context) {
        this.authView = authView;
        this.authRepository = new AuthRepository(context);
        this.emailRepository = new EmailRepository(context);
        this.context = context;
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void register(String username, String email, String password, String code) {
        authView.showLoading();

        Email emailObj = new Email(email, code, "register");
        emailRepository.verifyCode(emailObj, new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                if (response.isSuccessful()) {
                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                            .addOnSuccessListener(authResult -> {
                                FirebaseUser firebaseUser = authResult.getUser();
                                if (firebaseUser == null) {
                                    authView.hideLoading();
                                    authView.onError("Không thể tạo tài khoản Firebase!");
                                    return;
                                }

                                String uid = firebaseUser.getUid();

                                SharedPreferences prefs = context.getSharedPreferences("AuthPreferences", Context.MODE_PRIVATE);
                                prefs.edit().putString("UID", uid).apply();

                                User user = new User(username, email, password);
                                user.setFirebaseUid(uid);

                                authRepository.register(user, new Callback<Map<String, String>>() {
                                    @Override
                                    public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                                        authView.hideLoading();
                                        if (response.isSuccessful()) {
                                            authView.onSuccess("Đăng ký thành công!");
                                        } else {
                                            authView.onError("Lỗi lưu thông tin lên server!");
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Map<String, String>> call, Throwable t) {
                                        authView.hideLoading();
                                        authView.onError("Lỗi mạng khi gửi dữ liệu lên server!");
                                    }
                                });
                            })
                            .addOnFailureListener(e -> {
                                authView.hideLoading();
                                authView.onError("Tạo tài khoản Firebase thất bại: " + e.getMessage());
                            });
                } else {
                    authView.hideLoading();
                    authView.onError("Mã xác minh không hợp lệ hoặc đã hết hạn.");
                }
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                authView.hideLoading();
                authView.onError("Lỗi mạng khi xác minh mã.");
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

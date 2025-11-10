package com.example.sound4you.ui.auth;

import static com.example.sound4you.utils.Validator.isStrongPassword;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.content.Intent;

import com.example.sound4you.R;
import com.example.sound4you.presenter.auth.PasswordForgettingPresenterImpl;

public class PasswordForgettingFragment extends Fragment implements AuthView{
    private EditText emailInput;
    private EditText codeInput;
    private EditText newPasswordInput;
    private Button sendCodeButton;
    private Button resetPasswordButton;

    private PasswordForgettingPresenterImpl presenter;

    public PasswordForgettingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_password_forgetting, container, false);
        emailInput = view.findViewById(R.id.emailValueLogin);
        codeInput = view.findViewById(R.id.codeValueForgotPassword);
        newPasswordInput = view.findViewById(R.id.newPasswordInput);
        sendCodeButton = view.findViewById(R.id.btntakeKeyForgotPassword);
        resetPasswordButton = view.findViewById(R.id.btnResetPassword);

        presenter = new PasswordForgettingPresenterImpl(this, requireContext());
        sendCodeButton.setOnClickListener(r -> {
            String email = emailInput.getText().toString();
            if (email.isEmpty()) {
                onError("Vui lòng nhập email");
                return;
            }

            presenter.sendVerificationCode(email);
        });

        resetPasswordButton.setOnClickListener(r -> {
            String email = emailInput.getText().toString();
            String code = codeInput.getText().toString();
            String newPassword = newPasswordInput.getText().toString();
            if (email.isEmpty() || code.isEmpty() || newPassword.isEmpty()) {
                onError("Vui lòng nhập đầy đủ thông tin");
                return;
            }

            if (isStrongPassword(newPassword)) {
                Toast.makeText(getContext(), "Mật khẩu phải có ít nhất 6 ký tự, bao gồm chữ hoa, chữ thường và số", Toast.LENGTH_SHORT).show();
                return;
            }

            presenter.resetPassword(email, code, newPassword);
        });

        return view;
    }

    @Override
    public void showLoading() {}

    @Override
    public void hideLoading() {}

    @Override
    public void onSuccess(String message) {
        startActivity(new Intent(requireContext(), AuthActivity.class));
        requireActivity().finish();
    }

    @Override
    public void onError(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();
    }
}
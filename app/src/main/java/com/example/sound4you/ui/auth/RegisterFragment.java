package com.example.sound4you.ui.auth;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.widget.Button;
import android.widget.Toast;
import android.widget.EditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sound4you.MainActivity;
import com.example.sound4you.R;
import com.example.sound4you.presenter.auth.RegisterPresenterImpl;

public class RegisterFragment extends Fragment implements AuthView{
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText usernameEditText;
    private EditText codeEditText;
    private Button registerButton;
    private Button getCodeButton;
    private RegisterPresenterImpl presenter;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        emailEditText = view.findViewById(R.id.emailInput);
        usernameEditText = view.findViewById(R.id.usernameInput);
        passwordEditText = view.findViewById(R.id.passwordInput);
        codeEditText = view.findViewById(R.id.codeValue);
        registerButton = view.findViewById(R.id.registerButton);
        getCodeButton = view.findViewById(R.id.btnTakeVerificationCode);

        presenter = new RegisterPresenterImpl(this, requireContext());
        getCodeButton.setOnClickListener(r -> presenter.sendVerificationCode(emailEditText.getText().toString().trim()));

        registerButton.setOnClickListener(r -> {
            String username = usernameEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String code = codeEditText.getText().toString().trim();

            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || code.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            presenter.register(username, email, password, code);
        });

        return view;
    }

    @Override
    public void showLoading() {}

    @Override
    public void hideLoading() {}

    @Override
    public void onSuccess(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getContext(), MainActivity.class));
        requireActivity().finish();
    }

    @Override
    public void onError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
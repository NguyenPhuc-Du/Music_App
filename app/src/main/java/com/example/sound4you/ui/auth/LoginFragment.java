package com.example.sound4you.ui.auth;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.fragment.NavHostFragment;

import com.example.sound4you.MainActivity;
import com.example.sound4you.presenter.auth.LoginPresenterImpl;
import com.example.sound4you.R;

import com.google.android.material.textfield.TextInputEditText;

public class LoginFragment extends Fragment implements AuthView{

    private TextInputEditText emailInput;
    private TextInputEditText passwordInput;
    private ProgressBar progressBar;
    private Button loginButton;
    private LoginPresenterImpl presenter;

    public LoginFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);
        emailInput = view.findViewById(R.id.emailInput);
        passwordInput = view.findViewById(R.id.passwordInput);
        loginButton = view.findViewById(R.id.loginButton);
        progressBar = new ProgressBar(requireContext());

        presenter = new LoginPresenterImpl(this, requireContext());
        loginButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString();
            String password = passwordInput.getText().toString();
            presenter.login(email, password);
        });

        TextView tvForgotPassword = view.findViewById(R.id.forgotPassword);
        TextView tvGotoRegister = view.findViewById(R.id.registerAccount);

        tvForgotPassword.setOnClickListener(v-> {
            NavHostFragment.findNavController(this).navigate(R.id.action_loginFragment_to_passwordForgettingFragment);
        });

        tvGotoRegister.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(R.id.action_loginFragment_to_registerFragment);
        });

        return view;
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onSuccess (String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        if (message.equals("admin")) {
//            startActivity(new Intent(getContext(), AdminActivity.class));
//            requireActivity().finish();
        }
        else {
            startActivity(new Intent(getContext(), MainActivity.class));
            requireActivity().finish();
        }
    }

    @Override
    public void onError (String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
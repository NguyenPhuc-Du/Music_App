package com.example.sound4you.utils;

import android.text.TextUtils;
import android.util.Patterns;

public class Validator {

    public static boolean isValidEmail(String email) {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isStrongPassword(String password) {
        if (TextUtils.isEmpty(password)) return false;
        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,}$";
        return password.matches(passwordPattern);
    }

    public static boolean isValidUrl(String url) {
        return url != null && Patterns.WEB_URL.matcher(url).matches();
    }

    public static boolean isNotEmpty(String input) {
        return input != null && !input.trim().isEmpty();
    }
}

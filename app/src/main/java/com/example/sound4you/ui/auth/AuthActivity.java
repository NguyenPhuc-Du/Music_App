package com.example.sound4you.ui.auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sound4you.MainActivity;
import com.example.sound4you.R;
import com.example.sound4you.ui.admin.AdminActivity;

public class AuthActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences("AuthPreferences", MODE_PRIVATE);

        int userId = prefs.getInt("UserId", -1);
        String role = prefs.getString("Role", null);

        if (userId != -1) {
            if ("admin".equals(role)) {
                startActivity(new Intent(this, AdminActivity.class));
            } else {
                startActivity(new Intent(this, MainActivity.class));
            }
            finish();
            return;
        }

        setContentView(R.layout.activity_auth);
    }
}


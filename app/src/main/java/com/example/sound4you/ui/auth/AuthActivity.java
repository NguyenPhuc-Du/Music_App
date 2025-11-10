package com.example.sound4you.ui.auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sound4you.MainActivity;
import com.example.sound4you.R;
import com.example.sound4you.ui.admin.AdminActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        SharedPreferences prefs = getSharedPreferences("AuthPreferences", MODE_PRIVATE);

        String role = prefs.getString("Role", null);

        if (user != null) {
            if ("admin".equals(role)) {
                startActivity(new Intent(this, AdminActivity.class));
            } else {
                startActivity(new Intent(this, MainActivity.class));
            }
            finish();
        }
        else {
            setContentView(R.layout.activity_auth);
        }
    }
}

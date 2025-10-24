package com.example.sound4you.ui.auth;

import android.os.Bundle;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import com.example.sound4you.MainActivity;
import com.example.sound4you.R;

public class AuthActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
    }

    @Override
    protected void onStart() {
        super.onStart();
        String Token = getSharedPreferences("AuthPreferences", MODE_PRIVATE).getString("Token", null);

        if (Token != null && !Token.isEmpty()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }
}

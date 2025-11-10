package com.example.sound4you.ui.auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.sound4you.MainActivity;
import com.example.sound4you.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        SharedPreferences prefs = getSharedPreferences("AuthPreferences", MODE_PRIVATE);

        if (user != null) {
            startActivity(new Intent(this, MainActivity.class));
        } else {
            String uid = prefs.getString("UID", null);
            if (uid != null) {
                auth.signInAnonymously()
                        .addOnSuccessListener(result -> {
                            prefs.edit().putString("UID", result.getUser().getUid()).apply();
                            startActivity(new Intent(this, MainActivity.class));
                        })
                        .addOnFailureListener(Throwable::printStackTrace);
            }
            else {
                setContentView(R.layout.activity_auth);
            }
        }
    }
}

package com.example.sound4you;

import android.app.Application;
import com.google.firebase.FirebaseApp;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory;
// Nếu bạn đang chạy trên emulator (máy ảo), dùng dòng dưới thay thế
// import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Khởi tạo Firebase
        FirebaseApp.initializeApp(this);

        // Khởi tạo App Check Provider
        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
        firebaseAppCheck.installAppCheckProviderFactory(
                PlayIntegrityAppCheckProviderFactory.getInstance()
                // Nếu test trên máy ảo (emulator), dùng dòng dưới thay cho dòng trên:
                // DebugAppCheckProviderFactory.getInstance()
        );
    }
}

package com.example.sound4you.utils;

import androidx.annotation.NonNull;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseUtils {

    public static DatabaseReference getUserRef() {
        return FirebaseDatabase.getInstance().getReference("user");
    }

    public static void saveUserProfile(@NonNull String uid, @NonNull String displayName, @NonNull String avatarUrl) {
        DatabaseReference ref = getUserRef().child(uid);
        ref.child("displayName").setValue(displayName);
        ref.child("avatarUrl").setValue(avatarUrl);
        ref.child("updatedAt").setValue(System.currentTimeMillis());
    }

    public static void clearLastPlayed(@NonNull String uid) {
        getUserRef().child(uid).child("lastPlayed").removeValue();
    }
}

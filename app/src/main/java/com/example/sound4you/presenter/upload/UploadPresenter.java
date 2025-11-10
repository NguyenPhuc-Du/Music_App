package com.example.sound4you.presenter.upload;

import android.net.Uri;

public interface UploadPresenter {
    void uploadTrack(String firebaseUid, String title, String genre, Uri musicUri, Uri coverUri);
}

package com.example.sound4you.presenter.upload;

import android.net.Uri;

public interface UploadPresenter {
    void uploadTrack(int userId, String title, String genre, Uri musicUri, Uri coverUri);
}

package com.example.sound4you.presenter.upload;

import android.content.Context;
import android.net.Uri;
import com.example.sound4you.data.repository.TrackRepository;
import com.example.sound4you.ui.upload.UploadView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadPresenterImpl implements UploadPresenter {
    private final UploadView view;
    private final TrackRepository repo;
    private final Context context;

    public UploadPresenterImpl(Context context, UploadView view) {
        this.view = view;
        this.repo = new TrackRepository();
        this.context = context.getApplicationContext();
    }

    @Override
    public void uploadTrack(String firebaseUid, String title, String genre, Uri musicUri, Uri coverUri) {
        RequestBody uidBody = RequestBody.create(MediaType.parse("text/plain"), firebaseUid == null ? "" : firebaseUid);
        RequestBody titleBody = RequestBody.create(MediaType.parse("text/plain"), title == null ? "" : title);
        RequestBody genreBody = RequestBody.create(MediaType.parse("text/plain"), genre == null ? "" : genre);

        MultipartBody.Part musicPart = null, coverPart = null;

        try {
            if (musicUri != null) {
                File musicFile = uriToFile(musicUri, "music_");
                RequestBody fileReq = RequestBody.create(MediaType.parse("audio/*"), musicFile);
                musicPart = MultipartBody.Part.createFormData("music", musicFile.getName(), fileReq);
            }

            if (coverUri != null) {
                File coverFile = uriToFile(coverUri, "cover_");
                RequestBody fileReq = RequestBody.create(MediaType.parse("image/*"), coverFile);
                coverPart = MultipartBody.Part.createFormData("cover", coverFile.getName(), fileReq);
            }
        } catch (IOException e) {
            view.onUploadError("Không thể đọc file: " + e.getMessage());
            return;
        }

        repo.uploadTrack(uidBody, titleBody, genreBody, musicPart, coverPart, new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) view.onUploadSuccess("Upload thành công!");
                else view.onUploadError("Upload thất bại: " + response.message());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                view.onUploadError("Lỗi mạng: " + t.getMessage());
            }
        });
    }

    private File uriToFile(Uri uri, String prefix) throws IOException {
        InputStream is = context.getContentResolver().openInputStream(uri);
        if (is == null) throw new IOException("Không thể mở URI");
        File out = File.createTempFile(prefix, null, context.getCacheDir());
        try (FileOutputStream fos = new FileOutputStream(out)) {
            byte[] buf = new byte[8192];
            int len;
            while ((len = is.read(buf)) > 0) {
                fos.write(buf, 0, len);
            }
        } finally {
            try { is.close(); } catch (IOException ignored) {}
        }
        return out;
    }

}

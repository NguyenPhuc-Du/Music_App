package com.example.sound4you.presenter.upload;

import android.content.Context;
import android.media.MediaMetadataRetriever;
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

import okhttp3.ResponseBody;
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
    public void uploadTrack(int userId, String title, String genre, Uri musicUri, Uri coverUri) {

        String duration = getDurationFromUri(musicUri);   // ★ NEW: Đọc thời lượng

        RequestBody userIdBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(userId));
        RequestBody titleBody = RequestBody.create(MediaType.parse("text/plain"), title);
        RequestBody genreBody = RequestBody.create(MediaType.parse("text/plain"), genre);
        RequestBody durationBody = RequestBody.create(MediaType.parse("text/plain"), duration); // ★ NEW

        MultipartBody.Part musicPart = null;
        MultipartBody.Part coverPart = null;

        try {
            if (musicUri != null) {
                File f = uriToFile(musicUri, "music_");
                RequestBody body = RequestBody.create(MediaType.parse("audio/*"), f);
                musicPart = MultipartBody.Part.createFormData("music", f.getName(), body);
            }

            if (coverUri != null) {
                File f = uriToFile(coverUri, "cover_");
                RequestBody body = RequestBody.create(MediaType.parse("image/*"), f);
                coverPart = MultipartBody.Part.createFormData("cover", f.getName(), body);
            }

        } catch (IOException e) {
            view.onUploadError("Không thể đọc file: " + e.getMessage());
            return;
        }

        repo.uploadTrack(
                userIdBody, titleBody, genreBody, durationBody, musicPart, coverPart, new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            view.onUploadSuccess("Upload thành công!");
                        } else {
                            view.onUploadError("Upload thất bại");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        view.onUploadError("Lỗi mạng: " + t.getMessage());
                    }
                }
        );
    }

    private File uriToFile(Uri uri, String prefix) throws IOException {
        String ext = getExtensionFromUri(uri);
        if (ext == null) ext = "mp3";   // fallback nếu không xác định được

        File output = new File(context.getCacheDir(), prefix + System.currentTimeMillis() + "." + ext);

        InputStream is = context.getContentResolver().openInputStream(uri);
        FileOutputStream fos = new FileOutputStream(output);

        byte[] buffer = new byte[8192];
        int len;
        while ((len = is.read(buffer)) != -1) {
            fos.write(buffer, 0, len);
        }

        fos.close();
        is.close();
        return output;
    }

    private String getDurationFromUri(Uri uri) {
        try {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(context, uri);
            String dur = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            retriever.release();

            long ms = Long.parseLong(dur);
            long sec = ms / 1000;
            return String.valueOf(sec);

        } catch (Exception e) {
            return "0";
        }
    }

    private String getExtensionFromUri(Uri uri) {
        String ext = null;

        String mime = context.getContentResolver().getType(uri);
        if (mime != null) {
            String[] parts = mime.split("/");
            if (parts.length == 2) {
                ext = parts[1];
            }
        }

        if ("mpeg".equalsIgnoreCase(ext)) {
            return "mp3";
        }

        if (ext == null) {
            String path = uri.getPath();
            if (path != null && path.contains(".")) {
                ext = path.substring(path.lastIndexOf(".") + 1);
            }
        }

        if (ext == null) {
            ext = "mp3";
        }

        return ext;
    }
}

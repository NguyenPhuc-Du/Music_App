package com.example.sound4you.ui.upload;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.*;
import android.widget.*;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.bumptech.glide.Glide;
import com.example.sound4you.R;
import com.example.sound4you.presenter.upload.UploadPresenterImpl;
import com.google.firebase.auth.FirebaseAuth;
import com.example.sound4you.presenter.genre.GenrePresenterImpl;
import com.example.sound4you.ui.genre.GenreView;
import com.example.sound4you.data.model.Genre;

import java.util.ArrayList;
import java.util.List;

public class UploadFragment extends Fragment implements UploadView {

    private Uri musicUri, coverUri;
    private ImageView imgCover;
    private EditText edtTitle;
    private TextView tvGenre;
    private Button btnSave;
    private UploadPresenterImpl presenter;

    private GenrePresenterImpl genrePresenter;
    private List<Genre> genres = new ArrayList<>();
    private Genre selectedGenre = null;

    private final ActivityResultLauncher<Intent> pickMusicLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null && result.getData().getData() != null) {
                    musicUri = result.getData().getData();
                    Toast.makeText(requireContext(), "Đã chọn bài nhạc!", Toast.LENGTH_SHORT).show();
                    showUploadForm();
                } else {
                    NavHostFragment.findNavController(UploadFragment.this).navigateUp();
                }
            });

    private final ActivityResultLauncher<Intent> pickImageLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null && result.getData().getData() != null) {
                    coverUri = result.getData().getData();
                    Glide.with(this).load(coverUri).into(imgCover);
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_upload, container, false);

        presenter = new com.example.sound4you.presenter.upload.UploadPresenterImpl(requireContext(), this);
        imgCover = v.findViewById(R.id.imgCoverUpload);
        edtTitle = v.findViewById(R.id.edtTrackTitle);
        tvGenre = v.findViewById(R.id.tvTrackGenre);
        btnSave = v.findViewById(R.id.btnSaveTrackDetailsUpload);

        genrePresenter = new GenrePresenterImpl(new GenreView() {
            @Override
            public void onGenresLoaded(List<Genre> loaded) {
                genres.clear();
                if (loaded != null) genres.addAll(loaded);
            }

            @Override
            public void onGenreError(String error) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
            }
        });
        genrePresenter.loadGenres();

        tvGenre.setOnClickListener(vv -> {
            if (genres.isEmpty()) {
                Toast.makeText(requireContext(), "Đang tải danh sách thể loại...", Toast.LENGTH_SHORT).show();
                genrePresenter.loadGenres();
                return;
            }
            showGenreDialog();
        });

        Intent pickIntent = new Intent(Intent.ACTION_GET_CONTENT);
        pickIntent.setType("audio/*");
        pickMusicLauncher.launch(Intent.createChooser(pickIntent, "Chọn file nhạc"));

        if (getActivity() instanceof com.example.sound4you.MainActivity) {
            ((com.example.sound4you.MainActivity) getActivity()).hideBottomNav();
        }

        imgCover.setOnClickListener(v1 -> {
            Intent pickImg = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickImageLauncher.launch(pickImg);
        });

        btnSave.setOnClickListener(v2 -> {
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            String title = edtTitle.getText().toString().trim();
            String genre = selectedGenre != null ? selectedGenre.getTitle() : "";

            if (musicUri == null) {
                Toast.makeText(requireContext(), "Vui lòng chọn bài nhạc!", Toast.LENGTH_SHORT).show();
                return;
            }
            presenter.uploadTrack(uid, title, genre, musicUri, coverUri);
        });

        return v;
    }

    private void showUploadForm() {
        View vw = getView();
        if (vw != null) vw.findViewById(R.id.trackDetailsUploadContainer).setVisibility(View.VISIBLE);
    }

    private void showGenreDialog() {
        String[] names = new String[genres.size()];
        for (int i = 0; i < genres.size(); i++) names[i] = genres.get(i).getTitle();

        new android.app.AlertDialog.Builder(requireContext())
                .setTitle("Chọn thể loại")
                .setItems(names, (dialog, which) -> {
                    selectedGenre = genres.get(which);
                    tvGenre.setText(selectedGenre.getTitle());
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    @Override
    public void onUploadSuccess(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
        // return to previous screen after successful upload
        NavHostFragment.findNavController(UploadFragment.this).navigateUp();
    }

    @Override
    public void onUploadError(String error) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (getActivity() instanceof com.example.sound4you.MainActivity) {
            ((com.example.sound4you.MainActivity) getActivity()).showBottomNav();
        }
    }

}

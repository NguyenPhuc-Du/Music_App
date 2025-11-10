package com.example.sound4you.ui.follow;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.*;
import android.widget.*;

import androidx.annotation.*;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.sound4you.R;
import com.example.sound4you.data.model.User;
import com.example.sound4you.presenter.profile.ProfilePresenterImpl;
import com.example.sound4you.ui.profile.ProfileView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

public class EditProfileFragment extends Fragment implements ProfileView {

    private static final int REQUEST_GALLERY = 100;
    private static final int REQUEST_CAMERA = 101;

    private ImageButton btnBack;
    private Button btnSave;
    private ImageView profilePicture;
    private EditText edtUsername, edtBio;

    private Uri selectedImageUri;
    private ProfilePresenterImpl profilePresenter;
    private User currentUser;
    private String firebaseUid;

    private boolean wasNowPlayingVisible = false;

    public static EditProfileFragment newInstance() {
        return new EditProfileFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        View nowPlaying = requireActivity().findViewById(R.id.includedNowPlayingBar);
        wasNowPlayingVisible = nowPlaying.getVisibility() == View.VISIBLE;

        requireActivity().findViewById(R.id.bottomNav).setVisibility(View.GONE);
        nowPlaying.setVisibility(View.GONE);

        btnBack = v.findViewById(R.id.btnBackToProfile);
        btnSave = v.findViewById(R.id.btnSaveProfile);
        profilePicture = v.findViewById(R.id.profilePicture);
        edtUsername = v.findViewById(R.id.edtUsername);
        edtBio = v.findViewById(R.id.edtBio);

        profilePresenter = new ProfilePresenterImpl(this);
        firebaseUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        profilePresenter.loadProfileByFirebase(firebaseUid);

        btnBack.setOnClickListener(v1 -> requireActivity().onBackPressed());

        profilePicture.setOnClickListener(v2 -> showChangeAvatarDialog());

        btnSave.setOnClickListener(v3 -> updateProfile());

        return v;
    }

    private void showChangeAvatarDialog() {
        View dialogView = LayoutInflater.from(requireContext())
                .inflate(R.layout.layout_change_photo_bottom, null);

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .create();

        dialogView.findViewById(R.id.tvOptionCamera).setOnClickListener(v -> {
            dialog.dismiss();
            openCamera();
        });

        dialogView.findViewById(R.id.tvOptionGallery).setOnClickListener(v -> {
            dialog.dismiss();
            openGallery();
        });

        dialogView.findViewById(R.id.tvOptionCancel).setOnClickListener(v -> {
            dialog.dismiss();
            selectedImageUri = null;
            Glide.with(this)
                    .load(R.drawable.ic_avatar_placeholder)
                    .into(profilePicture);
        });

        dialog.show();
    }

    private void openGallery() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, REQUEST_GALLERY);
    }

    private void openCamera() {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == REQUEST_GALLERY && data.getData() != null) {
                selectedImageUri = data.getData();
                Glide.with(this).load(selectedImageUri).into(profilePicture);
            } else if (requestCode == REQUEST_CAMERA && data.getExtras() != null) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                profilePicture.setImageBitmap(photo);
                selectedImageUri = Uri.parse(MediaStore.Images.Media.insertImage(
                        requireContext().getContentResolver(), photo, "profile_" + System.currentTimeMillis(), null
                ));
            }
        }
    }

    private void updateProfile() {
        if (currentUser == null) return;

        String newName = edtUsername.getText().toString().trim();
        String newBio = edtBio.getText().toString().trim();

        if (newName.isEmpty()) {
            edtUsername.setError("Tên không được trống");
            return;
        }

        currentUser.setUsername(newName);
        currentUser.setBio(newBio);

        if (selectedImageUri != null) {
            uploadImageToFirebase(selectedImageUri);
        } else {
            profilePresenter.updateProfileByFirebase(firebaseUid, currentUser);
        }
    }

    private void uploadImageToFirebase(Uri uri) {
        StorageReference ref = FirebaseStorage.getInstance()
                .getReference("profile_images/" + UUID.randomUUID() + ".jpg");

        ref.putFile(uri)
                .addOnSuccessListener(task -> ref.getDownloadUrl().addOnSuccessListener(downloadUri -> {
                    currentUser.setProfile_picture(downloadUri.toString());
                    profilePresenter.updateProfileByFirebase(firebaseUid, currentUser);
                }))
                .addOnFailureListener(e -> {
                    Toast.makeText(requireContext(), "Tải ảnh thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void showLoading() {
        btnSave.setEnabled(false);
    }

    @Override
    public void hideLoading() {
        btnSave.setEnabled(true);
    }

    @Override
    public void onProfileLoaded(User user) {
        this.currentUser = user;

        edtUsername.setText(user.getUsername());
        edtBio.setText(user.getBio());

        Glide.with(this)
                .load(user.getProfile_picture())
                .placeholder(R.drawable.ic_avatar_placeholder)
                .into(profilePicture);
    }

    @Override
    public void onProfileUpdated(User user) {
        Toast.makeText(requireContext(), "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
        requireActivity().onBackPressed();
    }

    @Override
    public void onError(String msg) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        requireActivity().findViewById(R.id.bottomNav).setVisibility(View.VISIBLE);

        if (wasNowPlayingVisible)
            requireActivity().findViewById(R.id.includedNowPlayingBar).setVisibility(View.VISIBLE);
    }
}

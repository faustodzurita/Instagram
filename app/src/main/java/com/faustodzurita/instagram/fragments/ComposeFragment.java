package com.faustodzurita.instagram.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.faustodzurita.instagram.models.Post;
import com.faustodzurita.instagram.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;

import static android.app.Activity.RESULT_OK;

public class ComposeFragment extends Fragment {
    private EditText postDescriptionInput;
    private Button takePhotoButton;
    private ImageView postImageView;
    private Button postPhotoButton;
    private ProgressBar postProgressBar;
    private File photoFile;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_compose, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        postDescriptionInput = view.findViewById(R.id.fragment_compose_post_description_textfield);
        takePhotoButton = view.findViewById(R.id.fragment_compose_take_photo_button);
        postImageView = view.findViewById(R.id.fragment_compose_post_preview);
        postPhotoButton = view.findViewById(R.id.fragment_compose_post_photo_button);
        postProgressBar = view.findViewById(R.id.fragment_compose_progress_bar);

        takePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCamera();
            }
        });

        postPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postProgressBar.setVisibility(ProgressBar.VISIBLE);

                String description = postDescriptionInput.getText().toString();
                ParseUser user = ParseUser.getCurrentUser();
                ParseFile file = new ParseFile(photoFile);

                if (photoFile == null || description.equals("")) {
                    Toast.makeText(getContext(), "No photo or description.", Toast.LENGTH_LONG).show();
                } else {
                    savePost(description, user, file);
                }

                postProgressBar.setVisibility(ProgressBar.INVISIBLE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1034) {
            if (resultCode == RESULT_OK) {
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());

                postImageView.setImageBitmap(takenImage);
            } else {
                Toast.makeText(getContext(), "Please take a photo.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void launchCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        photoFile = getPhotoFileUri("photo.jpg");

        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileprovider", photoFile);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivityForResult(intent, 1034);
        }
    }

    public File getPhotoFileUri(String fileName) {
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "ComposeFragment");

        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.e("ComposeFragment", "Camera issue.");
        }

        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
    }

    private void savePost(String description, ParseUser parseUser, ParseFile parseFile) {
        Post post = new Post();
        post.setDescription(description);
        post.setUser(parseUser);
        post.setImage(parseFile);
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("ComposeFragment","Saving post successful.");

                    postDescriptionInput.setText("");
                    postImageView.setImageResource(0);
                } else {
                    Log.e("ComposeFragment", "Saving post failure.");
                    e.printStackTrace();
                }
            }
        });
    }
}

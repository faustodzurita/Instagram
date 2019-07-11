package com.faustodzurita.instagram.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.faustodzurita.instagram.LoginActivity;
import com.faustodzurita.instagram.Post;
import com.faustodzurita.instagram.PostAdapter;
import com.faustodzurita.instagram.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {
    private Button changePhotoButton;
    private RecyclerView timelineView;
    private Button logoutButton;
    private PostAdapter adapter;
    private List<Post> posts;
    private File photoFile;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        changePhotoButton = view.findViewById(R.id.profile_change_photo_btn);
        timelineView = view.findViewById(R.id.profile_timeline_rv);
        logoutButton = view.findViewById(R.id.profile_logout_btn);

        changePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCamera();
            }
        });

        posts = new ArrayList<>();

        adapter = new PostAdapter(getContext(), posts);

        timelineView.setAdapter(adapter);

        timelineView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        queryPosts();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1034) {
            if (resultCode == RESULT_OK) {
                ParseFile file = new ParseFile(photoFile);

                if (photoFile == null) {
                    Toast.makeText(getContext(), "No photo or description.", Toast.LENGTH_LONG).show();
                } else {
                    saveProfilePhoto(file);
                }
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
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "ProfileFragment");

        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.e("ProfileFragment", "Camera issue.");
        }

        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
    }

    private void saveProfilePhoto(ParseFile parseFile) {
        ParseUser user = ParseUser.getCurrentUser();
        user.put("profile", parseFile);
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("ProfileFragment","Saving profile photo successful.");
                } else {
                    Log.e("ProfileFragment", "Saving profile photo failure.");
                    e.printStackTrace();
                }
            }
        });
    }

    private void queryPosts() {
        ParseQuery<Post> postQuery = new ParseQuery<Post>(Post.class);
        postQuery.include(Post.KEY_USER);
        postQuery.setLimit(20);
        postQuery.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
        postQuery.addDescendingOrder(Post.KEY_CREATED_AT);
        postQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> newPosts, ParseException e) {
                if (e == null) {
                    Log.d("ProfileFragment","Post query successful.");

                    posts.addAll(newPosts);
                    adapter.notifyDataSetChanged();

                    for (int i = 0; i < newPosts.size(); i++) {
                        Post post = newPosts.get(i);
                        Log.d("ProfileFragment", "Post: " + post.getDescription() + ", Username: " + post.getUser().getUsername());
                    }
                } else {
                    Log.e("ProfileFragment", "Post query failure.");
                    e.printStackTrace();
                }
            }
        });
    }

    private void logout() {
        ParseUser.logOut();

        final Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}

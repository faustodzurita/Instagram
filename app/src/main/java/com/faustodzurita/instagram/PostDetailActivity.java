package com.faustodzurita.instagram;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.faustodzurita.instagram.models.Post;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.util.ArrayList;

public class PostDetailActivity extends AppCompatActivity {
    private TextView usernameText;
    private ImageView postProfileImageView;
    private ImageView postImageView;
    private TextView postDescriptionText;
    private TextView postTimestampText;
    private ImageView postLikeIcon;
    private TextView postLikeCounter;
    private ImageView postCommentIcon;
    private TextView postCommentText;
    private EditText postCommentInput;
    private Button postCommentButton;
    private Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        usernameText = findViewById(R.id.activity_post_detail_username_text);
        postProfileImageView = findViewById(R.id.activity_post_detail_profile_image);
        postImageView = findViewById(R.id.activity_post_detail_post_image);
        postDescriptionText = findViewById(R.id.activity_post_detail_post_description_text);
        postTimestampText = findViewById(R.id.activity_post_detail_timestamp_text);
        postLikeIcon = findViewById(R.id.activity_post_detail_like_icon);
        postLikeCounter = findViewById(R.id.activity_post_detail_like_counter_text);
        postCommentIcon = findViewById(R.id.activity_post_detail_comment_icon);
        postCommentText = findViewById(R.id.activity_post_detail_comments_text);
        postCommentInput = findViewById(R.id.activity_post_detail_comment_textfield);
        postCommentButton = findViewById(R.id.activity_post_detail_comment_submit_button);

        post = Parcels.unwrap(getIntent().getParcelableExtra("post"));

        usernameText.setText(post.getUser().getUsername());

        ParseFile profileImage = post.getUser().getParseFile("profile");
        if (profileImage != null) {
            Glide.with(this).load(profileImage.getUrl()).apply(RequestOptions.circleCropTransform()).into(postProfileImageView);
        }

        ParseFile image = post.getImage();
        if (image != null) {
            Glide.with(this).load(image.getUrl()).into(postImageView);
        }

        postDescriptionText.setText(post.getDescription());
        postTimestampText.setText(Post.getRelativeTimeAgo(post.getCreatedAtTime().toString()));
        postLikeCounter.setText(post.getLikes().size() + " likes");
        postCommentText.setText(generateCommentString(post.getComments()));

        if (Post.isLiked(post, ParseUser.getCurrentUser())) {
            postLikeIcon.setImageResource(R.drawable.ufi_heart_active);
        } else {
            postLikeIcon.setImageResource(R.drawable.ufi_heart);
        }
        postLikeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Post.isLiked(post, ParseUser.getCurrentUser())) {
                    ArrayList<String> likes = post.getLikes();
                    likes.remove(ParseUser.getCurrentUser().getUsername());
                    post.setLikes(likes);
                    post.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Log.d("PostDetailActivity","Saving unlike successful.");

                                postLikeIcon.setImageResource(R.drawable.ufi_heart);
                                postLikeCounter.setText(post.getLikes().size() + " likes");
                            } else {
                                Log.e("PostDetailActivity", "Saving unlike failure.");
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    ArrayList<String> likes = post.getLikes();
                    likes.add(ParseUser.getCurrentUser().getUsername());
                    post.setLikes(likes);
                    post.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Log.d("PostDetailActivity","Saving like successful.");

                                postLikeIcon.setImageResource(R.drawable.ufi_heart_active);
                                postLikeCounter.setText(post.getLikes().size() + " likes");
                            } else {
                                Log.e("PostDetailActivity", "Saving like failure.");
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });

        postCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = ParseUser.getCurrentUser().getUsername();
                String delimiter = ": ";
                String comment = postCommentInput.getText().toString();

                ArrayList<String> comments = post.getComments();
                comments.add(username + delimiter + comment);
                post.setComments(comments);
                post.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Log.d("PostDetailActivity","Saving comment successful.");

                            postCommentText.setText(generateCommentString(post.getComments()));
                            postCommentInput.setText("");
                        } else {
                            Log.e("PostDetailActivity", "Saving comment failure.");
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private String generateCommentString(ArrayList<String> comments) {
        String commentString = "";

        for (int i = 0; i < comments.size(); i++) {
            commentString += comments.get(i) + "\n";
        }

        return commentString;
    }
}
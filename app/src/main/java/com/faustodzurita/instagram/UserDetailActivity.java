package com.faustodzurita.instagram;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class UserDetailActivity extends AppCompatActivity {
    private ImageView userProfileImageView;
    private TextView userUsernameText;
    private RecyclerView userTimelineView;
    private PostAdapter adapter;
    private List<Post> posts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        userProfileImageView = findViewById(R.id.user_detail_profile_image_view);
        userUsernameText = findViewById(R.id.user_detail_username_et);
        userTimelineView = findViewById(R.id.user_detail_timeline_rv);

        ParseUser user = Parcels.unwrap(getIntent().getParcelableExtra("user"));

        userUsernameText.setText(user.getUsername());

        ParseFile profileImage = user.getParseFile("profile");
        if (profileImage != null) {
            Glide.with(this).load(profileImage.getUrl()).into(userProfileImageView);
        }

        posts = new ArrayList<>();

        adapter = new PostAdapter(this, posts);

        userTimelineView.setAdapter(adapter);

        userTimelineView.setLayoutManager(new GridLayoutManager(this, 3));

        queryPosts(user);
    }

    private void queryPosts(ParseUser user) {
        ParseQuery<Post> postQuery = new ParseQuery<Post>(Post.class);
        postQuery.include(Post.KEY_USER);
        postQuery.setLimit(20);
        postQuery.whereEqualTo(Post.KEY_USER, user);
        postQuery.addDescendingOrder(Post.KEY_CREATED_AT);
        postQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> newPosts, ParseException e) {
                if (e == null) {
                    Log.d("UserDetailActivity","Post query successful.");

                    posts.addAll(newPosts);
                    adapter.notifyDataSetChanged();

                    for (int i = 0; i < newPosts.size(); i++) {
                        Post post = newPosts.get(i);
                        Log.d("UserDetailActivity", "Post: " + post.getDescription() + ", Username: " + post.getUser().getUsername());
                    }
                } else {
                    Log.e("UserDetailActivity", "Post query failure.");
                    e.printStackTrace();
                }
            }
        });
    }
}

package com.faustodzurita.instagram.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.faustodzurita.instagram.extras.EndlessRecyclerViewScrollListener;
import com.faustodzurita.instagram.models.Post;
import com.faustodzurita.instagram.extras.PostAdapter;
import com.faustodzurita.instagram.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class TimelineFragment extends Fragment {
    private EndlessRecyclerViewScrollListener timelineScrollListener;
    private SwipeRefreshLayout timelineSwipeFrame;
    private RecyclerView timelineView;
    private PostAdapter adapter;
    private List<Post> posts;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timeline, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        timelineSwipeFrame = view.findViewById(R.id.fragment_timeline_swipe_frame);
        timelineView = view.findViewById(R.id.fragment_timeline);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        timelineScrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadNextData(page);
            }
        };
        timelineView.addOnScrollListener(timelineScrollListener);

        timelineSwipeFrame.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.clear();
                timelineScrollListener.resetState();
                queryPosts();
                timelineSwipeFrame.setRefreshing(false);
            }
        });
        timelineSwipeFrame.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        posts = new ArrayList<>();
        adapter = new PostAdapter(getContext(), posts);
        timelineView.setAdapter(adapter);
        timelineView.setLayoutManager(layoutManager);

        queryPosts();
    }

    @Override
    public void onResume() {
        super.onResume();

        adapter.clear();
        timelineScrollListener.resetState();
        queryPosts();
    }

    private void queryPosts() {
        ParseQuery<Post> postQuery = new ParseQuery<Post>(Post.class);
        postQuery.include(Post.KEY_USER);
        postQuery.setLimit(20);
        postQuery.setSkip(0);
        postQuery.addDescendingOrder(Post.KEY_CREATED_AT);
        postQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> newPosts, ParseException e) {
                if (e == null) {
                    Log.d("TimelineFragment","Post query successful.");

                    posts.addAll(newPosts);
                    adapter.notifyDataSetChanged();

                    for (int i = 0; i < newPosts.size(); i++) {
                        Post post = newPosts.get(i);
                        Log.d("TimelineFragment", "Post: " + post.getDescription() + ", Username: " + post.getUser().getUsername() + ", Created At: " + post.getCreatedAtTime().toString());
                    }
                } else {
                    Log.e("TimelineFragment", "Post query failure.");
                    e.printStackTrace();
                }
            }
        });
    }

    private void loadNextData(int offset) {
        if (offset > 1) {
            ParseQuery<Post> postQuery = new ParseQuery<Post>(Post.class);
            postQuery.include(Post.KEY_USER);
            postQuery.setLimit(20);
            postQuery.setSkip((offset - 1) * 20);
            postQuery.addDescendingOrder(Post.KEY_CREATED_AT);
            postQuery.findInBackground(new FindCallback<Post>() {
                @Override
                public void done(List<Post> newPosts, ParseException e) {
                    if (e == null) {
                        Log.d("TimelineFragment","Post query updates successful.");

                        posts.addAll(newPosts);
                        adapter.notifyDataSetChanged();

                        for (int i = 0; i < newPosts.size(); i++) {
                            Post post = newPosts.get(i);
                            Log.d("TimelineFragment", "Post: " + post.getDescription() + ", Username: " + post.getUser().getUsername() + ", Created At: " + post.getCreatedAtTime().toString());
                        }
                    } else {
                        Log.e("TimelineFragment", "Post query updates failure.");
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
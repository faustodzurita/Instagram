package com.faustodzurita.instagram;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private Context context;
    private List<Post> posts;

    public PostAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Post> newPosts) {
        posts.addAll(newPosts);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView usernameText;
        private ImageView postProfileImageView;
        private ImageView postImageView;
        private TextView postDescriptionText;
        private TextView postTimestampText;

        public ViewHolder(View itemView) {
            super(itemView);

            usernameText = itemView.findViewById(R.id.post_username_tv);
            postProfileImageView = itemView.findViewById(R.id.post_profile_image_view);
            postImageView = itemView.findViewById(R.id.post_image_view);
            postDescriptionText = itemView.findViewById(R.id.post_description_tv);
            postTimestampText = itemView.findViewById(R.id.post_timestamp_tv);

            itemView.setOnClickListener(this);

            postProfileImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION) {
                        ParseUser user = posts.get(position).getUser();
                        Intent intent = new Intent(context, UserDetailActivity.class);
                        intent.putExtra("user", Parcels.wrap(user));
                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();

            if (position != RecyclerView.NO_POSITION) {
                Post post = posts.get(position);
                Intent intent = new Intent(context, PostDetailActivity.class);
                intent.putExtra("post", Parcels.wrap(post));
                context.startActivity(intent);
            }
        }

        public void bind(Post post) {
            usernameText.setText(post.getUser().getUsername());

            ParseFile profileImage = post.getUser().getParseFile("profile");
            if (profileImage != null) {
                Glide.with(context).load(profileImage.getUrl()).into(postProfileImageView);
            }

            ParseFile postImage = post.getImage();
            if (postImage != null) {
                Glide.with(context).load(postImage.getUrl()).into(postImageView);
            }

            postDescriptionText.setText(post.getDescription());
            postTimestampText.setText(Post.getRelativeTimeAgo(post.getCreatedAtTime().toString()));
        }
    }
}

package com.faustodzurita.instagram.models;

import android.text.format.DateUtils;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

@ParseClassName("Post")
public class Post extends ParseObject {
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_USER = "user";
    public static final String KEY_CREATED_AT = "createdAt";
    public static final String KEY_LIKES = "likes";
    public static final String KEY_COMMENTS = "comments";

    public Post() {
    }

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile parseFile) {
        put(KEY_IMAGE, parseFile);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser parseUser) {
        put(KEY_USER, parseUser);
    }

    public Date getCreatedAtTime() { return getCreatedAt(); }

    public void setCreatedAtTime(Date createdAtTime) { put(KEY_CREATED_AT, createdAtTime); }

    public ArrayList getLikes() {
        if (getList(KEY_LIKES) == null) {
            return new ArrayList<String>();
        } else {
            return (ArrayList) getList(KEY_LIKES);
        }
    }

    public void setLikes(ArrayList likes) {
        put(KEY_LIKES, likes);
    }

    public ArrayList getComments() {
        if (getList(KEY_COMMENTS) == null) {
            return new ArrayList<String>();
        } else {
            return (ArrayList) getList(KEY_COMMENTS);
        }
    }

    public void setComments(ArrayList comments) {
        put(KEY_COMMENTS, comments);
    }

    public static boolean isLiked(Post post, ParseUser user) {
        ArrayList<String> likes = post.getLikes();

        for (int i = 0; i < likes.size(); i++) {
            if (likes.get(i).equals(user.getUsername())) {
                return true;
            }
        }

        return false;
    }

    public static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }
}
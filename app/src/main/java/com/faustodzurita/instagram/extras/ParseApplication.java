package com.faustodzurita.instagram.extras;

import android.app.Application;

import com.faustodzurita.instagram.models.Post;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Post.class);

        Parse.Configuration configuration = new Parse.Configuration.Builder(this)
                .applicationId("instagram")
                .clientKey("key")
                .server("https://faustodzurita-instagram.herokuapp.com/parse")
                .build();

        Parse.initialize(configuration);
    }
}
package com.ani.twitter.models;

import org.json.JSONException;
import org.json.JSONObject;

public class User {

    private String name;
    private long id;
    private String screenName;
    private String profileImageUrl;

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public static User fromJSON(JSONObject object) {
        User user = new User();
        try {
            user.name = object.getString("name");
            user.id = object.getLong("id");
            user.screenName = object.getString("screen_name");
            user.profileImageUrl = object.getString("profile_image_url");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return user;
    }
}

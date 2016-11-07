package com.ani.twitter.models;

import android.support.annotation.Nullable;

import com.ani.twitter.database.TwitterDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

@Table(database = TwitterDatabase.class)
public class User extends BaseModel implements Serializable {

    @Column
    private String name;

    @Column
    @PrimaryKey
    private long id;

    @Column
    private String screenName;

    @Column
    private String profileImageUrl;

    @Column
    private String profileBackgroundImageUrl;

    @Column
    @Nullable
    private String profileBannerUrl;

    @Column
    private String tagline;

    @Column
    private int followersCount;

    @Column
    private int followingCount;

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

    public String getProfileBackgroundImageUrl() {
        return profileBackgroundImageUrl;
    }

    public String getProfileBannerUrl() {
        return profileBannerUrl;
    }

    public String getTagline() {
        return tagline;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public void setProfileBackgroundImageUrl(String profileBackgroundImageUrl) {
        this.profileBackgroundImageUrl = profileBackgroundImageUrl;
    }

    public void setProfileBannerUrl(String profileBannerUrl) {
        this.profileBannerUrl = profileBannerUrl;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    public void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
    }

    public static User fromJSON(JSONObject object) {
        User user = new User();
        try {
            user.name = object.getString("name");
            user.id = object.getLong("id");
            user.screenName = object.getString("screen_name");
            user.profileImageUrl = object.getString("profile_image_url");
            user.profileBackgroundImageUrl = object.getString("profile_background_image_url");
            user.profileBannerUrl = object.optString("profile_banner_url");
            user.tagline = object.getString("description");
            user.followersCount = object.getInt("followers_count");
            user.followingCount = object.getInt("friends_count");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return user;
    }
}

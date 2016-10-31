package com.ani.twitter.models;

import android.support.annotation.Nullable;

import com.ani.twitter.TwitterDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Table(database = TwitterDatabase.class)
public class Tweet extends BaseModel {

    @Column
    private String text;

    @Column
    @PrimaryKey
    private long id;

    @Column
    private String createdAt;

    @Column
    @ForeignKey(saveForeignKeyModel = true)
    private User user;

    // FIXME : should use entity
    @Nullable
    private Entity entity;

    @Column
    private int retweetCount;

    @Column
    private int favoriteCount;

    public String getText() {
        return text;
    }

    public long getId() {
        return id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public User getUser() {
        return user;
    }

    @Nullable
    public Entity getEntity() {
        return entity;
    }

    public int getRetweetCount() {
        return retweetCount;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setEntity(@Nullable Entity entity) {
        this.entity = entity;
    }

    public void setRetweetCount(int retweetCount) {
        this.retweetCount = retweetCount;
    }

    public void setFavoriteCount(int favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public static Tweet fromJSON(JSONObject object) {
        Tweet tweet = new Tweet();
        try {
            tweet.text = object.getString("text");
            tweet.id = object.getLong("id");
            tweet.createdAt = object.getString("created_at");
            tweet.user = User.fromJSON(object.getJSONObject("user"));

            JSONObject entities = object.optJSONObject("entities");
            tweet.entity = entities == null ? null : Entity.fromJSON(entities);

            tweet.retweetCount = object.getInt("retweet_count");
            tweet.favoriteCount = object.getInt("favorite_count");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return tweet;
    }

    public static List<Tweet> fromJSONArray(JSONArray array) {
        ArrayList<Tweet> tweets = new ArrayList<>();

        for (int index = 0; index < array.length(); ++index) {
            try {
                Tweet tweet = Tweet.fromJSON(array.getJSONObject(index));
                if (tweet != null) {
                    tweets.add(tweet);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }

        return tweets;
    }
}

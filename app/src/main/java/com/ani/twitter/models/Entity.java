package com.ani.twitter.models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Entity {

    public List<Media> media = new ArrayList<>();

    public List<Media> getMedia() {
        return media;
    }

    public static Entity fromJSON(JSONObject object) {
        Entity entity = new Entity();
        JSONArray media = object.optJSONArray("media");
        if (media != null) {
            entity.getMedia().addAll(Media.fromJSONArray(media));
        }
        return entity;
    }
}

package com.ani.twitter.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Media {
    private long id;
    private String mediaUrl;
    private String url;

    public long getId() {
        return id;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public String getUrl() {
        return url;
    }

    public static Media fromJSON(JSONObject object) {
        Media media = null;
        try {
            media = new Media();
            media.id = object.getLong("id");
            media.mediaUrl = object.getString("media_url");
            media.url = object.getString("url");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return media;
    }

    public static List<Media> fromJSONArray(JSONArray array) {
        List<Media> medias = new ArrayList<>();
        for (int index = 0; index < array.length(); ++index) {
            try {
                Media media = fromJSON(array.getJSONObject(index));
                if (media != null) {
                    medias.add(media);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return medias;
    }
}
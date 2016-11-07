package com.ani.twitter.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.ani.twitter.models.Tweet;
import com.loopj.android.http.AsyncHttpResponseHandler;

public final class HomeTimelineFragment extends TweetsListFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    void fetchTimeline(Long maxId, AsyncHttpResponseHandler handler) {
        client.getHomeTimeline(maxId, handler);
    }
}

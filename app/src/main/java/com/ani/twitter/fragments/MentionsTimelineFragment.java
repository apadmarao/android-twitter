package com.ani.twitter.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.loopj.android.http.AsyncHttpResponseHandler;

public final class MentionsTimelineFragment extends TweetsListFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    void fetchTimeline(Long maxId, AsyncHttpResponseHandler handler) {
        client.getMentionsTimeline(maxId, handler);
    }
}

package com.ani.twitter.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.loopj.android.http.AsyncHttpResponseHandler;

public class UserTimelineFragment extends TweetsListFragment {

    public static UserTimelineFragment newInstance(String screenName) {
        UserTimelineFragment userTimelineFragment = new UserTimelineFragment();

        Bundle args = new Bundle();
        args.putString("screenName", screenName);
        userTimelineFragment.setArguments(args);

        return userTimelineFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    void fetchTimeline(Long maxId, AsyncHttpResponseHandler handler) {
        String screenName = getArguments().getString("screenName");
        client.getUserTimeline(screenName, maxId, handler);
    }
}

package com.ani.twitter.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.ani.twitter.R;
import com.ani.twitter.TwitterApplication;
import com.ani.twitter.adapters.TweetsAdapter;
import com.ani.twitter.fragments.ComposeTweetFragment;
import com.ani.twitter.models.Tweet;
import com.ani.twitter.models.Tweet_Table;
import com.ani.twitter.network.TwitterClient;
import com.ani.twitter.utils.EndlessRecyclerViewScrollListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity
        implements ComposeTweetFragment.ComposeTweetFragmentListener {

    private TwitterClient client;
    private List<Tweet> tweets;
    private TweetsAdapter tweetsAdapter;
    private RecyclerView rvTweets;
    private EndlessRecyclerViewScrollListener scrollListener;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rvTweets = (RecyclerView) findViewById(R.id.rvTweets);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        client = TwitterApplication.getRestClient();

        tweets = new ArrayList<>();
        tweetsAdapter = new TweetsAdapter(this, tweets);
        rvTweets.setAdapter(tweetsAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvTweets.setLayoutManager(layoutManager);

        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                populateTimeline(oldestTweetId());
            }
        };
        rvTweets.addOnScrollListener(scrollListener);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateTimeline(null);
            }
        });

        populateTimeline(null);
    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    private void populateTimeline(@Nullable final Long maxId) {
        if (!client.isNetworkAvailable() || !client.isOnline()) {
            Toast.makeText(this, "Can't connect right now", Toast.LENGTH_LONG).show();
            swipeContainer.setRefreshing(false);
            if (tweets.isEmpty()) {
                new LoadTweetsDb().execute();
            }
            return;
        }

        client.getHomeTimeline(maxId, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                List<Tweet> tweets = Tweet.fromJSONArray(response);
                Tweet[] tweetsArr = new Tweet[tweets.size()];
                tweetsArr = tweets.toArray(tweetsArr);
                new SaveTweetsDb(maxId == null).execute(tweetsArr);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                swipeContainer.setRefreshing(false);
                Toast.makeText(TimelineActivity.this, "Error loading from network", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Nullable
    private Long oldestTweetId() {
        return tweets.isEmpty() ? null : tweets.get(tweets.size() - 1).getId();
    }

    public void onTweet(View view) {
        FragmentManager fm = getSupportFragmentManager();
        ComposeTweetFragment composeTweetFragment = ComposeTweetFragment.newInstance();
        composeTweetFragment.show(fm, "fragment_edit_name");
    }

    @Override
    public void onTweet(Tweet tweet) {
        new SaveTweetsDb(false, true).execute(tweet);
    }

    private class LoadTweetsDb extends AsyncTask<Void, Void, List<Tweet>> {
        protected List<Tweet> doInBackground(Void... strings) {
            return SQLite.select().from(Tweet.class)
                    .orderBy(Tweet_Table.id, false).queryList();
        }

        protected void onPostExecute(List<Tweet> result) {
            // This method is executed in the UIThread
            tweets.clear();
            tweets.addAll(result);
            tweetsAdapter.notifyDataSetChanged();
        }
    }

    private class SaveTweetsDb extends AsyncTask<Tweet, Void, List<Tweet>> {
        // whether this is a refresh, used to reset state in memory and db and ui
        private final boolean isRefresh;
        // whether to add the tweets returned by the background task to the head or tail
        private final boolean addToHead;

        private SaveTweetsDb(boolean isRefresh) {
            this.isRefresh = isRefresh;
            addToHead = false;
        }

        private SaveTweetsDb(boolean isRefresh, boolean addToHead) {
            this.isRefresh = isRefresh;
            this.addToHead = addToHead;
        }

        protected List<Tweet> doInBackground(Tweet... tweets) {
            // If this is a refresh, clear old tweets from db
            if (isRefresh) {
                SQLite.delete(Tweet.class).execute();
            }
            for (Tweet tweet : tweets) {
                tweet.save();
            }

            return Arrays.asList(tweets);
        }

        protected void onPostExecute(List<Tweet> result) {
            // This method is executed in the UIThread

            swipeContainer.setRefreshing(false);
            // If this is a refresh, clear old tweets from memory and reset scroll
            if (isRefresh) {
                tweets.clear();
                scrollListener.resetState();
            }
            if (addToHead) {
                Collections.reverse(tweets);
                Collections.reverse(result);
                tweets.addAll(result);
                Collections.reverse(tweets);
            } else {
                tweets.addAll(result);
            }
            tweetsAdapter.notifyDataSetChanged();
        }
    }
}

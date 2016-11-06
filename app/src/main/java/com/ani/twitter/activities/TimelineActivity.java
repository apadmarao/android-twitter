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

import static com.ani.twitter.R.id.rvTweets;
import static com.ani.twitter.R.id.swipeContainer;

public class TimelineActivity extends AppCompatActivity {
//        implements ComposeTweetFragment.ComposeTweetFragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    public void onTweet(View view) {
        FragmentManager fm = getSupportFragmentManager();
        ComposeTweetFragment composeTweetFragment = ComposeTweetFragment.newInstance();
        composeTweetFragment.show(fm, "fragment_edit_name");
    }
//
//    @Override
//    public void onTweet(Tweet tweet) {
//        new SaveTweetsDb(false, true).execute(tweet);
//    }
}

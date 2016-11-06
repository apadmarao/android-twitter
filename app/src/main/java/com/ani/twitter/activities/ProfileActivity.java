package com.ani.twitter.activities;

import android.app.Application;
import android.media.Image;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.ani.twitter.R;
import com.ani.twitter.TwitterApplication;
import com.ani.twitter.fragments.UserTimelineFragment;
import com.ani.twitter.models.User;
import com.ani.twitter.network.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

import static com.ani.twitter.R.string.tweet;
import static com.ani.twitter.models.User_Table.screenName;
import static com.raizlabs.android.dbflow.config.FlowManager.getContext;

public class ProfileActivity extends AppCompatActivity {

    private TwitterClient client;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        client = TwitterApplication.getRestClient();
        client.getUserInfo(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                user = User.fromJSON(response);
                getSupportActionBar().setTitle("@" + user.getName());
                populateProfileHeader(user);
            }
        });

        String screenName = getIntent().getStringExtra("screenName");

        if (savedInstanceState == null) {
            UserTimelineFragment fragmentUserTimeline = UserTimelineFragment.newInstance(screenName);

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContainer, fragmentUserTimeline);
            ft.commit();
        }
    }

    private void populateProfileHeader(User user) {
        ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        TextView tvProfileName = (TextView) findViewById(R.id.tvProfileName);
        TextView tvProfileTagline = (TextView) findViewById(R.id.tvProfileTagline);
        TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
        TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);

        Picasso.with(this).load(user.getProfileImageUrl())
                .transform(new RoundedCornersTransformation(2, 2)).into(ivProfileImage);
        tvProfileName.setText(user.getName());
        tvProfileTagline.setText(user.getTagline());
        tvFollowers.setText(Integer.toString(user.getFollowersCount()));
        tvFollowing.setText(Integer.toString(user.getFollowingCount()));
    }
}

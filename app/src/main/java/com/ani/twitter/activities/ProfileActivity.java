package com.ani.twitter.activities;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
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

import java.text.NumberFormat;

import cz.msebera.android.httpclient.Header;

public class ProfileActivity extends AppCompatActivity {

    public static final String USER_EXTRA = "user";

    private TwitterClient client;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        user = (User) getIntent().getSerializableExtra(USER_EXTRA);
        if (user == null) {
            client = TwitterApplication.getRestClient();
            client.getUserInfo(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    user = User.fromJSON(response);
                    getSupportActionBar().setTitle("@" + user.getName());
                    populateProfileHeader(user);
                }

                // FIXME : handle error
            });
        } else {
            getSupportActionBar().setTitle("@" + user.getName());
            populateProfileHeader(user);
        }

        String screenName = user != null ? user.getScreenName() : null;
        if (savedInstanceState == null) {
            UserTimelineFragment fragmentUserTimeline = UserTimelineFragment.newInstance(screenName);

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContainer, fragmentUserTimeline);
            ft.commit();
        }
    }

    private void populateProfileHeader(User user) {
        ImageView ivProfileBannerImage = (ImageView) findViewById(R.id.ivProfileBannerImage);
        ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        TextView tvProfileName = (TextView) findViewById(R.id.tvProfileName);
        TextView tvProfileScreenName = (TextView) findViewById(R.id.tvProfileScreenName);
        TextView tvProfileTagline = (TextView) findViewById(R.id.tvProfileTagline);
        TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);
        TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);

        if (user.getProfileBannerUrl() == null || user.getProfileBannerUrl().isEmpty()) {
            ivProfileImage.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        } else {
            Picasso.with(this).load(user.getProfileBannerUrl()).into(ivProfileBannerImage);
        }
        Picasso.with(this).load(user.getProfileImageUrl()).into(ivProfileImage);

        Resources res = getResources();

        tvProfileName.setText(user.getName());
        tvProfileScreenName.setText(String.format(res.getString(R.string.handle), user.getScreenName()));
        tvProfileTagline.setText(user.getTagline());

        tvFollowing.setText(Html.fromHtml(String.format(res.getString(R.string.following),
                formatCount(user.getFollowingCount()))));
        tvFollowers.setText(Html.fromHtml(String.format(res.getString(R.string.followers),
                formatCount(user.getFollowersCount()))));
    }

    private String formatCount(int count) {
        return NumberFormat.getNumberInstance(getResources().getConfiguration().locale)
                .format(count);
    }
}

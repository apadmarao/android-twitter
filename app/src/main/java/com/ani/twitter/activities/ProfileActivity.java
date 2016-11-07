package com.ani.twitter.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
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

import static com.ani.twitter.R.id.tvProfileTagline;

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
//        TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
//        TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);

        if (user.getProfileBannerUrl() == null || user.getProfileBannerUrl().isEmpty()) {
            ivProfileImage.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        } else {
            Picasso.with(this).load(user.getProfileBannerUrl()).into(ivProfileBannerImage);
        }
        Picasso.with(this).load(user.getProfileImageUrl())
                .transform(new RoundedCornersTransformation(2, 2)).into(ivProfileImage);
        tvProfileName.setText(user.getName());
        tvProfileScreenName.setText("@" + user.getScreenName());
        tvProfileTagline.setText(user.getTagline());
//        tvFollowers.setText(Integer.toString(user.getFollowersCount()));
//        tvFollowing.setText(Integer.toString(user.getFollowingCount()));
    }
}

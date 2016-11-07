package com.ani.twitter.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.ani.twitter.R;
import com.ani.twitter.fragments.ProfileHeaderFragment;
import com.ani.twitter.fragments.UserTimelineFragment;
import com.ani.twitter.models.User;

public class ProfileActivity extends AppCompatActivity {

    public static final String USER_EXTRA = "user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        User user = (User) getIntent().getSerializableExtra(USER_EXTRA);
        String screenName = user == null ? null : user.getScreenName();

        if (savedInstanceState == null) {
            UserTimelineFragment fragmentUserTimeline = UserTimelineFragment.newInstance(screenName);
            ProfileHeaderFragment fragmentHeader = ProfileHeaderFragment.newInstance(user);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flProfileHeader, fragmentHeader);
            ft.replace(R.id.flContainer, fragmentUserTimeline);
            ft.commit();
        }
    }
}

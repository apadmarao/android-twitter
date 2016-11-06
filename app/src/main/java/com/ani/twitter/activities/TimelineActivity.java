package com.ani.twitter.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ani.twitter.R;
import com.ani.twitter.fragments.ComposeTweetFragment;
import com.ani.twitter.fragments.HomeTimelineFragment;
import com.ani.twitter.fragments.MentionsTimelineFragment;

public class TimelineActivity extends AppCompatActivity {
//        implements ComposeTweetFragment.ComposeTweetFragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager(),
                TimelineActivity.this));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.slidingTabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    public void onTweet(View view) {
        FragmentManager fm = getSupportFragmentManager();
        ComposeTweetFragment composeTweetFragment = ComposeTweetFragment.newInstance();
        composeTweetFragment.show(fm, "fragment_edit_name");
    }

    public void onProfileView(MenuItem item) {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

//
//    @Override
//    public void onTweet(Tweet tweet) {
//        new SaveTweetsDb(false, true).execute(tweet);
//    }

    private static class TweetsPagerAdapter extends FragmentPagerAdapter {

        private String tabTitles[] = new String[] { "Home", "Mentions" };
        private Context context;

        public TweetsPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new HomeTimelineFragment();
            } else {
                return new MentionsTimelineFragment();
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }
    }
}

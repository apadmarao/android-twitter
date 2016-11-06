package com.ani.twitter.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
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

    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager(),
                TimelineActivity.this));

        tabLayout = (TabLayout) findViewById(R.id.slidingTabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabLayoutIcons();
        tabLayout.addOnTabSelectedListener(new OnTabSelectedListener(TimelineActivity.this, viewPager));
    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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

        private TweetsPagerAdapter(FragmentManager fm, Context context) {
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
    }

    private static class OnTabSelectedListener extends TabLayout.ViewPagerOnTabSelectedListener {

        private final Context context;

        private OnTabSelectedListener(Context context, ViewPager viewPager) {
            super(viewPager);
            this.context = context;
        }

        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            super.onTabSelected(tab);
            int tabIconColor = ContextCompat.getColor(context, R.color.colorAccent);
            tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            super.onTabUnselected(tab);
            int tabIconColor = ContextCompat.getColor(context, R.color.darkGray);
            tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
            super.onTabReselected(tab);
        }
    }

    private void setupTabLayoutIcons() {
        for (int index = 0; index < tabLayout.getTabCount(); ++index) {
            TabLayout.Tab tab = tabLayout.getTabAt(index);
            if (tab == null) {
                return;
            }

            // setup icon
            if (index == 0) {
                tab.setIcon(R.drawable.ic_home_black_24dp);
            } else {
                tab.setIcon(R.drawable.ic_notifications_active_black_24dp);
            }

            // setup icon color
            final int tabIconColor;
            if (tab.isSelected()) {
                tabIconColor = ContextCompat.getColor(TimelineActivity.this, R.color.colorAccent);
            } else {
                tabIconColor = ContextCompat.getColor(TimelineActivity.this, R.color.darkGray);
            }
            tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
        }
    }
}

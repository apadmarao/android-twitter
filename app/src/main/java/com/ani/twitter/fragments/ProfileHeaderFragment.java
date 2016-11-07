package com.ani.twitter.fragments;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ani.twitter.R;
import com.ani.twitter.TwitterApplication;
import com.ani.twitter.models.User;
import com.ani.twitter.network.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.text.NumberFormat;

import cz.msebera.android.httpclient.Header;

public class ProfileHeaderFragment extends Fragment {

    private static final String USER_EXTRA = "user";

    private TwitterClient client;
    private User user;

    private ImageView ivProfileBannerImage;
    private ImageView ivProfileImage;
    private TextView tvProfileName;
    private TextView tvProfileScreenName;
    private TextView tvProfileTagline;
    private TextView tvFollowing;
    private TextView tvFollowers;

    public static ProfileHeaderFragment newInstance(User user) {
        ProfileHeaderFragment fragment = new ProfileHeaderFragment();
        Bundle args = new Bundle();
        args.putSerializable(USER_EXTRA, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        client = TwitterApplication.getRestClient();

        user = (User) getArguments().getSerializable(USER_EXTRA);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_header, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ivProfileBannerImage = (ImageView) view.findViewById(R.id.ivProfileBannerImage);
        ivProfileImage = (ImageView) view.findViewById(R.id.ivProfileImage);
        tvProfileName = (TextView) view.findViewById(R.id.tvProfileName);
        tvProfileScreenName = (TextView) view.findViewById(R.id.tvProfileScreenName);
        tvProfileTagline = (TextView) view.findViewById(R.id.tvProfileTagline);
        tvFollowing = (TextView) view.findViewById(R.id.tvFollowing);
        tvFollowers = (TextView) view.findViewById(R.id.tvFollowers);

        if (user == null) {
            // loading self profile from toolbar
            populateHeader();
        } else {
            // clicked on user profile image
            populateHeaderWithUser(user);
        }
    }

    private void populateHeader() {
        if (!client.isNetworkAvailable() || !client.isOnline()) {
            Toast.makeText(getActivity(), "Can't connect right now", Toast.LENGTH_LONG).show();
            return;
        }

        client.getUserInfo(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                user = User.fromJSON(response);
                populateHeaderWithUser(user);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                    JSONObject errorResponse) {
                Toast.makeText(getActivity(), "Error loading from network", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void populateHeaderWithUser(User user) {
        ((AppCompatActivity) getActivity()).getSupportActionBar()
                .setTitle("@" + user.getScreenName());

        if (user.getProfileBannerUrl() == null || user.getProfileBannerUrl().isEmpty()) {
            ivProfileImage.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        } else {
            Picasso.with(getActivity()).load(user.getProfileBannerUrl()).into(ivProfileBannerImage);
        }
        Picasso.with(getActivity()).load(user.getProfileImageUrl()).into(ivProfileImage);

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

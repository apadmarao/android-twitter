package com.ani.twitter;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ani.twitter.models.Entity;
import com.ani.twitter.models.Media;
import com.ani.twitter.models.Tweet;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.TweetViewHolder> {

    private List<Tweet> tweets;
    private Context context;

    public TweetsAdapter(Context context, List<Tweet> tweets) {
        this.context = context;
        this.tweets = tweets;
    }

    private Context getContext() {
        return context;
    }

    @Override
    public TweetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.item_tweet, parent, false);

        return new TweetViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(TweetViewHolder holder, int position) {
        Tweet tweet = tweets.get(position);

        holder.tvName.setText(tweet.getUser().getName());

        Resources res = getContext().getResources();
        String text = String.format(res.getString(R.string.handle), tweet.getUser().getScreenName());
        holder.tvScreenName.setText(text);

        holder.tvTweet.setText(tweet.getText());
        holder.tvTime.setText(getRelativeTimeAgo(tweet.getCreatedAt()));

        holder.ivProfile.setImageResource(android.R.color.transparent);
        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(holder.ivProfile);

        holder.ivMedia.setImageResource(android.R.color.transparent);
        String mediaUrl = mediaUrl(tweet);
        if (mediaUrl != null) {
            Picasso.with(getContext()).load(mediaUrl).into(holder.ivMedia);
            holder.ivMedia.setVisibility(View.VISIBLE);
        } else {
            holder.ivMedia.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    private static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

    @Nullable
    private static String mediaUrl(Tweet tweet) {
        Entity entity = tweet.getEntity();
        if (entity != null) {
            List<Media> media = entity.getMedia();
            if (!media.isEmpty()) {
                return media.get(0).getMediaUrl();
            }
        }

        return null;
    }

    static class TweetViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivProfile;
        private TextView tvName;
        private TextView tvScreenName;
        private TextView tvTweet;
        private TextView tvTime;
        private ImageView ivMedia;

        TweetViewHolder(View itemView) {
            super(itemView);

            ivProfile = (ImageView) itemView.findViewById(R.id.ivProfile);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvScreenName = (TextView) itemView.findViewById(R.id.tvScreenName);
            tvTweet = (TextView) itemView.findViewById(R.id.tvTweet);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
            ivMedia = (ImageView) itemView.findViewById(R.id.ivMedia);
        }
    }
}

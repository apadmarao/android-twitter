package com.ani.twitter.fragments;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ani.twitter.R;
import com.ani.twitter.TwitterApplication;
import com.ani.twitter.network.TwitterClient;
import com.ani.twitter.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ComposeTweetFragment extends DialogFragment {

    private static final int TWEET_CHARS = 140;

    private TwitterClient client;

    private EditText etTweet;
    private Button btnTweet;
    private Button btnCancel;
    private TextView tvCharsRemaining;

    public ComposeTweetFragment() {
        // Empty constructor is required for DialogFragment
    }

    public static ComposeTweetFragment newInstance() {
        ComposeTweetFragment frag = new ComposeTweetFragment();
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_compose_tweet, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        client = TwitterApplication.getRestClient();
        etTweet = (EditText) view.findViewById(R.id.etTweet);
        btnTweet = (Button) view.findViewById(R.id.btnTweet);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        tvCharsRemaining = (TextView) view.findViewById(R.id.tvCharactersRemaining);

        getDialog().setTitle("Hello world");
        // Show soft keyboard automatically and request focus to field
        etTweet.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        etTweet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                setCharsRemaining(TWEET_CHARS - editable.toString().length());
            }
        });

        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!client.isNetworkAvailable() || !client.isOnline()) {
                    Toast.makeText(getContext(), "Can't connect right now", Toast.LENGTH_LONG).show();
                    return;
                }

                client.tweet(etTweet.getText().toString(), new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Tweet tweet = Tweet.fromJSON(response);

                        ComposeTweetFragmentListener listener = (ComposeTweetFragmentListener) getActivity();
                        listener.onTweet(tweet);

                        dismiss();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                            JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                    }
                });
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        setCharsRemaining(TWEET_CHARS);
    }

    public interface ComposeTweetFragmentListener {
        void onTweet(Tweet tweet);
    }

    private void setCharsRemaining(int charsRemaining) {
        Resources res = getContext().getResources();
        tvCharsRemaining.setText(String.format(res.getString(R.string.number), charsRemaining));
    }
}

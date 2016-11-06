package com.ani.twitter.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import java.io.IOException;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 */
public class TwitterClient extends OAuthBaseClient {
	private static final Class<? extends Api> REST_API_CLASS = TwitterApi.class;
	private static final String REST_URL = "https://api.twitter.com/1.1";
	private static final String REST_CONSUMER_KEY = "bFgqFtVyue71qymFKZvxeLMZa";
	private static final String REST_CONSUMER_SECRET = "bxms8BinKmoIl2scFuSBAXLxfvrZQifsuYIGvgoKO1jkk5xUBK";
	private static final String REST_CALLBACK_URL = "oauth://tweettweet";

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

	public void getHomeTimeline(@Nullable Long maxId, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/home_timeline.json");

		RequestParams params = new RequestParams();
		params.put("count", 25);
		params.put("since_id", 1);
		if (maxId != null) {
			// max id is the inclusive oldest tweet
			// subtract one to avoid dups
			params.put("max_id", maxId - 1);
		}

		client.get(apiUrl, params, handler);
	}

	public void getMentionsTimeline(@Nullable Long maxId, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/mentions_timeline.json");

		RequestParams params = new RequestParams();
		params.put("count", 25);
		if (maxId != null) {
			// max id is the inclusive oldest tweet
			// subtract one to avoid dups
			params.put("max_id", maxId - 1);
		}

		client.get(apiUrl, params, handler);
	}

	public void tweet(String status, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/update.json");

		RequestParams params = new RequestParams();
		params.put("status", status);

		client.post(apiUrl, params, handler);
	}

	public void getUserTimeline(@Nullable String screenName, @Nullable Long maxId, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/user_timeline.json");

		RequestParams params = new RequestParams();
		params.put("count", 25);
		if (maxId != null) {
			// max id is the inclusive oldest tweet
			// subtract one to avoid dups
			params.put("max_id", maxId - 1);
		}
		if (screenName != null) {
			params.put("screen_name", screenName);
		}

		client.get(apiUrl, params, handler);
	}

	public void getUserInfo(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("account/verify_credentials.json");
		client.get(apiUrl, null, handler);
	}

	public Boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager
				= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
	}

	public boolean isOnline() {
		Runtime runtime = Runtime.getRuntime();
		try {
			Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
			int     exitValue = ipProcess.waitFor();
			return (exitValue == 0);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		return false;
	}
}

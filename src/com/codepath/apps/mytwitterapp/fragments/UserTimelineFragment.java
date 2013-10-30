package com.codepath.apps.mytwitterapp.fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.codepath.apps.mytwitterapp.MyTwitterClientApp;
import com.codepath.apps.mytwitterapp.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public class UserTimelineFragment extends TweetsListFragment {

	String screen_name;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//populates the initial set of tweets
		MyTwitterClientApp.getRestClient().getUserTimeline( new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsonTweets) {
              //create tweets array and make listview		
				ArrayList<Tweet> tweets = Tweet.fromJson(jsonTweets);

				getAdapter().addAll(tweets);
			    min_id = Tweet.getMinId(tweets, min_id);
		
			}
			
			public void onFailure(Throwable e, JSONObject error) {
				// Handle the failure and alert the user to
				// retry
				Log.e("ERROR", e.toString());
				Toast.makeText(getActivity(), "Tweets not loaded",
				        Toast.LENGTH_SHORT).show();
			}
			
		});
	}
	
	public void setScreenname( String s) {
		screen_name = s;
	}
	
  /*@Override
   public void fetchTimelineAsync(int page) {
	   	MyTwitterClientApp.getRestClient().getUserInfo( screen_name, new JsonHttpResponseHandler() {
	           public void onSuccess(JSONArray json) {
	           	
	              ArrayList<Tweet> tweets = Tweet.fromJson(json);
				   adapter.clear();
				   adapter.addAll(tweets);
				   
				   //update min_id for the oldest tweet we have
				   min_id = Tweet.getMinId(tweets, min_id);

	               // ...the data has come back, finish populating listview...
	               // Now we call onRefreshComplete to signify refresh has finished
	           	  lvTweets.onRefreshComplete();
	           }

	           public void onFailure(Throwable e) {
	               Log.d("DEBUG", "Fetch timeline error: " + e.toString());
	           }
	       }, 0);
	   }

	//called on endless scroll
	@Override
	public void loadMoreTweets() {
		MyTwitterClientApp.getRestClient().getUserInfo( screen_name, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsonTweets) {
				ArrayList<Tweet> tweets = Tweet.fromJson(jsonTweets);
				adapter.addAll(tweets);
                min_id = Tweet.getMinId(Tweet.fromJson(jsonTweets), min_id);

			}

			public void onFailure(Throwable e, JSONObject error) {
				// Handle the failure and alert the user to
				// retry
			
				Log.e("ERROR", e.toString());
				Toast.makeText(getActivity(), "Tweets not loaded",
				        Toast.LENGTH_SHORT).show();
			}

		}, min_id);
	}*/
			
}

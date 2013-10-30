package com.codepath.apps.mytwitterapp.fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.codepath.apps.mytwitterapp.MyTwitterClientApp;
import com.codepath.apps.mytwitterapp.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MentionsFragment extends TweetsListFragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//populates the initial set of tweets
		MyTwitterClientApp.getRestClient().getMentions( new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsonTweets) {
              //create tweets array and make listview		
				getAdapter().addAll(Tweet.fromJson(jsonTweets));
					
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
}

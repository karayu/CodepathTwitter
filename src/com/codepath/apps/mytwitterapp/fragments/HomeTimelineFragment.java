package com.codepath.apps.mytwitterapp.fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.codepath.apps.mytwitterapp.EndlessScrollListener;
import com.codepath.apps.mytwitterapp.MyTwitterClientApp;
import com.codepath.apps.mytwitterapp.R;
import com.codepath.apps.mytwitterapp.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class HomeTimelineFragment extends TweetsListFragment {

	public static String TABTYPE = "HomeTimelineFragment";
	PullToRefreshListView lvTweets;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		lvTweets = (PullToRefreshListView)getActivity().findViewById(R.id.lvTweets);

		
		//populates the initial set of tweets
		MyTwitterClientApp.getRestClient().getTimeline( TABTYPE, new JsonHttpResponseHandler() {
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
			
		}, 0);	   
	}	

}

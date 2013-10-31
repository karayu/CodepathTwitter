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
		min_id = 0;
		fetchTimelineAsync();
		

	}	
	
	

	@Override
	   public void fetchTimelineAsync() {
	 	    Log.d("DEBUG", "fetchtimline from tweetslistfragment");

		   	MyTwitterClientApp.getRestClient().getHomeTimeline(new JsonHttpResponseHandler() {
		           public void onSuccess(JSONArray json) {
		        	  Log.d("DEBUG", "fetching tweets: " + json);
		              ArrayList<Tweet> tweets = Tweet.fromJson(json);
		               
		              //if min_id ==0 then this is either pulling to refresh or initial load. 
		              if(refresh) {
		            	  adapter.clear();
		            	  Log.d("DEBUG", "refreshing!");
		              }
					   
		              adapter.addAll(tweets);
					   //update min_id for the oldest tweet we have
					   min_id = Tweet.getMinId(tweets, min_id);
					   
					   
		               // ...the data has come back, finish populating listview...
		               // Now we call onRefreshComplete to signify refresh has finished
					  if(refresh && lvTweets!=null) {
						  lvTweets.onRefreshComplete();
						  refresh = false;
					  }
					  

		           }

		           public void onFailure(Throwable e) {
		               Log.d("DEBUG", "Fetch timeline error: " + e.toString());
		           }
		       }, min_id);
		   }

		//called on endless scroll
	   /*@Override
		public void loadMoreTweets() {
	 	    Log.d("DEBUG", "loadmoretweets from tweetslistfragment");

			MyTwitterClientApp.getRestClient().getHomeTimeline( new JsonHttpResponseHandler() {
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

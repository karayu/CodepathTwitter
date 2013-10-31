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
	
	@Override
	   public void fetchTimelineAsync() {
	 	    Log.d("DEBUG", "fetchtimline from tweetslistfragment");

		   	MyTwitterClientApp.getRestClient().getMentions( new JsonHttpResponseHandler() {
		           public void onSuccess(JSONArray json) {
		           	
		        	   
		        	  
		              ArrayList<Tweet> tweets = Tweet.fromJson(json);
		              
		              if(refresh) {
		            	  adapter.clear();
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
		       }, 0);
		   }



}

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
		//populates tweets
		fetchTimelineAsync();

	}
	
	public void setScreenname( String s) {
		screen_name = s;
	}
	
   @Override
   public void fetchTimelineAsync() {
	   
	   	MyTwitterClientApp.getRestClient().getUserTimeline( screen_name, min_id, new JsonHttpResponseHandler() {
	           public void onSuccess(JSONArray json) {
	           	
	        	  Log.d("DEBUG", "pull down refresh beign called from UserTime and using correct twitter client call");
	              ArrayList<Tweet> tweets = Tweet.fromJson(json);
	              
	              if(refresh){ 
				     adapter.clear();
	              }

				  adapter.addAll(tweets);
	              min_id = Tweet.getMinId(Tweet.fromJson(json), min_id);


	               // ...the data has come back, finish populating listview...
	               // Now we call onRefreshComplete to signify refresh has finished for pull to refresh
				  if(refresh && lvTweets!= null) {
					  lvTweets.onRefreshComplete();
				  }
				  

	           }

	           public void onFailure(Throwable e) {
	               Log.d("DEBUG", "Fetch timeline error: " + e.toString());
	           }
	       });
	   }

	//called on endless scroll
	
	/*public void loadMoreTweets() {
		MyTwitterClientApp.getRestClient().getUserTimeline( screen_name, new JsonHttpResponseHandler() {
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

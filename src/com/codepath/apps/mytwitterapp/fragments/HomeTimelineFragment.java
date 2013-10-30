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

	long min_id = 0;
	long max_id = 0;
	PullToRefreshListView lvTweets;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		lvTweets = (PullToRefreshListView)getActivity().findViewById(R.id.lvTweets);

		
		//populates the initial set of tweets
		MyTwitterClientApp.getRestClient().getHomeTimeline( new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsonTweets) {
              //create tweets array and make listview
				
				ArrayList<Tweet> tweets = Tweet.fromJson(jsonTweets);
				getAdapter().addAll(tweets);
				
				//this part should be removed. maybe?
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
	
	
	//the listeners live in TweetsListFragment but the actual function of what happens lives here... how to call?
	 
	//called on endless scroll
	public void loadMoreTweets() {
		MyTwitterClientApp.getRestClient().getNewTweets( new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsonTweets) {
				adapter.addAll(Tweet.fromJson(jsonTweets));
				max_id = Tweet.getMaxId(Tweet.fromJson(jsonTweets), min_id);
			}

			public void onFailure(Throwable e, JSONObject error) {
				// Handle the failure and alert the user to
				// retry
			
				Log.e("ERROR", e.toString());
				Toast.makeText(getActivity(), "Tweets not loaded",
				        Toast.LENGTH_SHORT).show();
			}

		}, min_id);
	}
	
	//called for pulltorefresh. deletes everything and gets a set of new 25 tweets
    public void fetchTimelineAsync(int page) {
    	MyTwitterClientApp.getRestClient().getHomeTimeline(new JsonHttpResponseHandler() {
            public void onSuccess(JSONArray json) {
            	
               ArrayList<Tweet> tweets = Tweet.fromJson(json);
			   adapter.clear();
			   adapter.addAll(tweets);
			    
			   min_id = Tweet.getMinId(tweets, min_id);

                // ...the data has come back, finish populating listview...
                // Now we call onRefreshComplete to signify refresh has finished
            	//lvTweets.onRefreshComplete();
            }

            public void onFailure(Throwable e) {
                Log.d("DEBUG", "Fetch timeline error: " + e.toString());
            }
        }, 0);
    }
}

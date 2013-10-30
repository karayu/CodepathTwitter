package com.codepath.apps.mytwitterapp.fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.apps.mytwitterapp.EndlessScrollListener;
import com.codepath.apps.mytwitterapp.MyTwitterClientApp;
import com.codepath.apps.mytwitterapp.R;
import com.codepath.apps.mytwitterapp.TweetsAdapter;
import com.codepath.apps.mytwitterapp.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class TweetsListFragment extends Fragment {
	TweetsAdapter adapter;
	PullToRefreshListView lvTweets;
	long min_id = 0;
	
	String tabtype = "";

	
	@Override
	public View onCreateView(LayoutInflater inf, ViewGroup parent, Bundle savedInstanceState) {
		return inf.inflate(R.layout.fragment_tweets_list, parent, false);
	}
	
	@Override 
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		   ArrayList<Tweet> tweets = new ArrayList<Tweet>();
		   adapter = new TweetsAdapter(getActivity(), tweets);
		   
		   lvTweets = (PullToRefreshListView) getActivity().findViewById(R.id.lvTweets);
		   lvTweets.setAdapter(adapter);
		   
		   
			//endless scroll listener
		    lvTweets.setOnScrollListener(new EndlessScrollListener() {
				@Override
				public void onLoadMore(int page, int totalItemsCount) {
					loadMoreTweets();
				}

			 });
			   
				
	        // Set a listener to be invoked when user pulls down
	        lvTweets.setOnRefreshListener(new OnRefreshListener() {
	            @Override
	            public void onRefresh() {
	                fetchTimelineAsync(0);
	            }
	        });
			
	}
	

   public void fetchTimelineAsync(int page) {
	   	MyTwitterClientApp.getRestClient().getTimeline( tabtype, new JsonHttpResponseHandler() {
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
	public void loadMoreTweets() {
		MyTwitterClientApp.getRestClient().getTimeline( tabtype, new JsonHttpResponseHandler() {
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
	}
		
	   
	   
	public TweetsAdapter getAdapter() {
		return adapter;
	}
	
   
   /*public void setMinId(Long m) {
	   min_id = m;
   }
   
   public long getMinId() {
	   return min_id;
   }*/
	
   public void setTagType( String type) {
	   tabtype = type;
   }
	
}

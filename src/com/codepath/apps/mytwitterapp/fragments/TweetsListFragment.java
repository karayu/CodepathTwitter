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

public abstract class TweetsListFragment extends Fragment {
	TweetsAdapter adapter;
	PullToRefreshListView lvTweets;
	long min_id = 0;
	boolean refresh = false;
	
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
	                fetchTimelineAsync();
	                
				}

			 });
			   
				
	        // Set a listener to be invoked when user pulls down
	        lvTweets.setOnRefreshListener(new OnRefreshListener() {
	            @Override
	            public void onRefresh() {
	            	refresh = true;
	                fetchTimelineAsync();
	            }
	        });
			
	}
	

   public abstract void fetchTimelineAsync(); 

	   
	public TweetsAdapter getAdapter() {
		return adapter;
	}
	
   
   /*public void setMinId(Long m) {
	   min_id = m;
   }
   
   public long getMinId() {
	   return min_id;
   }*/

}

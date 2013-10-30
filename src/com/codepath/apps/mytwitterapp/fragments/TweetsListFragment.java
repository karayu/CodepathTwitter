package com.codepath.apps.mytwitterapp.fragments;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.mytwitterapp.EndlessScrollListener;
import com.codepath.apps.mytwitterapp.R;
import com.codepath.apps.mytwitterapp.TweetsAdapter;
import com.codepath.apps.mytwitterapp.models.Tweet;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class TweetsListFragment extends Fragment {
	TweetsAdapter adapter;
	//ListView lvTweets;
	PullToRefreshListView lvTweets;


	
	@Override
	public View onCreateView(LayoutInflater inf, ViewGroup parent, Bundle savedInstanceState) {
		return inf.inflate(R.layout.fragment_tweets_list, parent, false);
	}
	
	@Override 
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		   ArrayList<Tweet> tweets = new ArrayList<Tweet>();
		   adapter = new TweetsAdapter(getActivity(), tweets);
		   
		   //lvTweets = (ListView)getActivity().findViewById(R.id.lvTweets);
		   lvTweets = (PullToRefreshListView) getActivity().findViewById(R.id.lvTweets);
		   lvTweets.setAdapter(adapter);
		   
			//endless scroll listener
		    lvTweets.setOnScrollListener(new EndlessScrollListener() {
				@Override
				public void onLoadMore(int page, int totalItemsCount) {
					//loadMoreTweets();
				}

			 });
			   
				
	        // Set a listener to be invoked when user pulls down
	        lvTweets.setOnRefreshListener(new OnRefreshListener() {
	            @Override
	            public void onRefresh() {
	                // Your code to refresh the list contents
	                // Make sure you call listView.onRefreshComplete()
	                // once the loading is done. This can be done from here or any
	                // place such as when the network request has completed successfully.
	                //fetchTimelineAsync(0);
	            }
	        });
			
	}
	
	public TweetsAdapter getAdapter() {
		return adapter;
	}
	


	
	
	
}

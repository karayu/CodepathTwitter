package com.codepath.apps.mytwitterapp.fragments;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.codepath.apps.mytwitterapp.R;
import com.codepath.apps.mytwitterapp.TweetsAdapter;
import com.codepath.apps.mytwitterapp.models.Tweet;

public class TweetsListFragment extends Fragment {
	TweetsAdapter adapter;
	
	@Override
	public View onCreateView(LayoutInflater inf, ViewGroup parent, Bundle savedInstanceState) {
		return inf.inflate(R.layout.fragment_tweets_list, parent, false);
	}
	
	@Override 
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		   ArrayList<Tweet> tweets = new ArrayList<Tweet>();
		   adapter = new TweetsAdapter(getActivity(), tweets);
		   
		   ListView lvTweets = (ListView)getActivity().findViewById(R.id.lvTweets);
		   lvTweets.setAdapter(adapter);
	}
	
	public TweetsAdapter getAdapter() {
		return adapter;
	}
	
}

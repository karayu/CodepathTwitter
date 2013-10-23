package com.codepath.apps.mytwitterapp;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.mytwitterapp.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TimelineActivity extends Activity {

	private ListView lvTweets;
	ArrayList<Tweet> tweets = new ArrayList<Tweet>();;
	TweetsAdapter adapter;
	long min_id = 0;
	
	public static int COMPOSE_REQUEST_CODE = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		lvTweets = (ListView)findViewById(R.id.lvTweets);

		
		MyTwitterClientApp.getRestClient().getHomeTimeline( new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsonTweets) {
			   tweets = Tweet.fromJson(jsonTweets);
			   adapter = new TweetsAdapter(getBaseContext(), tweets);
			   lvTweets.setAdapter(adapter);
			   
			   min_id = Tweet.getMinId(tweets, min_id);
			   		
			}
			
			public void onFailure(Throwable e, JSONObject error) {
				// Handle the failure and alert the user to
				// retry
				Log.e("ERROR", e.toString());
				Toast.makeText(getApplicationContext(), "Tweets not loaded",
				        Toast.LENGTH_SHORT).show();
			}
			
		}, 0);
		
		
		//endless scroll listener
		lvTweets.setOnScrollListener(new EndlessScrollListener() {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				loadMoreTweets();

			}

		});
	}
	
	//called on endless scroll
	public void loadMoreTweets() {
		MyTwitterClientApp.getRestClient().getHomeTimeline( new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsonTweets) {
				adapter.addAll(Tweet.fromJson(jsonTweets));
				min_id = Tweet.getMinId(Tweet.fromJson(jsonTweets), min_id);
			}

			public void onFailure(Throwable e, JSONObject error) {
				// Handle the failure and alert the user to
				// retry
			
				Log.e("ERROR", e.toString());
				Toast.makeText(getApplicationContext(), "Tweets not loaded",
				        Toast.LENGTH_SHORT).show();
			}

		}, min_id);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.timeline, menu);
		return true;
	}
	
	
	//launches the compose screen
	public void compose(MenuItem mi) {
		//do some stuff
		Intent i = new Intent(getApplicationContext(), ComposeActivity.class);
		startActivityForResult(i,COMPOSE_REQUEST_CODE); 
	}
	
	// the tweet is passed back in string form through the intent and we add it to the adapter
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	  if (resultCode == RESULT_OK && requestCode == COMPOSE_REQUEST_CODE) {
		 
		  String d = data.getExtras().getString("tweet");
		 
		  try {
			JSONObject tweetDetails = new JSONObject(d);
			Tweet tweet = new Tweet(tweetDetails);
			adapter.insert(tweet, 0);
			
		  } catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.d("DEBUG", "tweet data is: " + d);

			e.printStackTrace();
			Toast.makeText(getApplicationContext(), "Tweets failed",
			        Toast.LENGTH_SHORT).show();
			
		  }
	  }
	} 

}

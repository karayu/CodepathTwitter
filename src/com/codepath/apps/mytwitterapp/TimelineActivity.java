package com.codepath.apps.mytwitterapp;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.codepath.apps.mytwitterapp.fragments.TweetsListFragment;
import com.codepath.apps.mytwitterapp.models.Tweet;
import com.codepath.apps.mytwitterapp.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TimelineActivity extends FragmentActivity {
	
	ArrayList<Tweet> tweets = new ArrayList<Tweet>();
	TweetsAdapter adapter;

	User user;
	
	
	public static int COMPOSE_REQUEST_CODE = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		
		
		MyTwitterClientApp.getRestClient().getUserAccount( new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject userDetails) {
				   user = new User(userDetails);
			   	   user.save();	
			}
			
			public void onFailure(Throwable e, JSONObject error) {
				Log.e("ERROR", e.toString());
				Toast.makeText(getApplicationContext(), "User not logged in!",
				        Toast.LENGTH_SHORT).show();
			}
			
		});	
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
			Log.d("DEBUG", "tweet data is: " + d);

			e.printStackTrace();
			Toast.makeText(getApplicationContext(), "Tweets failed",
			        Toast.LENGTH_SHORT).show();
			
		  }
	  }
	} 

}

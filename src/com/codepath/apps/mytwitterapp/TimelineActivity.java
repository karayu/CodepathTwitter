package com.codepath.apps.mytwitterapp;

import java.util.ArrayList;

import org.json.JSONArray;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.codepath.apps.mytwitterapp.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TimelineActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		
		MyTwitterClientApp.getRestClient().getHomeTimeline( new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsonTweets) {
				
			   ArrayList<Tweet> tweets = Tweet.fromJson(jsonTweets);
			   ListView lvTweets = (ListView)findViewById(R.id.lvTweets);
			   TweetsAdapter adapter = new TweetsAdapter(getBaseContext(), tweets);
			   lvTweets.setAdapter(adapter);
			   			   
			}
			
			/*public void onFailure(Exception e, JSONArray json) {
				 Log.d("DEBUG", "Fetch timeline error: " + e.toString());
			     Log.d("DEBUG", "Fetch timeline error json: " + json.toString());
				
			}*/
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.timeline, menu);
		return true;
	}
	
	
	public void compose(MenuItem mi) {
		//do some stuff
		
	}

}

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
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.mytwitterapp.models.Tweet;
import com.codepath.apps.mytwitterapp.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TimelineActivity extends FragmentActivity {

	//private PullToRefreshListView lvTweets;
	ListView lvTweets;
	ArrayList<Tweet> tweets = new ArrayList<Tweet>();
	TweetsAdapter adapter;
	long min_id = 0;
	long max_id = 0;
	User user;
	
	
	public static int COMPOSE_REQUEST_CODE = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		//lvTweets = (PullToRefreshListView)findViewById(R.id.lvTweets);
		
		lvTweets = (ListView)findViewById(R.id.lvTweets);
		Log.d("DEBUG", "got the listview!");
		
		MyTwitterClientApp.getRestClient().getUserAccount( new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject userDetails) {
				   user = new User(userDetails);
			   	   user.save();	
			}
			
			public void onFailure(Throwable e, JSONObject error) {
				// Handle the failure and alert the user to
				// retry
				Log.e("ERROR", e.toString());
				Toast.makeText(getApplicationContext(), "User not logged in!",
				        Toast.LENGTH_SHORT).show();
			}
			
		});
		
		//populates the initial set of tweets
		MyTwitterClientApp.getRestClient().getHomeTimeline( new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsonTweets) {
			   ArrayList<Tweet> tweets = Tweet.fromJson(jsonTweets);
			   
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
		
        // Set a listener to be invoked when user pulls down
        /*lvTweets.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list contents
                // Make sure you call listView.onRefreshComplete()
                // once the loading is done. This can be done from here or any
                // place such as when the network request has completed successfully.
                fetchTimelineAsync(0);
            }
        });*/
	}
	
	//called for pulltorefresh. deletes everything and gets a set of new 25 tweets
    public void fetchTimelineAsync(int page) {
    	MyTwitterClientApp.getRestClient().getHomeTimeline(new JsonHttpResponseHandler() {
            public void onSuccess(JSONArray json) {
            	
               ArrayList<Tweet> tweets = Tweet.fromJson(json);
			   adapter.clear();
			   adapter.addAll(tweets);
			    
			   min_id = Tweet.getMinId(tweets, min_id);
			   //max_id = Tweet.getMinId(tweets, max_id);

                // ...the data has come back, finish populating listview...
                // Now we call onRefreshComplete to signify refresh has finished
            	//lvTweets.onRefreshComplete();
            }

            public void onFailure(Throwable e) {
                Log.d("DEBUG", "Fetch timeline error: " + e.toString());
            }
        }, 0);
    }
	
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
			Log.d("DEBUG", "tweet data is: " + d);

			e.printStackTrace();
			Toast.makeText(getApplicationContext(), "Tweets failed",
			        Toast.LENGTH_SHORT).show();
			
		  }
	  }
	} 

}

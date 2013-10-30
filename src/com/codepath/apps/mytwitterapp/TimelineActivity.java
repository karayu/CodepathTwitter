package com.codepath.apps.mytwitterapp;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.codepath.apps.mytwitterapp.fragments.HomeTimelineFragment;
import com.codepath.apps.mytwitterapp.fragments.MentionsFragment;
import com.codepath.apps.mytwitterapp.models.Tweet;
import com.codepath.apps.mytwitterapp.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

//Home icon designed by Edward Boatman
//Mentions icon designed by Jens Tarning 

public class TimelineActivity extends FragmentActivity implements TabListener {
	
	ArrayList<Tweet> tweets = new ArrayList<Tweet>();
	TweetsAdapter adapter;
	String tabTag = "";

	User user;
	
	
	public static int COMPOSE_REQUEST_CODE = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		setupNavigationTabs();
		
		MyTwitterClientApp.getRestClient().getUserAccount( new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject userDetails) {
				   user = new User(userDetails);
			   	   user.save();	
				   setupActionBar();

			}
			
			public void onFailure(Throwable e, JSONObject error) {
				Log.e("ERROR", e.toString());
				Toast.makeText(getApplicationContext(), "User not logged in!",
				        Toast.LENGTH_SHORT).show();
			}
			
		});	
		
	}
	
	public void setupActionBar() {
		ActionBar ab = getActionBar();
		ab.setTitle("@"+user.getScreenName());
	}

	private void setupNavigationTabs() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);
		
		Tab tabHome = actionBar.newTab().setText("Home")
			.setTag("HomeTimelineFragment").setIcon(R.drawable.ic_home)
			.setTabListener(this);
		
		Tab tabMentions = actionBar.newTab().setText("Mentions")
			.setTag("MentionsTimelineFragment").setIcon(R.drawable.ic_mentions)
			.setTabListener(this);

		
		actionBar.addTab(tabHome);
		actionBar.addTab(tabMentions);
		actionBar.selectTab(tabHome);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.timeline, menu);
		return true;
	}
	
	//launches the profile view
	public void onProfileView(MenuItem mi) {
		Intent i = new Intent(this, ProfileActivity.class);
		startActivity(i);
		
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

	
	
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		FragmentManager manager = getSupportFragmentManager();
		android.support.v4.app.FragmentTransaction fts = manager.beginTransaction();
		
		if(tab.getTag() == "HomeTimelineFragment") {
			//fragment to home timeline
			HomeTimelineFragment h = new HomeTimelineFragment();
			fts.replace(R.id.frame_container, h);
			h.setTagType("HomeTimelineFragment");
			
		} else {
			MentionsFragment m = new MentionsFragment();
			fts.replace(R.id.frame_container, m);
			m.setTagType("MentionsTimelineFragment");

		}
		
		fts.commit();
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
	} 
	
	public String getTab() {
		return tabTag;
	}

}

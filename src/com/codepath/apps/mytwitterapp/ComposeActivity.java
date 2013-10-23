package com.codepath.apps.mytwitterapp;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.mytwitterapp.models.Tweet;
import com.codepath.apps.mytwitterapp.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;


public class ComposeActivity extends Activity {
	
	private RelativeLayout rlButtons;
	private User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose);
		
		//set gray background for buttons
		rlButtons = (RelativeLayout)findViewById(R.id.rlButtons);
		rlButtons.setBackgroundColor(Color.LTGRAY);
		
		MyTwitterClientApp.getRestClient().getUserAccount( new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject userDetails) {
			   user = new User(userDetails);
			   setup();
			}
			
			public void onFailure(Throwable e, JSONObject error) {
				// Handle the failure and alert the user to
				// retry
				Log.e("ERROR", e.toString());
				Toast.makeText(getApplicationContext(), "Could not find user details",
				        Toast.LENGTH_SHORT).show();
			}
			
		});
		


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.compose, menu);
		return true;
	}
	
	public void setup() {
		ImageView ivUserPic = (ImageView)findViewById(R.id.ivUserPic);
		ImageLoader.getInstance().displayImage(user.getProfileImageUrl(), ivUserPic);
		
		TextView tvUserHandle = (TextView)findViewById(R.id.tvUserHandle);
		String formattedName = "<b>  @" + user.getScreenName() + "<b>";
		tvUserHandle.setText(Html.fromHtml(formattedName));
		
	}
	
	//action for cancel button. return with result, RESULT_CANCELED
	public void cancel(View v) {
		
		setResult(RESULT_CANCELED);
		finish();
		
	}
	
	//action for the tweet button. checks to see if you entered a tweet, if so, submits it with code RESULT_OK
	public void tweet(View v) {
		EditText etTweet = (EditText) findViewById(R.id.etTweet);
		
		if( etTweet.getText() == null || etTweet.getText().toString().isEmpty() ) {
			Toast.makeText(this, "Please enter text for tweet",
			        Toast.LENGTH_SHORT).show();
		}
		else if ( etTweet.getText().length() > 140 ){
			Toast.makeText(this, "You cannot exceed 140 chars",
			        Toast.LENGTH_SHORT).show();
		}
		else {
			
			Log.d("DEBUG", "sending tweet");
			MyTwitterClientApp.getRestClient().tweet( new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(JSONObject tweetDetails) {
				   Log.d("DEBUG", "Successful Post!"); 
				   Log.d("DEBUG", "Received response is:" + tweetDetails);
				   
				   Toast.makeText(getApplicationContext(), "Successful tweet",
					        Toast.LENGTH_SHORT).show();
				  
				   Intent data = new Intent();
				   data.putExtra("tweet", tweetDetails.toString());
				   setResult(RESULT_OK, data);
				   finish();
				   
				}
				
				public void onFailure(Throwable e, JSONObject error) {
					// Handle the failure and alert the user to
					// retry
					Log.e("ERROR", e.toString());
					Toast.makeText(getApplicationContext(), "Tweet failed",
					        Toast.LENGTH_SHORT).show();
				}
				
				
			}, etTweet.getText().toString());
			

		}
	}

}

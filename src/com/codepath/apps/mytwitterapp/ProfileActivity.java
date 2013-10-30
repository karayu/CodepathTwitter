package com.codepath.apps.mytwitterapp;

import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.mytwitterapp.fragments.UserTimelineFragment;
import com.codepath.apps.mytwitterapp.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ProfileActivity extends FragmentActivity {

	User u;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        
        //putting in a try/catch because i'm not sure what happens when you didn't store soemthing in an intent
        try {
        	String screenname = (String) getIntent().getSerializableExtra("screenname");
        	
        	//UserTimelineFragment utfrag = (UserTimelineFragment)findViewById(R.id.fragmentUserTimeline);
        		//	setScreenname(screenname);
        	
    		Log.d("DEBUG", "intent's passed screenname is: " + screenname);

        	if(screenname!=null && !screenname.isEmpty())
        	{
        		loadUserProfileInfo(screenname);
        	}
        	else {
                loadProfileInfo();
        	}
        }
        catch (Exception e) {
        	e.printStackTrace();
        }

    }

    private void loadUserProfileInfo(String userscreenname) {
    	
  		  Log.d("DEBUG", "loading user profile info for: " + userscreenname);
    	  MyTwitterClientApp.getRestClient().getUserInfo( userscreenname, new JsonHttpResponseHandler() {

  			@Override
  			public void onSuccess(JSONObject json) {
  	  		      Log.d("DEBUG", "returned json: " + json);

  				  User u = new User(json);
  				  getActionBar().setTitle("@"+u.getScreenName());
  				  populateProfileHeader(u);

  			}
  			
  			public void onFailure(Throwable e, JSONObject error) {
  				Log.e("ERROR", e.toString());
  				Toast.makeText(getApplicationContext(), "Could not get your info!",
  				        Toast.LENGTH_SHORT).show();
  			}
  			
  		}, 0);	 		
	}

	public void loadProfileInfo() {
        MyTwitterClientApp.getRestClient().getMyInfo( new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject json) {
				  User u = new User(json);
				  getActionBar().setTitle("@"+u.getScreenName());
				  populateProfileHeader(u);

			}
			
			public void onFailure(Throwable e, JSONObject error) {
				Log.e("ERROR", e.toString());
				Toast.makeText(getApplicationContext(), "Could not get your info!",
				        Toast.LENGTH_SHORT).show();
			}
			
		});	 
    }
    
    protected void populateProfileHeader(User user) {
    	TextView tvName = (TextView)findViewById(R.id.tvName);
    	TextView tvTagline = (TextView)findViewById(R.id.tvTagline);
    	TextView tvFollowers = (TextView)findViewById(R.id.tvFollowers);
    	TextView tvFollowing = (TextView)findViewById(R.id.tvFollowing);
    	ImageView ivProfileImage = (ImageView)findViewById(R.id.ivProfileImage);
    	
    	tvName.setText(user.getName());
    	tvTagline.setText(user.getTagline());
    	tvFollowers.setText(user.getFollowers() + " Followers");
    	tvFollowing.setText(user.getFollowing() + " Following");
    	
    	ImageLoader.getInstance().displayImage(user.getProfileImageUrl(), ivProfileImage);
    	

	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile, menu);
        return true;
    }
    
}

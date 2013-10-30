package com.codepath.apps.mytwitterapp.models;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

/*
 * This is a temporary, sample model that demonstrates the basic structure
 * of a SQLite persisted Model object. Check out the ActiveAndroid wiki for more details:
 * https://github.com/pardom/ActiveAndroid/wiki/Creating-your-database-model
 * 
 */
@Table(name = "Tweets")
public class Tweet extends Model {
	

	  // Define database columns and associated fields
	  @Column(name="user")
	  User user;
	  @Column(name = "timestamp")
	  String timestamp;
	  @Column(name = "text")
	  String text;
	  @Column(name = "tweet_id")
	  Long tweet_id;

	
	
	public Tweet(JSONObject object){
		super();
		
	    try {
	 
	        this.user = new User( object.getJSONObject("user"));
	        this.timestamp = object.getString("created_at");
	        this.text = object.getString("text");
	        this.tweet_id = object.getLong("id");
	        
	        
	      } catch (JSONException e) {
	        e.printStackTrace();
	      }

	}
	
	//generate an arraylist of tweets from a JSONArray, save them to ActiveAndroid model
	public static ArrayList<Tweet> fromJson(JSONArray jsonArray) {
		ArrayList<Tweet> tweets = new ArrayList<Tweet>(jsonArray.length());

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject tweetJson = null;
			try {
				tweetJson = jsonArray.getJSONObject(i);
				
				Tweet tweet = new Tweet(tweetJson);

				if (tweet != null) {
					tweets.add(tweet);
					
					//Log.d("DEBUG", "saving tweet " + tweet);
					//saves to persistence via ActiveAndroid
					User.saveUser(tweet.getUser());
					
					tweet.save();
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}


		}

		return tweets;
	}


	// Getters
	public User getUser() {
		return user;
	}
	
	public String getText() {
		return text;
	}
	
	public Long getTweetId() {
		return tweet_id;
	}
	
	
	// Record Finders
	public static Tweet byId(long id) {
	   return new Select().from(Tweet.class).where("id = ?", id).executeSingle();
	}
	
	public static ArrayList<Tweet> recentItems() {
      return new Select().from(Tweet.class).orderBy("id DESC").limit("300").execute();
	}
	
	//given an array of tweets, finds the lowest id of all of them. Used for infinite scroll. lower ids are older tweets
	public static long getMinId(ArrayList<Tweet> tweets, long initial_min_id) {
		long curr_id = 0;
		long min_id = initial_min_id;
		
		for (int i = 0; i < tweets.size(); i++) {
			curr_id = tweets.get(i).getTweetId();			
			
			if( min_id == 0) {
				min_id = curr_id;
			}
			else if( curr_id < min_id) {
				min_id = curr_id;
			}
		}		
		
		return min_id;
	}
	
	//given an array of tweets, finds the highest id of all of them. useful for since_id. not used
	public static long getMaxId(ArrayList<Tweet> tweets, long initial_max_id) {
		long curr_id = 0;
		long max_id = initial_max_id;
		
		for (int i = 0; i < tweets.size(); i++) {
			curr_id = tweets.get(i).getTweetId();			
			
			if( max_id == 0) {
				max_id = curr_id;
			}
			else if( curr_id > max_id) {
				max_id = curr_id;
			}
		}		
		
		return max_id;
	}
	
	
	
}

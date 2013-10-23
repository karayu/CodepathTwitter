package com.codepath.apps.mytwitterapp.models;

import java.io.Serializable;
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
	  @Column(name = "id")
	  Long id;

	
	
	public Tweet(JSONObject object){
		super();
		
	    try {
	 
	        this.user = new User( object.getJSONObject("user"));
	        this.timestamp = object.getString("created_at");
	        this.text = object.getString("text");
	        this.id = object.getLong("id");
	        
	        
	      } catch (JSONException e) {
	        e.printStackTrace();
	      }

	}
	
	/*public Tweet( User user, String timestamp, String text, long id) {
		this.user = user;
		this.timestamp = timestamp;
		this.text = text;
		this.id = id;
	}*/
	
	public static ArrayList<Tweet> fromJson(JSONArray jsonArray) {
		ArrayList<Tweet> tweets = new ArrayList<Tweet>(jsonArray.length());

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject tweetJson = null;
			try {
				tweetJson = jsonArray.getJSONObject(i);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}

			Tweet tweet = new Tweet(tweetJson);

			if (tweet != null) {
				tweets.add(tweet);
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
		return id;
	}
	
	
	// Record Finders
	public static Tweet byId(long id) {
	   return new Select().from(Tweet.class).where("id = ?", id).executeSingle();
	}
	
	public static ArrayList<Tweet> recentItems() {
      return new Select().from(Tweet.class).orderBy("id DESC").limit("300").execute();
	}
	
	//given an array of tweets, finds the lowest id of all of them
	public static long getMinId(ArrayList<Tweet> tweets, long min_id) {
		long curr_id = 0;
		
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
	
	
	
	
}

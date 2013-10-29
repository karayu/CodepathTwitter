package com.codepath.apps.mytwitterapp.models;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Users")
public class User extends Model {

	  @Column(name="name")
	  String name;
	  @Column(name = "profile_image_url")
	  String profile_image_url;
	  @Column(name = "screen_name")
	  String screen_name;
	  
	  
	public User(JSONObject object){
		super();
		
	    try {
	        //this.userId = object.getString("user.id_str");
	        //this.userHandle = object.getString("user.name");
	        this.name = object.getString("name");
	        this.profile_image_url = object.getString("profile_image_url");
	        this.screen_name = object.getString("screen_name");
	        
	      } catch (JSONException e) {
	        e.printStackTrace();
	      }

	}



	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getProfileImageUrl() {
		return profile_image_url;
	}


	public String getScreenName() {
		return screen_name;
	}
	
	public static void saveUser(User u) {
		u.save();
	}

}

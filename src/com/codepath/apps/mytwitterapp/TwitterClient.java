package com.codepath.apps.mytwitterapp;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;
import android.util.Log;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
    public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
    public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
    public static final String REST_CONSUMER_KEY = "Nmxayw0DMHOexwxiqD7USA";       // Change this
    public static final String REST_CONSUMER_SECRET = "Zvr4SGMWJC1aPqXUFFAE3x9NsXeEixmEmdMUIBoa4"; // Change this
    public static final String REST_CALLBACK_URL = "oauth://mytwitterapp"; // Change this (here and in manifest)
    
    public TwitterClient(Context context) {
        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
    }
    
    public void getHomeTimeline(AsyncHttpResponseHandler handler, long max_id) {
    	String url = getApiUrl("statuses/home_timeline.json");
    	
	   	RequestParams params = new RequestParams();
		params.put("count", String.valueOf(25)); 

	   	//if max_id != 0, it's not the first load and we set max_id
    	if( max_id != 0 ) {
    		//only return tweets smaller than the passed max_id, this is for scrolling
        	params.put("max_id", String.valueOf(max_id-1));
    	}
    	
    	client.get(url, params, handler);

    	//not sure how request only certain fields vs limit to fields with certain values
    	/*RequestParams params = new RequestParams();
    	params.put("page", String.valueOf(page));
    	getClient().get(apiUrl, params, handler);*/
    	

    }
    
    public void getMentions(AsyncHttpResponseHandler handler) {
    	String url = getApiUrl("statuses/mentions_timeline.json");
    	
	   	//RequestParams params = new RequestParams();
		//params.put("count", String.valueOf(25)); 
		
    	client.get(url, null, handler);
    	
    }
    
    //gets new tweets since the id specified, could have been used for pull to request
    public void getNewTweets(AsyncHttpResponseHandler handler, long since_id) {
    	String url = getApiUrl("statuses/home_timeline.json");
    	
	   	RequestParams params = new RequestParams();
		params.put("count", String.valueOf(25)); 

	   	//if max_id != 0, it's not the first load and we set max_id
    	if( since_id != 0 ) {
    		//only return tweets smaller than the passed max_id, this is for scrolling
        	params.put("since_id", String.valueOf(since_id));
    	}
    	else {
    		Log.d("DEBUG", "latest tweet id not set");
    	}
    	
    	client.get(url, params, handler);

    	//not sure how request only certain fields vs limit to fields with certain values
    	/*RequestParams params = new RequestParams();
    	params.put("page", String.valueOf(page));
    	getClient().get(apiUrl, params, handler);*/
    	

    }
    
    //get the user's account info
    public void getUserAccount(AsyncHttpResponseHandler handler) {
    	String url = getApiUrl("account/verify_credentials.json");
    	client.get(url,  null, handler);	
    }
    
    //tweet a message
    public void tweet(AsyncHttpResponseHandler handler, String msg) {
    	
       	String url = getApiUrl("statuses/update.json");
    	
	   	RequestParams params = new RequestParams();
		params.put("status", msg); 
    	client.post(url, params, handler);	

    }
    
    
    /* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
     * 	  i.e getApiUrl("statuses/home_timeline.json");
     * 2. Define the parameters to pass to the request (query or body)
     *    i.e RequestParams params = new RequestParams("foo", "bar");
     * 3. Define the request method and make a call to the client
     *    i.e client.get(apiUrl, params, handler);
     *    i.e client.post(apiUrl, params, handler);
     */
}
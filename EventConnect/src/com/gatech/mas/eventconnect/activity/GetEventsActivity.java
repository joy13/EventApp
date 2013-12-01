package com.gatech.mas.eventconnect.activity;

import java.net.URI;
import java.util.ArrayList;

import org.json.JSONException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.gatech.mas.eventconnect.R;
import com.gatech.mas.eventconnect.common.Event;
import com.gatech.mas.eventconnect.common.EventConnectConstants;
import com.gatech.mas.eventconnect.common.ListEventsArrayAdapter;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.app.ActionBar;
import android.app.ListActivity;

public class GetEventsActivity extends ListActivity {

	ArrayList<Event> eventItems;
	Intent intent;
	String sessionName;
	String sessionId;
	String api;
	String apiLocation = "http://dev.m.gatech.edu/d/sbajaj9/w/eventconnect/c/api/allevents";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		intent = getIntent();
		sessionName = intent.getExtras().getString(EventConnectConstants.SESSION_NAME);
		sessionId = intent.getExtras().getString(EventConnectConstants.SESSION_ID);
		api = intent.getExtras().getString(EventConnectConstants.API);
		if(api.equalsIgnoreCase(EventConnectConstants.ALL_EVENTS))
		{
			apiLocation = EventConnectConstants.ALLEVENTS_APILOCATION;
		}
		else if(api.equalsIgnoreCase(EventConnectConstants.NEW_EVENTS))
		{
			apiLocation = EventConnectConstants.NEWEVENTS_APILOCATION;
		} 
		else if(api.equalsIgnoreCase(EventConnectConstants.MY_EVENTS))
		{
			
			apiLocation = EventConnectConstants.MYEVENTS_APILOCATION;
		} 
		new FetchEventsTask().execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		api = intent.getExtras().getString(EventConnectConstants.API);
		MenuInflater inflater = getMenuInflater();
		if(api.equalsIgnoreCase(EventConnectConstants.MY_EVENTS))
		{
			inflater.inflate(R.menu.add_event, menu);
			/*
			menu.add(R.drawable.ic_new_event);
			inflater.inflate(R.menu.get_all_events, menu);
			*/
			
		} 
		else {
			inflater.inflate(R.menu.get_all_events, menu);
		}
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_new_event:
	        	
	            Intent i = new Intent(getApplicationContext(), CreateEventActivity.class);
	            i.putExtra(EventConnectConstants.SESSION_NAME, sessionName);
	    		i.putExtra(EventConnectConstants.SESSION_ID, sessionId);
	            startActivity(i);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	public class FetchEventsTask extends AsyncTask<String, Integer, ArrayList<Event>> {
		@Override
		protected ArrayList<Event> doInBackground(String... params) {
			eventItems = new ArrayList<Event>();
			Log.e("gtconnect", "in fetch events");
			try {
				URI api = new URI(apiLocation);
				HttpClient client = new DefaultHttpClient();
				HttpGet request = new HttpGet();
				request.setURI(api);
				request.setHeader("Cookie", sessionName+"="+sessionId);

				HttpResponse response = client.execute(request);
				HttpEntity entity = response.getEntity();
				Log.e("gtconnect", "got entity");
				if (entity!= null)
				{
					Log.e("gtconnect", "entity not null");
				}
				String str = EntityUtils.toString(entity);
				Log.e("gtconnect", "str length is" + str.length());
				try {

					JSONArray JsonArrayForResult = new JSONArray(str);

					for (int i = 0; i < JsonArrayForResult.length(); i++) {
						JSONObject jsonObject = JsonArrayForResult.getJSONObject(i);
						eventItems.add(new Event(jsonObject));
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			} catch (Exception e) {
				Log.e("log_tag", "Error in http connection: " + e.toString());
				e.printStackTrace();
			}
			
			return eventItems;
		}
		
		@Override
		protected void onPostExecute(ArrayList<Event> result) {
			super.onPostExecute(result);
			Message message = new Message();
			handler.sendMessage(message);
			}
		
		private Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {

				super.handleMessage(msg);
				ListEventsArrayAdapter listEventsArrayAdapter = new ListEventsArrayAdapter(getApplicationContext(),eventItems);
				setListAdapter(listEventsArrayAdapter);
				Log.e("gtconnect", "adapter set");
				listEventsArrayAdapter.setNotifyOnChange(true);
			}
		};
		
	}


	}


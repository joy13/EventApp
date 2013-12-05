package com.gatech.mas.eventconnect.activity;

import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.JSONException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.view.LayoutInflater;

import com.gatech.mas.eventconnect.R;
import com.gatech.mas.eventconnect.common.Event;
import com.gatech.mas.eventconnect.common.EventConnectConstants;
import com.gatech.mas.eventconnect.common.ListEventsArrayAdapter;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.text.Editable;
import android.text.TextWatcher;

public class GetEventsActivity extends ListActivity implements LocationListener {

	ArrayList<Event> eventItems;
	Intent intent;
	String sessionName;
	String sessionId;
	String api;
	String apiLocation = "http://dev.m.gatech.edu/d/sbajaj9/w/eventconnect/c/api/allevents";
	double latitude;
	double longitude;
	boolean isReco = false;
	
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
			new FetchEventsTask().execute();
		}
		else if(api.equalsIgnoreCase(EventConnectConstants.NEW_EVENTS))
		{
			apiLocation = EventConnectConstants.NEWEVENTS_APILOCATION;
			new FetchEventsTask().execute();
		} 
		else if(api.equalsIgnoreCase(EventConnectConstants.MY_EVENTS))
		{
			
			apiLocation = EventConnectConstants.MYEVENTS_APILOCATION;
			new FetchEventsTask().execute();
		}
		else if(api.equalsIgnoreCase(EventConnectConstants.RECOMMENDED_EVENTS))
		{
			isReco = true;
			apiLocation = EventConnectConstants.RECOMMENDEDEVENTS_APILOCATION;
			String providerName = LocationManager.GPS_PROVIDER;
			LocationManager locManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
			if (providerName != null && locManager.isProviderEnabled(providerName)) {
				locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0, this);
				new FetchEventsTask().execute();
			}
			else
			{
				Toast.makeText(GetEventsActivity.this, EventConnectConstants.TURN_ON_GPS,Toast.LENGTH_LONG).show();
	            Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	            GetEventsActivity.this.startActivityForResult(myIntent, 100);	            
			}
		} 	
		
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
			Log.e("gtconnect", "in fetch events, apilocation is: " +apiLocation);
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
					Log.e("GT_CONNECT", "location of event 2 "+eventItems.get(2).title 
							+ eventItems.get(2).latitude + eventItems.get(2).longitude);
					
					if (isReco)
					{
						ArrayList<Event> temp = new ArrayList<Event>();
						String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
						for (Event event: eventItems)
						{
							Log.e("GT_CONNECT", "in for loop ");
							String date[] = event.time.split("\\s+");
							event.date = date[0];
							if(event.date.equalsIgnoreCase(timeStamp))
							{
								temp.add(event);
								Log.e("GT_CONNECT", "added event with date " +event.date);
							}
						}
						if(temp.size() != 0)
						{	
							for (Event event: temp)
							{
								Log.e("GT_CONNECT", "looping over temp, event latitutde is " +event.latitude
										+"event longitude is " +event.longitude);
								event.distance = Math.sqrt(Math.pow(Double.valueOf(event.latitude)-latitude, 2) 
										+ Math.pow(Double.valueOf(event.longitude)-longitude,2));
								Log.e("GT_CONNECT", "distance is " +event.distance);
							}
							eventItems = null;
							eventItems = new ArrayList<Event>();
							for(Event event: temp)
							{
								eventItems.add(event);
							}
							Log.e("GT_CONNECT", "size of event list " +eventItems.size());
							Collections.sort(eventItems, new Comparator<Event>() {
						        @Override
						        public int compare(Event event1, Event event2)
						        {
						            return(event1.distance < event2.distance ? 1 : -1);
						        }
						    });
						}
						else
						{
							eventItems.clear();

						}
					}
				} catch (JSONException e) {
					Log.e("GT_CONNECT", e.getMessage());
				}
			} catch (Exception e) {
				Log.e("gtconnect", "Error in http connection: " + e.toString());
				e.printStackTrace();
			}
			Log.e("GT_CONNECT", "returning event list of size " +eventItems.size());
			return eventItems;
		}		
		
		@Override
		protected void onPostExecute(ArrayList<Event> result) {
			super.onPostExecute(result);
			if (result.size() == 0) {
				/*
				AlertDialog alertDialog = new AlertDialog.Builder(GetEventsActivity.this).create();
            	alertDialog.setMessage("No events for today!!");
                alertDialog.show();
                */
                Toast toast = Toast.makeText(GetEventsActivity.this, "No events for today!!", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
			}
			Message message = new Message();
			handler.sendMessage(message);
			}
		
		private Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {

				super.handleMessage(msg);
				String [] info = {api, sessionName, sessionId};
				ListEventsArrayAdapter listEventsArrayAdapter = new ListEventsArrayAdapter(getApplicationContext(),eventItems, info);
				setListAdapter(listEventsArrayAdapter);
				Log.e("gtconnect", "adapter set");
				listEventsArrayAdapter.setNotifyOnChange(true);
			}
		};
		
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		latitude = location.getLatitude();
		longitude = location.getLongitude();
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			  if(requestCode == 100 && resultCode == 0){
				  LocationManager locManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
				  String providerName = LocationManager.GPS_PROVIDER;
		            if(locManager.isProviderEnabled(providerName)){
		                Log.e("GTCONNECT", " Location providers: "+ providerName);
		                //Start searching for location and update the location text when update available. 
		                // Do whatever you want
		                locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0, this);
		                Log.e("GT_CONNECT", "USER ENABLED GPS");
		                new FetchEventsTask().execute();
		            }
		            else{
		            	Log.e("GT_CONNECT", "USER DID NOT enable GPS");
		            	AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		            	alertDialog.setMessage("Please turn on GPS to see the nearest events!");
		                alertDialog.show();
		            }
		        }
		  }
	
	public static double distFrom(double lat1, double lng1, double lat2, double lng2) {
		Log.e("GT_CONNECT","in distance function");
	    return Math.sqrt(Math.pow(lat1-lat2, 2) + Math.pow(lng1-lng2,2));
		}

}
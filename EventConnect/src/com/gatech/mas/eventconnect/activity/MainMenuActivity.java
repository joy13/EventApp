package com.gatech.mas.eventconnect.activity;

import java.net.URI;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.gatech.mas.eventconnect.R;
import com.gatech.mas.eventconnect.common.Event;
import com.gatech.mas.eventconnect.common.EventAdapter;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;
import android.view.View;

public class MainMenuActivity extends Activity {

	//private final String apiLocation = "http://dev.m.gatech.edu/d/sbajaj9/w/eventconnect/c/api/newevents";
	String sessionName;
	String sessionId;
	ArrayList<Event> eventItems;
	public final static String SESSION_NAME = "com.gatech.mas.eventconnect.activity.SESSION_NAME";
	public final static String SESSION_ID = "com.gatech.mas.eventconnect.activity.SESSION_ID";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
		GridView gridview = (GridView) findViewById(R.id.gridView);
	    gridview.setAdapter(new EventAdapter(this));
	    gridview.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	            Toast.makeText(MainMenuActivity.this, "" + position, Toast.LENGTH_SHORT).show();
	        }
	    });
		Intent intent = getIntent();
		// To get the action of the intent use
		String action = intent.getAction();

		if (!action.equals(Intent.ACTION_VIEW)) {
			throw new RuntimeException("Should not happen");
		}
		// To get the data use
		Uri data = intent.getData();
		sessionName = data.getQueryParameter("sessionName");
		sessionId = data.getQueryParameter("sessionId");
//		new FetchEventsTask().execute();
		Intent sessionInfoIntent = new Intent(this, GetAllEventsActivity.class);
		sessionInfoIntent.putExtra(SESSION_NAME, sessionName);
		sessionInfoIntent.putExtra(SESSION_ID, sessionId);		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}
	
}

package com.gatech.mas.eventconnect.activity;

import java.util.ArrayList;

import com.gatech.mas.eventconnect.R;
import com.gatech.mas.eventconnect.common.CustomAdapter;
import com.gatech.mas.eventconnect.common.Event;
import com.gatech.mas.eventconnect.common.EventConnectConstants;
import com.gatech.mas.eventconnect.common.GridMenuAdapter;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
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
	Intent intent;
	ArrayList<Event> eventItems;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
		GridView gridview = (GridView) findViewById(R.id.gridView);
	    // gridview.setAdapter(new GridMenuAdapter(this));
		gridview.setAdapter(new CustomAdapter(this, getData()));
	 
	    intent = getIntent();
		// To get the action of the intent use
		String action = intent.getAction();

		if (!action.equals(Intent.ACTION_VIEW)) {
			throw new RuntimeException("Should not happen");
		}
	    gridview.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	            //Toast.makeText(MainMenuActivity.this, "" + position, Toast.LENGTH_SHORT).show();
	        	Intent sessionInfoIntent;
	            Uri data = intent.getData();
	    		sessionName = data.getQueryParameter("sessionName");
	    		sessionId = data.getQueryParameter("sessionId");
//	    		new FetchEventsTask().execute();
	    		sessionInfoIntent = new Intent(getApplicationContext(), GetEventsActivity.class);
	    		if(position == 0)
	    		{
	    			sessionInfoIntent.putExtra(EventConnectConstants.API, EventConnectConstants.ALL_EVENTS);
	    		}
	    		else if(position == 1)
	    		{
	    			sessionInfoIntent.putExtra(EventConnectConstants.API, EventConnectConstants.NEW_EVENTS);
	    		}
	    		else if(position == 2)
	    		{
	    			sessionInfoIntent.putExtra(EventConnectConstants.API, EventConnectConstants.MY_EVENTS);
	    		}
	    		else if(position == 3)
	    		{
	    			sessionInfoIntent.putExtra(EventConnectConstants.API, EventConnectConstants.MY_EVENTS);
	    		}
	    		sessionInfoIntent.putExtra(EventConnectConstants.SESSION_NAME, sessionName);
	    		sessionInfoIntent.putExtra(EventConnectConstants.SESSION_ID, sessionId);	
	    		startActivity(sessionInfoIntent);
	        }
	    });
		
		// To get the data use
		Uri data = intent.getData();
		sessionName = data.getQueryParameter("sessionName");
		sessionId = data.getQueryParameter("sessionId");
//		new FetchEventsTask().execute();
		Intent sessionInfoIntent = new Intent(this, GetEventsActivity.class);
		sessionInfoIntent.putExtra(EventConnectConstants.SESSION_NAME, sessionName);
		sessionInfoIntent.putExtra(EventConnectConstants.SESSION_ID, sessionId);		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}
	
	public ArrayList<Integer> getData(){
        ArrayList<Integer> list = new ArrayList<Integer>();
        list.add(R.drawable.allevents);
        // add 2 - 11
        list.add(R.drawable.newevents);
        
        list.add(R.drawable.recommended);
        
        list.add(R.drawable.myevents);

        return list;
    }
	
}

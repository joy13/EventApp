package com.gatech.mas.eventconnect.common;



import java.util.ArrayList;

import com.gatech.mas.eventconnect.R;
import com.gatech.mas.eventconnect.activity.CreateEventActivity;
import com.gatech.mas.eventconnect.activity.CreateEventActivity.SelectDateFragment;
import com.gatech.mas.eventconnect.activity.EditEventActivity;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ListEventsArrayAdapter extends ArrayAdapter<Event> {
	public static final int FLAG_ACTIVITY_NEW_TASK = 268435456;
	private final Context context;
	ArrayList<Event> eventList;
	String strEvent;
	String api;
	String sessionName;
	String sessionId;
	public ListEventsArrayAdapter(Context context, ArrayList<Event> eventList, String [] info) {
		super(context, R.layout.list_events, eventList);
		this.context = context;
		this.eventList = eventList;
		this.api = info[0];
		this.sessionName = info[1];
		this.sessionId = info[2];
		// TODO Auto-generated constructor stub
	}




	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.e("gtconnect", "in get view of adapter");
		LayoutInflater inflater = (LayoutInflater) context
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
		View rowView = inflater.inflate(R.layout.list_events, parent, false);

		TextView textView = (TextView) rowView.findViewById(R.id.label);
		
		final Event event = this.eventList.get(position);
		
		
		
	
		
		OnClickListener l = new OnClickListener() { public void onClick(View v) {
			if(api.equalsIgnoreCase(EventConnectConstants.MY_EVENTS)) {
			
			Intent intent;
			intent = new Intent(v.getContext(), EditEventActivity.class);
			intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
			
			String [] params = {event.title, event.time, event.location, event.body, event.event_id};
			intent.putExtra("EventParameters", params);
			intent.putExtra(EventConnectConstants.SESSION_NAME, sessionName);
			intent.putExtra(EventConnectConstants.SESSION_ID, sessionId);
			v.getContext().startActivity(intent);
			}
			else {
				CharSequence text = "Hello toast!";
				int duration = Toast.LENGTH_SHORT;

				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
				
			}
				
		} };
		rowView.setOnClickListener(l);
		
		
		if(event != null){
			textView.setTextColor(Color.BLACK);
			textView.setBackgroundColor(Color.TRANSPARENT);
			strEvent = "<b>" + event.title + "</b>" + " on " + "<b>" + event.time + "</b>" + " at " + "<b>" + 
						event.location + "</b>" + "<br>" + event.body;
			textView.setText(Html.fromHtml(strEvent));
		}
 
		return rowView;
	}
	
	public void editActivity(View view) {

		CharSequence text = "Hello toast!";
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
		
	}
	



}

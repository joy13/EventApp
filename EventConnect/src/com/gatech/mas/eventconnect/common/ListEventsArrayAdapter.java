package com.gatech.mas.eventconnect.common;

import java.util.ArrayList;

import com.gatech.mas.eventconnect.R;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ListEventsArrayAdapter extends ArrayAdapter<Event> {
	private final Context context;
	ArrayList<Event> eventList;
	String strEvent;
	public ListEventsArrayAdapter(Context context, ArrayList<Event> eventList) {
		super(context, R.layout.list_events, eventList);
		this.context = context;
		this.eventList = eventList;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.e("gtconnect", "in get view of adapter");
		LayoutInflater inflater = (LayoutInflater) context
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
		View rowView = inflater.inflate(R.layout.list_events, parent, false);

		TextView textView = (TextView) rowView.findViewById(R.id.label);
		
		Event event = this.eventList.get(position);
		
		if(event != null){
			textView.setTextColor(Color.BLACK);
			textView.setBackgroundColor(Color.TRANSPARENT);
			strEvent = "<b>" + event.title + "</b>" + " on " + "<b>" + event.time + "</b>" + " at " + "<b>" + 
						event.location + "</b>" + "<br>" + event.body;
			textView.setText(Html.fromHtml(strEvent));
		}
 
		return rowView;
	}

}

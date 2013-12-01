package com.gatech.mas.eventconnect.common;

import org.json.JSONException;
import org.json.JSONObject;

public class Event {

	public String title;
	public String time;
	public String location;
	public String body;
	public String timeOfCreation;
	
	public  Event(JSONObject jObject) {
		try {
			title = jObject.getString("title");
			time = jObject.getString("event_time");
			location = jObject.getString("location");
			body = jObject.getString("body");
			timeOfCreation = jObject.getString("time_of_event_creation");
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
}

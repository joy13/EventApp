package com.gatech.mas.eventconnect.common;

import org.json.JSONException;
import org.json.JSONObject;

public class Event {
	
	public String event_id;
	public String title;
	public String time;
	public String location;
	public String body;
	public String buildingName;
	public String latitude;
	public String longitude;
	public double distance = 0;
	public String date;
	
	public  Event(JSONObject jObject) {
		try {
			event_id = jObject.getString("event_id");
			title = jObject.getString("title");
			time = jObject.getString("event_time");
			location = jObject.getString("location");
			buildingName = jObject.getString("building_name");
			body = jObject.getString("body");
			latitude = jObject.getString("latitude");
			longitude = jObject.getString("longitude");
			distance = 0;
			date = "dummy";
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
}

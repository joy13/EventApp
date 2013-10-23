package com.gatech.mas.eventconnect.activity;

import com.gatech.mas.eventconnect.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class GetAllEventsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_get_all_events);
//		new FetchEventsTask().execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.get_all_events, menu);
		return true;
	}
	
//	public class FetchEventsTask extends AsyncTask<String, Integer, ArrayList<Event>> {
//		@Override
//		protected ArrayList<Event> doInBackground(String... params) {
//			eventItems = new ArrayList<Event>();
//			try {
//				URI api = new URI(apiLocation);
//				HttpClient client = new DefaultHttpClient();
//				HttpGet request = new HttpGet();
//				request.setURI(api);
//				request.setHeader("Cookie", sessionName+"="+sessionId);
//
//				HttpResponse response = client.execute(request);
//				HttpEntity entity = response.getEntity();
//				String str = EntityUtils.toString(entity);
//				try {
//
//					JSONArray JsonArrayForResult = new JSONArray(str);
//
//					for (int i = 0; i < JsonArrayForResult.length(); i++) {
//						JSONObject jsonObject = JsonArrayForResult.getJSONObject(i);
//						eventItems.add(new Event(jsonObject));
//					}
//
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
//			} catch (Exception e) {
//				Log.e("log_tag", "Error in http connection: " + e.toString());
//				e.printStackTrace();
//			}
//			
//			return eventItems;
//		}
		
//		@Override
//		protected void onPostExecute(ArrayList<Event> result) {
//			super.onPostExecute(result);
//			//TextView textView = (TextView)findViewById(R.id.label);			
//			if(eventItems != null){
//				Event event = eventItems.get(0);
//				//textView.setText(event.title + "on" + event.time + "at" + event.location);
//			}
//		}


//	}

}

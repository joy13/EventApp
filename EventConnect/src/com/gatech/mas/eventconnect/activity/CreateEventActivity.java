package com.gatech.mas.eventconnect.activity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.gatech.mas.eventconnect.R;
import com.gatech.mas.eventconnect.common.EventConnectConstants;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.widget.DatePicker;

public class CreateEventActivity extends FragmentActivity {
	
	private static final int MY_DATE_DIALOG_ID = 0;
	
	EditText mEdit;
	
	private static String url = EventConnectConstants.MYEVENTS_APILOCATION;
	private int year;
	private int month;
	private int day;
	private int hour;
	private int min;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_event_form);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		
		return super.onCreateOptionsMenu(menu);
	}
	
	@SuppressLint("ValidFragment")
	public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
		
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			final Calendar calendar = Calendar.getInstance();
			int yy = calendar.get(Calendar.YEAR);
			int mm = calendar.get(Calendar.MONTH);
			int dd = calendar.get(Calendar.DAY_OF_MONTH);
			return new DatePickerDialog(getActivity(), this, yy, mm, dd);
		}
	 
		public void onDateSet(DatePicker view, int yy, int mm, int dd) {
			populateSetDate(yy, mm, dd);
			year = yy;
			month = mm;
			day = dd;
		}

	


	}

	public void selectDate(View view) {
		DialogFragment newFragment = new SelectDateFragment();
		newFragment.show(getFragmentManager(), "DatePicker");
	}
	
	public void populateSetDate(int year, int month, int day) {
		mEdit = (EditText)findViewById(R.id.editText1);
		mEdit.setText(month+"/"+day+"/"+year);
	}
	
	@SuppressLint("ValidFragment")
	public class SelectTimeFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
		
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			final Calendar calendar = Calendar.getInstance();
			int hour = calendar.get(Calendar.HOUR);
			int minute = calendar.get(Calendar.MINUTE);
			
			return new TimePickerDialog(getActivity(), this, hour, minute, true);
		}
		

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// TODO Auto-generated method stub
			populateSetTime(hourOfDay, minute, 0);
			hour = hourOfDay;
			min = minute;
		}

	}

	public void selectTime(View view) {
		DialogFragment newFragment = new SelectTimeFragment();
		newFragment.show(getFragmentManager(), "TimePicker");
	}
	
	public void populateSetTime(int hour, int minute, int second) {
		mEdit = (EditText)findViewById(R.id.editText2);
		mEdit.setText(hour+":"+ minute + ":" + "00");
	}

	
	public void submitEvent(View button) {
		new CreateNewEvent().execute();
		
		
	}
	
	/**
     * Background Async Task to Create new product
     * */
    class CreateNewEvent extends AsyncTask<String, Void, Boolean> {
 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
  
        }
 
        /**
         * Creating product
         * */
        protected Boolean doInBackground(String... args) {
    		EditText titleField = (EditText) findViewById(R.id.EditTitle);
    		EditText locationText = (EditText) findViewById(R.id.locationText);
    		EditText majorField = (EditText) findViewById(R.id.majorText);
    		EditText roomText = (EditText) findViewById(R.id.roomText);
    		EditText descriptionText = (EditText) findViewById(R.id.descriptionText);
    		final String title = titleField.getText().toString();
    		final String datetime = Integer.toString(year) + "-" + Integer.toString(month) + "/" + Integer.toString(day) + "/"  + " " + Integer.toString(hour) + ":" + Integer.toString(min) + " PM";
    		final String location = locationText.getText().toString();
    		final String room = roomText.getText().toString();
    		final String major = majorField.getText().toString();
    		final String description = descriptionText.getText().toString();
            // Building Parameters
    		/*
            List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
            params.add(new BasicNameValuePair ("title", title));
            params.add(new BasicNameValuePair("body", description));
            params.add(new BasicNameValuePair("location", room));
            params.add(new BasicNameValuePair("major", major));
            params.add(new BasicNameValuePair("building_id", "168"));
            */
    		Map<String, String> jsonMap = new HashMap<String, String>();
    		jsonMap.put("title", title);
    		jsonMap.put("body", description);
    		jsonMap.put("location", room);
    		jsonMap.put("major", major);
    		jsonMap.put("building_id", "168");
    		
    		JSONObject holder = new JSONObject(jsonMap);

 
            // getting JSON Object
            // Note that create product url accepts POST method
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            Intent i = getIntent();
            if(i == null)
            {
            	Log.e("gtconnect", "intent is null");
            }
            String sessionName = i.getExtras().getString(EventConnectConstants.SESSION_NAME);
            String sessionId = i.getExtras().getString(EventConnectConstants.SESSION_ID);
            httpPost.setHeader("Cookie", sessionName + "=" + sessionId);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-Type", "application/json");
            try {
            	StringEntity se = new StringEntity(holder.toString());
				httpPost.setEntity(se);
				HttpResponse httpResponse = httpClient.execute(httpPost);
				Log.e("HTTP Response", httpResponse.toString());
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
 
          

            return null;
        }
 
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
        	finish();

        }

    }

	
		
}

	





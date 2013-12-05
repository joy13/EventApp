package com.gatech.mas.eventconnect.activity;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.http.util.EntityUtils;





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

public class EditEventActivity extends FragmentActivity {
	
	private static final int MY_DATE_DIALOG_ID = 0;
	private String event_id;
	private EditText mEditDate;
	private EditText titleField;
	private EditText mEditTime;
	private EditText locationText;
	private EditText majorField;
	private EditText roomText;
	private EditText descriptionText;
	
	private static String url = EventConnectConstants.MYEVENTS_APILOCATION;
	private int year;
	private int month;
	private int day;
	private int hour;
	private int min;
	private String [] params;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_event_form);
		Intent intent = getIntent();
		params = intent.getExtras().getStringArray("EventParameters");
		titleField = (EditText)findViewById(R.id.EditTitle);
		titleField.setText(params[0]);
		mEditDate = (EditText)findViewById(R.id.editText1);
		mEditTime = (EditText)findViewById(R.id.editText2);
		String [] dateTime = params[1].split(" ");
		String [] date = dateTime[0].split("-");
		year = Integer.parseInt(date[0]);
		month = Integer.parseInt(date[1]);
		day = Integer.parseInt(date[2]);
		String [] time = dateTime[1].split(":");
		hour = Integer.parseInt(time[0]);
		min = Integer.parseInt(time[1]);
		mEditDate.setText(dateTime[0]);
		mEditTime.setText(dateTime[1]);
		roomText = (EditText) findViewById(R.id.roomText);
		roomText.setText(params[2]);
		descriptionText = (EditText) findViewById(R.id.descriptionText);
		descriptionText.setText(params[3]);
		event_id = params[4];
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
		
		mEditDate.setText(month+"/"+day+"/"+year);
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
	
		mEditTime.setText(hour+":"+ minute + ":" + "00");
	}

	
	public void updateEvent(View button) {
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
    		titleField = (EditText) findViewById(R.id.EditTitle);
    		locationText = (EditText) findViewById(R.id.locationText);
    		majorField = (EditText) findViewById(R.id.majorText);
    		roomText = (EditText) findViewById(R.id.roomText);
    		descriptionText = (EditText) findViewById(R.id.descriptionText);
    		final String title = titleField.getText().toString();
    		final String datetime = Integer.toString(year) + "-" + Integer.toString(month) + "-" + Integer.toString(day) + "T"  + Integer.toString(hour) + ":" + Integer.toString(min);
    		final String location = locationText.getText().toString();
    		final String room = roomText.getText().toString();
    		final String major = majorField.getText().toString();
    		final String description = descriptionText.getText().toString();
            // Building Parameters
    		
            List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
            params.add(new BasicNameValuePair ("title", title));
            params.add(new BasicNameValuePair("body", description));
            params.add(new BasicNameValuePair("location", room));
            params.add(new BasicNameValuePair("major", major));
            params.add(new BasicNameValuePair("building_id", "168"));
            /*
    		Map<String, String> jsonMap = new HashMap<String, String>();
    		
    		jsonMap.put("title", title);
    		jsonMap.put("body", description);
    		jsonMap.put("location", room);
    		jsonMap.put("major", major);
    		jsonMap.put("building_id", "168");
    		jsonMap.put("event_time", datetime);
    		
    		JSONObject holder = new JSONObject(jsonMap);
			*/
 

            DefaultHttpClient httpClient = new DefaultHttpClient();
            String urlPut = url + "/" + event_id;
            HttpPut httpPut = new HttpPut(urlPut);
            Intent i = getIntent();
            String sessionName = i.getExtras().getString(EventConnectConstants.SESSION_NAME);
            String sessionId = i.getExtras().getString(EventConnectConstants.SESSION_ID);
            httpPut.setHeader("Cookie", sessionName + "=" + sessionId);
            /*
            httpPut.setHeader("Accept", "application/json");
            httpPut.setHeader("Content-type", "application/json");
            */
            try {
            	// Add your data
            	
            	
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("title", title));
                nameValuePairs.add(new BasicNameValuePair("body", description));
                nameValuePairs.add(new BasicNameValuePair("location", room));
                nameValuePairs.add(new BasicNameValuePair("event_time", datetime));
                nameValuePairs.add(new BasicNameValuePair("major", major));
                nameValuePairs.add(new BasicNameValuePair("building_id", "168"));
                
                httpPut.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                
                /*
            	StringEntity se = new StringEntity(holder.toString());
				httpPut.setEntity(se);
				*/
                
                
				HttpResponse httpResponse = httpClient.execute(httpPut);
				HttpEntity entity = httpResponse.getEntity();
				String content = EntityUtils.toString(entity, EntityUtils.getContentCharSet(entity));
				Log.e("HTTP Response", content);
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

	





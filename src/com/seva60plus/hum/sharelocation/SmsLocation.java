package com.seva60plus.hum.sharelocation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.seva60plus.hum.R;
import com.seva60plus.hum.activity.DashboardActivity;
import com.seva60plus.hum.activity.HumDetailsView;
import com.seva60plus.hum.activity.MenuLay;
import com.seva60plus.hum.custom.NoInternetPage;
import com.seva60plus.hum.util.ConnectionDetector;
import com.seva60plus.hum.util.Util;

public class SmsLocation extends Activity {
	Button sathiCall1, sathiCall2, bothSMS;
	TextView headerTitleText;
	ImageView homeBtn;
	RelativeLayout backBtn;

	String call1, call2;
	String name1, name2;
	String LocationMsg = "", AddressMsg = "";

	String Lat, Lng;
	//String url = "https://www.google.co.in/maps/@";
	String msgUrl = "";

	//SpannableString url;
	String time, formattedDate;

	public static final String MY_PREFS_NAME = "MyPrefsFile";

	//---by Dibyendu
	// flag for Internet connection status
	Boolean isInternetPresent = false;

	// Connection detector class
	ConnectionDetector cd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sms_location);

		cd = new ConnectionDetector(getApplicationContext());// by Dibyendu

		LinearLayout banner = (LinearLayout) findViewById(R.id.footerLay);

		banner.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// Take action.
				/*
				 * Intent myIntent = new Intent(SmsLocation.this,
				 * AdBanner.class); myIntent.putExtra("banner_value", "2");
				 * startActivity(myIntent);
				 */
				Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.homers.in"));
				startActivity(i);

			}
		});

		//------Date & Time
		Calendar cal = Calendar.getInstance(TimeZone.getDefault());
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss a", Locale.getDefault());
		time = sdf.format(cal.getTime());

		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		formattedDate = df.format(cal.getTime());
		//--------

		SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
		call1 = prefs.getString("saathi1Call", null);
		call2 = prefs.getString("saathi2Call", null);
		name1 = prefs.getString("saathi1CallName", null);
		name2 = prefs.getString("saathi2CallName", null);

		//  AddressMsg = getIntent().getExtras().getString("msg");
		Lat = getIntent().getExtras().getString("lat");
		Lng = getIntent().getExtras().getString("lat");

		System.out.println("lat: " + Lat);
		System.out.println("lng: " + Lng);

		msgUrl = "https://www.google.co.in/maps/place/" + Lat + "," + Lng;
		
		LocationMsg = "Location : \n" + msgUrl + "\nTime : \n" + time + "\nDate : \n" + formattedDate;
		System.out.println(LocationMsg);
		//---End-------
		headerTitleText = (TextView) findViewById(R.id.header_title);
		headerTitleText.setText("SMS LOCATION");
		Typeface font = Typeface.createFromAsset(getAssets(), "openSansBold.ttf");
		headerTitleText.setTypeface(font);

		backBtn = (RelativeLayout) findViewById(R.id.back_settings);

		backBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (Util.isInternetAvailable(SmsLocation.this)) {
					Intent i = new Intent(SmsLocation.this, HumDetailsView.class);
					startActivity(i);
					overridePendingTransition(0, 0);
					finish();
				} else {
					Intent i = new Intent(SmsLocation.this, NoInternetPage.class);
					startActivity(i);
					overridePendingTransition(0, 0);

				}

			}
		});

		findViewById(R.id.llMenu).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// Take action.
				Intent myIntent = new Intent(SmsLocation.this, MenuLay.class);
				startActivity(myIntent);
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

			}
		});

		sathiCall1 = (Button) findViewById(R.id.sathiSms1);
		sathiCall1.setText("SMS " + name1 + "\n" + call1);
		sathiCall1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// Take action.
				sendLongSMS();

			}
		});

		sathiCall2 = (Button) findViewById(R.id.sathiSms2);
		sathiCall2.setText("SMS " + name2 + "\n" + call2);
		sathiCall2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// Take action.
				sendLongSMS1();

			}
		});

		bothSMS = (Button) findViewById(R.id.both_sms);
		bothSMS.setText("SMS\n" + name1 + "\n" + name2);
		bothSMS.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// Take action.
				System.out.println("LocMsg: " + LocationMsg);
				Intent i = new Intent(android.content.Intent.ACTION_VIEW);
				i.putExtra("address", call1 + "; " + call2);
				// here i can send message to emulator 5556,5558,5560
				// you can change in real device
				i.putExtra("sms_body", LocationMsg);
				i.setType("vnd.android-dir/mms-sms");
				startActivity(i);

			}
		});

	}

	public void sendLongSMS() {

		//String shop = getString(R.string.shop_number);

		String phoneNumber = call1;
		String message = LocationMsg;

		SmsManager smsManager = SmsManager.getDefault();
		ArrayList<String> parts = smsManager.divideMessage(message);
		smsManager.sendMultipartTextMessage(phoneNumber, null, parts, null, null);
		//Toast.makeText(getApplicationContext(), "Customer Name:"+custName+"\nPhone Number:"+phNumber+"\nMedicine Name:"+medicineName+"\nQuantity:"+quntNumber, Toast.LENGTH_SHORT).show();
		Toast.makeText(getApplicationContext(), "SMS SENT", Toast.LENGTH_SHORT).show();
	}

	public void sendLongSMS1() {

		//String shop = getString(R.string.shop_number);

		String phoneNumber = call2;
		String message = LocationMsg;

		SmsManager smsManager = SmsManager.getDefault();
		ArrayList<String> parts = smsManager.divideMessage(message);
		smsManager.sendMultipartTextMessage(phoneNumber, null, parts, null, null);
		//Toast.makeText(getApplicationContext(), "Customer Name:"+custName+"\nPhone Number:"+phNumber+"\nMedicine Name:"+medicineName+"\nQuantity:"+quntNumber, Toast.LENGTH_SHORT).show();
		Toast.makeText(getApplicationContext(), "SMS SENT", Toast.LENGTH_SHORT).show();
	}
	public void onHomeClick(View v) {
		startActivity(new Intent(SmsLocation.this, DashboardActivity.class));
		finish();
	}
}

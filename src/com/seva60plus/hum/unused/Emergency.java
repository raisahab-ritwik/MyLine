package com.seva60plus.hum.unused;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.seva60plus.hum.R;
import com.seva60plus.hum.activity.MenuLay;

public class Emergency extends Activity {
	Button sathiCall1, sathiCall2, policeBtn, ambuBtn;
	TextView headerTitleText;
	ImageView backBtn, homeBtn;

	String call1Pref, call2Pref;
	String name1Pref, name2Pref;
	String con1 = "";
	String con2 = "";
	String saathiName1, saathiName2;
	String name1Saathi, name2Saathi, call1Saathi, call2Saathi;//===for storing the value
	TelephonyManager tm;
	String subId;

	public static final String MY_PREFS_NAME = "MyPrefsFile";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.emergency);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
		call1Pref = prefs.getString("saathi1Call", null);
		call2Pref = prefs.getString("saathi2Call", null);
		name1Pref = prefs.getString("saathi1CallName", null);
		name2Pref = prefs.getString("saathi2CallName", null);

		tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		subId = tm.getSubscriberId().toString();
		System.out.println(subId);

		if (call1Pref.equals("") || call2Pref.equals("") || name1Pref.equals("") || name2Pref.equals("")) {

			JSONObject json1 = null;
			String str = "";
			HttpResponse response;
			HttpClient myClient = new DefaultHttpClient();
			HttpPost myConnection = new HttpPost("http://wrctec.com/androidApi/sevaGenie/register/checkUserExists/" + subId);

			try {
				response = myClient.execute(myConnection);
				str = EntityUtils.toString(response.getEntity(), "UTF-8");

			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				JSONArray jArray = new JSONArray(str);
				json1 = jArray.getJSONObject(0);

				//result = json1.getString("code");

				saathiName1 = json1.getString("sathi1_name");
				saathiName2 = json1.getString("sathi2_name");

				con1 = json1.getString("sathi1_contact_no");
				con2 = json1.getString("sathi2_contact_no");

				//  Toast.makeText(getApplicationContext(), sex, Toast.LENGTH_LONG).show();

			} catch (JSONException e) {
				e.printStackTrace();
			}

			//====Storeing The Variable====
			name1Saathi = saathiName1;
			name2Saathi = saathiName2;
			call1Saathi = con1;
			call2Saathi = con2;
			//===Storeing complete====
		} else {
			name1Saathi = name1Pref;
			name2Saathi = name2Pref;
			call1Saathi = call1Pref;
			call2Saathi = call2Pref;
		}//===Storeing complete====

		headerTitleText = (TextView) findViewById(R.id.header_title);
		headerTitleText.setText("EMERGENCY");
		Typeface font = Typeface.createFromAsset(getAssets(), "openSansBold.ttf");
		headerTitleText.setTypeface(font);

		backBtn = (ImageView) findViewById(R.id.iv_back);

		backBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// Take action.
				finish();

			}
		});

		findViewById(R.id.llMenu).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// Take action.
				Intent myIntent = new Intent(Emergency.this, MenuLay.class);
				startActivity(myIntent);
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

			}
		});

		sathiCall1 = (Button) findViewById(R.id.rlSaathiView);
		sathiCall1.setText("Call " + name1Saathi + "\n" + call1Saathi);
		sathiCall1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// Take action.
				String phnum = getString(R.string.sathi1);
				Intent callIntent = new Intent(Intent.ACTION_CALL);
				callIntent.setData(Uri.parse("tel:" + call1Saathi));
				startActivity(callIntent);

			}
		});

		sathiCall2 = (Button) findViewById(R.id.sathiCall2);
		sathiCall2.setText("Call " + name2Saathi + "\n" + call2Saathi);
		sathiCall2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// Take action.
				String phnum = getString(R.string.sathi2);
				Intent callIntent = new Intent(Intent.ACTION_CALL);
				callIntent.setData(Uri.parse("tel:" + call2Saathi));
				startActivity(callIntent);

			}
		});

		policeBtn = (Button) findViewById(R.id.police);
		policeBtn.setText("Police\n100");
		policeBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// Take action.
				String phnum = "100";
				Intent callIntent = new Intent(Intent.ACTION_CALL);
				callIntent.setData(Uri.parse("tel:" + phnum));
				startActivity(callIntent);

			}
		});

		ambuBtn = (Button) findViewById(R.id.ambulance);
		ambuBtn.setText("Fire Brigade\n101");
		ambuBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// Take action.
				String phnum1 = "101";
				Intent callIntent = new Intent(Intent.ACTION_CALL);
				callIntent.setData(Uri.parse("tel:" + phnum1));
				startActivity(callIntent);

			}
		});

	}
}

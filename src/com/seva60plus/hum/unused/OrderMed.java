package com.seva60plus.hum.unused;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.seva60plus.hum.R;
import com.seva60plus.hum.R.anim;
import com.seva60plus.hum.R.id;
import com.seva60plus.hum.R.layout;
import com.seva60plus.hum.activity.MenuLay;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class OrderMed extends Activity {
	public static final String ACTION_SMS_SENT = "com.wrc.android.apis.os.SMS_SENT_ACTION";

	public static final String MY_PREFS_NAME = "MyPrefsFile";
	Button saveBtn;
	TextView headerTitleText;
	ImageView backBtn, homeBtn;
	EditText cust, phone, medName, quantity;
	String custName, phNumber, medicineName, quntNumber;

	String phermaNoPref, userNamePref, UserPhoneNumberPref;

	String con1 = "";
	String con2 = "";
	String Name, Phone, phermaNo;
	String userName, userPhone, phermaPhoneNo; //---for store value
	TelephonyManager tm;
	String subId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_med);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		subId = tm.getSubscriberId().toString();
		System.out.println(subId);

		SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
		phermaNoPref = prefs.getString("phermaNum", null);
		userNamePref = prefs.getString("UserName", null);
		UserPhoneNumberPref = prefs.getString("UserPhoneNumber", null);

		// Toast.makeText(getApplicationContext(), "Pref: "+phermaNoPref, Toast.LENGTH_LONG).show();
		if (phermaNoPref.equals("") || userNamePref.equals("") || UserPhoneNumberPref.equals("")) {

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

				Name = json1.getString("name");
				Phone = json1.getString("phone");

				phermaNo = json1.getString("pharmacy_no");

				//   Toast.makeText(getApplicationContext(), "PhremaNo: "+phermaNo, Toast.LENGTH_LONG).show();

			} catch (JSONException e) {
				e.printStackTrace();
			}

			userName = Name;
			userPhone = Phone;
			phermaPhoneNo = phermaNo;

		} else {
			userName = userNamePref;
			userPhone = UserPhoneNumberPref;
			phermaPhoneNo = phermaNoPref;
		}

		headerTitleText = (TextView) findViewById(R.id.header_title);
		headerTitleText.setText("ORDER MEDICINE");
		Typeface font = Typeface.createFromAsset(getAssets(), "openSansBold.ttf");
		headerTitleText.setTypeface(font);

		cust = (EditText) findViewById(R.id.custEdit);
		cust.setText(userName, TextView.BufferType.EDITABLE);

		phone = (EditText) findViewById(R.id.phoneCustEdit);
		phone.setText(userPhone, TextView.BufferType.EDITABLE);

		saveBtn = (Button) findViewById(R.id.save);
		saveBtn.setTypeface(font);
		saveBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// Take action.
				//Intent myIntent = new Intent(MainActivity.this, Registration.class);
				//startActivity(myIntent);

				//Toast.makeText(getApplicationContext(), "Registration Sucessfully completed", Toast.LENGTH_SHORT).show();

				custName = cust.getText().toString();
				phone = (EditText) findViewById(R.id.phoneCustEdit);
				phNumber = phone.getText().toString();
				medName = (EditText) findViewById(R.id.medEdit);
				medicineName = medName.getText().toString();
				quantity = (EditText) findViewById(R.id.quntEdit);
				quntNumber = quantity.getText().toString();

				sendLongSMS();

			}
		});

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
				Intent myIntent = new Intent(OrderMed.this, MenuLay.class);
				startActivity(myIntent);
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

			}
		});

	}

	public void sendLongSMS() {

		//String shop = getString(R.string.shop_number);
		/*
		 * String phoneNumber = "+91"+oderNumber; String message =
		 * "Customer Name:"
		 * +Name+"\nPhone Number:"+Phone+"\nMedicine Name:"+medicineName
		 * +"\nQuantity:"+quntNumber;
		 * 
		 * SmsManager smsManager = SmsManager.getDefault(); ArrayList<String>
		 * parts = smsManager.divideMessage(message);
		 * smsManager.sendMultipartTextMessage(phoneNumber, null, parts, null,
		 * null); //Toast.makeText(getApplicationContext(),
		 * "Customer Name:"+custName
		 * +"\nPhone Number:"+phNumber+"\nMedicine Name:"
		 * +medicineName+"\nQuantity:"+quntNumber, Toast.LENGTH_SHORT).show();
		 * Toast.makeText(getApplicationContext(),
		 * "Customer Name:"+Name+"\nPhone Number:"
		 * +Phone+"\nMedicine Name:"+medicineName+"\nQuantity:"+quntNumber,
		 * Toast.LENGTH_SHORT).show();
		 */
		String message1 = "Customer Name:" + custName + "\nPhone Number:" + phNumber + "\nMedicine Name:" + medicineName + "\nQuantity:" + quntNumber;
		String strSMSBody = message1;
		//sms recipient added by user from the activity screen
		String strReceipentsList = phermaPhoneNo;
		SmsManager sms = SmsManager.getDefault();
		List<String> messages = sms.divideMessage(strSMSBody);
		for (String message : messages) {
			sms.sendTextMessage(strReceipentsList, null, message, PendingIntent.getBroadcast(this, 0, new Intent(ACTION_SMS_SENT), 0), null);
		}

	}

}
package com.seva60plus.hum.sathi;

import java.util.ArrayList;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.seva60plus.hum.R;
import com.seva60plus.hum.Listeners.AddSaathiListener;
import com.seva60plus.hum.activity.BaseActivity;
import com.seva60plus.hum.activity.CountryCodeActivity;
import com.seva60plus.hum.activity.DashboardActivity;
import com.seva60plus.hum.asynctask.AddSaathi;
import com.seva60plus.hum.custom.NoInternetPage;
import com.seva60plus.hum.db.DBConstants;
import com.seva60plus.hum.db.SaathiDB;
import com.seva60plus.hum.util.Util;

public class AddSaathiAcivity extends BaseActivity implements AddSaathiListener, DBConstants {

	private Context mContext;
	private TextView tvSaathiCountryCode;
	private EditText etSaathiPhone, etSaathiName, etSaathiEmail;
	private static final int SAATHI_COUNTRY_CODE = 231231;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_saathi);
		mContext = AddSaathiAcivity.this;
		tvSaathiCountryCode = (TextView) findViewById(R.id.tvSaathiCountryCode);
		etSaathiName = (EditText) findViewById(R.id.etSaathiName);
		etSaathiPhone = (EditText) findViewById(R.id.etSaathiPhone);
		etSaathiEmail = (EditText) findViewById(R.id.etSaathiEmail);

		tvSaathiCountryCode.setText("+91");
		tvSaathiCountryCode.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				startActivityForResult(new Intent(mContext, CountryCodeActivity.class), SAATHI_COUNTRY_CODE);

			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == SAATHI_COUNTRY_CODE && resultCode == Activity.RESULT_OK) {

			String countryCode = data.getStringExtra(CountryCodeActivity.RESULT_CONTRYCODE);

			tvSaathiCountryCode.setText("+" + countryCode);

		}
	}

	public void onSaveButtonClick(View v) {
		if (Util.isInternetAvailable(mContext)) {

			if (etSaathiPhone.getText().toString().trim().isEmpty())
				Toast.makeText(mContext, "Please Enter Saathi\'s Phone Number.", Toast.LENGTH_SHORT).show();
			else if (etSaathiName.getText().toString().trim().isEmpty())
				Toast.makeText(mContext, "Please Enter your Saathi\'s Name.", Toast.LENGTH_SHORT).show();
			else if (!etSaathiEmail.getText().toString().trim().isEmpty() && !Util.isEmailValid(etSaathiEmail.getText().toString().trim()))
				Toast.makeText(mContext, "Email id is not valid.", Toast.LENGTH_SHORT).show();
			else if (!etSaathiName.getText().toString().trim().isEmpty() && !etSaathiPhone.getText().toString().trim().isEmpty())
				addSaathi();

		} else if (!Util.isInternetAvailable(mContext)) {
			Intent i = new Intent(mContext, NoInternetPage.class);
			startActivity(i);
			overridePendingTransition(0, 0);
		}

	}

	public void onCancelButtonClick(View v) {
		finish();
	}

	/**
	 * Add Saathi Method.
	 **/
	private void addSaathi() {

		Saathi mSaathi = new Saathi();
		mSaathi.setName(etSaathiName.getText().toString().trim());
		mSaathi.setEmailId(etSaathiEmail.getText().toString().trim());
		mSaathi.setPhoneNumber(etSaathiPhone.getText().toString().trim());
		mSaathi.setCountryCode(tvSaathiCountryCode.getText().toString().trim());
		AddSaathi addSaathi = new AddSaathi(mContext, mSaathi);
		addSaathi.mListener = this;
		addSaathi.execute();

	}

	@Override
	public void onSaathiAdd(String callBack) {

		if (callBack.equalsIgnoreCase(AddSaathi.RESPONSE_SUCCESS)) {

			sendSMStoSaathi(tvSaathiCountryCode.getText().toString().trim() + etSaathiPhone.getText().toString().trim());

			saveSaathi();

			Toast.makeText(mContext, "Saathi Added Successfully!!", Toast.LENGTH_LONG).show();

			startActivity(new Intent(mContext, SaathiActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

			finish();

		} else {

			Toast.makeText(mContext, "Oops! Something went wrong.", Toast.LENGTH_LONG).show();
		}
	}

	private void sendSMStoSaathi(String saathiPhone) {

		System.out.println("Send Sms called >> --------------------");

		String message = etSaathiName.getText().toString().trim()
				+ " has added you as a Saathi! Please install the Seva60Plus Saathi app from Google Play Store to monitor the well-being of "
				+ etSaathiName.getText().toString().trim() + " \nhttps://play.google.com/store/apps/details?id=com.seva60plus.saathi&hl=en";
		System.out.println("Message:: " + message);

		sendSMS(saathiPhone, message);

		System.out.println("SMS Sent >> -----------------------");
		startActivity(new Intent(mContext, DashboardActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
		finish();
	}

	private void sendSMS(String phone, String message) {

		String SENT = "SMS_SENT";
		PendingIntent sentPI = PendingIntent.getBroadcast(AddSaathiAcivity.this, 0, new Intent(SENT), 0);

		registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {

				switch (getResultCode()) {

				case Activity.RESULT_OK:

					Toast.makeText(AddSaathiAcivity.this, "SMS sent", Toast.LENGTH_SHORT).show();
					finish();
					break;

				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
					Toast.makeText(AddSaathiAcivity.this, "Generic failure", Toast.LENGTH_SHORT).show();
					break;

				case SmsManager.RESULT_ERROR_NO_SERVICE:
					Toast.makeText(AddSaathiAcivity.this, "No service", Toast.LENGTH_SHORT).show();
					break;

				case SmsManager.RESULT_ERROR_NULL_PDU:
					Toast.makeText(AddSaathiAcivity.this, "Null PDU", Toast.LENGTH_SHORT).show();
					break;

				case SmsManager.RESULT_ERROR_RADIO_OFF:
					Toast.makeText(getBaseContext(), "Radio off", Toast.LENGTH_SHORT).show();
					break;

				}
			}
		}, new IntentFilter(SENT));

		ArrayList<PendingIntent> arrPI = new ArrayList<PendingIntent>();
		arrPI.add(sentPI);

		SmsManager sms = SmsManager.getDefault();
		ArrayList<String> parts = sms.divideMessage(message);
		sms.sendMultipartTextMessage(phone, null, parts, arrPI, null);
	}

	private void saveSaathi() {
		// --- SAVE FORM SAATHI IN DATABASE
		ContentValues mValues = new ContentValues();
		mValues.put(SAATHI_NAME, etSaathiName.getText().toString().trim());
		mValues.put(DBConstants.SAATHI_COUNTRY_CODE, tvSaathiCountryCode.getText().toString().trim());
		mValues.put(SAATHI_PHONE_NUMBER, etSaathiPhone.getText().toString().trim());
		mValues.put(SAATHI_EMAIL, etSaathiEmail.getText().toString().trim());
		new SaathiDB().saveSaathi(mContext, mValues);

	}
}

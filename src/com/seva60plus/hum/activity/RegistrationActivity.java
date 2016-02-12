package com.seva60plus.hum.activity;

import java.util.ArrayList;
import java.util.regex.Pattern;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.seva60plus.hum.R;
import com.seva60plus.hum.Listeners.RegisterUserListener;
import com.seva60plus.hum.asynctask.RegisterUser;
import com.seva60plus.hum.custom.NoInternetPage;
import com.seva60plus.hum.db.DBConstants;
import com.seva60plus.hum.db.SaathiDB;
import com.seva60plus.hum.model.Hum;
import com.seva60plus.hum.model.HumRegister;
import com.seva60plus.hum.util.PreferenceUtil;
import com.seva60plus.hum.util.Util;

public class RegistrationActivity extends BaseActivity implements OnClickListener, OnCheckedChangeListener, RegisterUserListener, OnDateSetListener {

	private EditText etHumName, etHumPhoneNumber, etSaathiPhone, etSaathiName, etSaathiEmail;
	private TextView tvHumCountryCode, tvSaathiCountryCode, tvDateOfBirth;
	private LinearLayout llDateOfBirth;
	private RelativeLayout rlGeoLocation;
	private RelativeLayout rlHome;
	private RadioGroup rgGender;
	private CheckBox cbGeoLocation;
	private Button btCancel, btSave;
	private String imei, subId;
	private Context mContext;
	private String userEmail;
	private String gender = "M";
	private String isGeo = "1";
	private int HUM_COUNTRY_CODE = 765649391;
	private int SAATHI_COUNTRY_CODE = 8645311;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);
		mContext = RegistrationActivity.this;
		initView();
	}

	private void initView() {

		etHumName = (EditText) findViewById(R.id.etHumName);
		etHumPhoneNumber = (EditText) findViewById(R.id.etHumPhoneNumber);
		tvDateOfBirth = (TextView) findViewById(R.id.tvDateOfBirth);
		etSaathiPhone = (EditText) findViewById(R.id.etSaathiPhone);
		etSaathiName = (EditText) findViewById(R.id.etSaathiName);
		etSaathiEmail = (EditText) findViewById(R.id.etSaathiEmail);
		tvHumCountryCode = (TextView) findViewById(R.id.tvHumCountryCode);
		tvSaathiCountryCode = (TextView) findViewById(R.id.tvSaathiCountryCode);
		llDateOfBirth = (LinearLayout) findViewById(R.id.llDateOfBirth);
		rlGeoLocation = (RelativeLayout) findViewById(R.id.rlGeoLocation);
		rlHome = (RelativeLayout) findViewById(R.id.rlHome);
		rgGender = (RadioGroup) findViewById(R.id.rgGender);
		cbGeoLocation = (CheckBox) findViewById(R.id.cbGeoLocation);
		btCancel = (Button) findViewById(R.id.btCancel);
		btSave = (Button) findViewById(R.id.btSave);
		imei = getImeiNumber();
		subId = getSubId();
		userEmail = getUserEmail();

		tvHumCountryCode.setText("+91");
		tvSaathiCountryCode.setText("+91");
		tvHumCountryCode.setOnClickListener(this);
		tvSaathiCountryCode.setOnClickListener(this);
		btCancel.setOnClickListener(this);
		btSave.setOnClickListener(this);
		llDateOfBirth.setOnClickListener(this);
		rlGeoLocation.setOnClickListener(this);
		rgGender.setOnCheckedChangeListener(this);
		rlHome.setVisibility(View.INVISIBLE);

	}

	private String getUserEmail() {
		String email = "";
		Pattern emailPattern = Patterns.EMAIL_ADDRESS;
		Account[] accounts = AccountManager.get(getBaseContext()).getAccounts();
		for (Account account : accounts) {
			if (emailPattern.matcher(account.name).matches()) {
				email = account.name;
				break;

			}
		}
		return email;
	}

	private String getSubId() {
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getSubscriberId().toString();
	}

	private String getImeiNumber() {
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getDeviceId();
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.btCancel:

			break;

		case R.id.btSave:

			onSaveButtonClick();
			break;

		case R.id.rlGeoLocation:

			if (cbGeoLocation.isChecked())
				cbGeoLocation.setChecked(false);

			else if (!cbGeoLocation.isChecked())
				cbGeoLocation.setChecked(true);
			break;

		case R.id.llDateOfBirth:

			new DatePickerDialog(mContext, this, 1950, 0, 1).show();

			break;

		case R.id.tvHumCountryCode:

			startActivityForResult(new Intent(mContext, CountryCodeActivity.class), HUM_COUNTRY_CODE);

			break;

		case R.id.tvSaathiCountryCode:

			startActivityForResult(new Intent(mContext, CountryCodeActivity.class), SAATHI_COUNTRY_CODE);
			break;

		default:
			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == HUM_COUNTRY_CODE && resultCode == Activity.RESULT_OK) {
			String countryCode = data.getStringExtra(CountryCodeActivity.RESULT_CONTRYCODE);
			Toast.makeText(this, "You selected countrycode: " + countryCode, Toast.LENGTH_LONG).show();
			tvHumCountryCode.setText("+" + countryCode);

		} else if (requestCode == SAATHI_COUNTRY_CODE && resultCode == Activity.RESULT_OK) {
			String countryCode = data.getStringExtra(CountryCodeActivity.RESULT_CONTRYCODE);
			Toast.makeText(this, "You selected countrycode: " + countryCode, Toast.LENGTH_LONG).show();
			tvSaathiCountryCode.setText("+" + countryCode);

		}
	}

	private void onSaveButtonClick() {

		if (Util.isInternetAvailable(mContext)) {

			if (etHumName.getText().toString().trim().isEmpty())
				Toast.makeText(mContext, "Please Enter your Name.", Toast.LENGTH_SHORT).show();

			else if (etHumPhoneNumber.getText().toString().trim().isEmpty())
				Toast.makeText(mContext, "Please Enter your Phone Number.", Toast.LENGTH_SHORT).show();

			else if (tvDateOfBirth.getText().toString().trim().isEmpty())
				Toast.makeText(mContext, "Please Enter your Birth Date", Toast.LENGTH_SHORT).show();

			else if (etSaathiName.getText().toString().trim().isEmpty())
				Toast.makeText(mContext, "Please Enter your Saathi\'s Name.", Toast.LENGTH_SHORT).show();

			else if (etSaathiPhone.getText().toString().trim().isEmpty())
				Toast.makeText(mContext, "Please Enter Saathi\'s Phone Number.", Toast.LENGTH_SHORT).show();

			else if (etHumPhoneNumber.getText().toString().trim().length() != 10)
				Toast.makeText(mContext, "Hum phone number should be atleast 10 digits.", Toast.LENGTH_LONG).show();

			else if (!etSaathiEmail.getText().toString().trim().isEmpty() && !Util.isEmailValid(etSaathiEmail.getText().toString().trim()))
				Toast.makeText(mContext, "Email id is not valid.", Toast.LENGTH_SHORT).show();

			else if (!etHumName.getText().toString().trim().isEmpty() || !etHumPhoneNumber.getText().toString().trim().isEmpty()
					|| !tvDateOfBirth.getText().toString().trim().isEmpty() || !etSaathiName.getText().toString().trim().isEmpty()
					|| !etSaathiPhone.getText().toString().trim().isEmpty())
				registerHum();

		} else if (!Util.isInternetAvailable(mContext)) {
			Intent i = new Intent(mContext, NoInternetPage.class);
			startActivity(i);
			overridePendingTransition(0, 0);
		}

	}

	private void registerHum() {

		if (cbGeoLocation.isChecked())
			isGeo = "1";

		else
			isGeo = "0";

		// Hum Register Obj.
		HumRegister humRegisterObj = new HumRegister();
		humRegisterObj.setSubmit("submit");
		humRegisterObj.setSub_id(subId);
		humRegisterObj.setImei(imei);
		humRegisterObj.setEmail(userEmail);

		humRegisterObj.setName(etHumName.getText().toString().trim());
		humRegisterObj.setCountry_code(tvHumCountryCode.getText().toString().trim());
		humRegisterObj.setPhone(etHumPhoneNumber.getText().toString().trim());
		humRegisterObj.setDob(tvDateOfBirth.getText().toString().trim());

		humRegisterObj.setGender(gender);
		humRegisterObj.setAddress1("");
		humRegisterObj.setCity("");
		humRegisterObj.setState("");
		humRegisterObj.setPincode("");
		humRegisterObj.setSathi_name1(etSaathiName.getText().toString().trim());

		Log.v("Registration Activity", "User Email: " + userEmail);
		humRegisterObj.setSathi_email1(etSaathiEmail.getText().toString().trim());
		humRegisterObj.setSathi_name2("");
		humRegisterObj.setSathi_email2("");

		humRegisterObj.setSathi1_country_code(tvSaathiCountryCode.getText().toString().trim());
		humRegisterObj.setSathi_contact_no1(etSaathiPhone.getText().toString().trim());

		humRegisterObj.setSathi2_country_code("");
		humRegisterObj.setSathi_contact_no2("");
		humRegisterObj.setPharmacy_no("");

		humRegisterObj.setIs_geo_enabled(isGeo);
		humRegisterObj.setStatus("1");

		RegisterUser registerUser = new RegisterUser(mContext, humRegisterObj);
		registerUser.mListener = this;
		registerUser.execute();
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {

		if (checkedId == R.id.rbMale)
			gender = "M";

		else if (checkedId == R.id.rbFemale)
			gender = "F";

	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

		tvDateOfBirth.setText(new StringBuilder().append(dayOfMonth).append("/").append(monthOfYear + 1).append("/").append(year).append(" "));
	}

	@Override
	public void onRegisterUser(String callback, String saathiPhone) {

		System.out.println("On regiser callback:: \n" + callback + "\n Saathi Phone:: " + saathiPhone + "\n");

		if (callback.equalsIgnoreCase(RegisterUser.RESPONSE_SUCCESS)) {

			Toast.makeText(getApplicationContext(), "Registered successfully!!", Toast.LENGTH_LONG).show();

			saveHumCredentials(saathiPhone);

			saveSaathi();

			//sendSMStoSaathi(saathiPhone);

		} else if (callback.equalsIgnoreCase(RegisterUser.RESPONSE_FAILED))

			Toast.makeText(mContext, "Oops! Something went wrong.", Toast.LENGTH_LONG).show();

	}

	private void saveSaathi() {

		// Flush out the Saathi Database Table.
		new SaathiDB().deleteSaathiTable(mContext);

		// --- SAVE SAATHI IN DATABASE
		ContentValues mValues = new ContentValues();
		mValues.put(DBConstants.SAATHI_NAME, etSaathiName.getText().toString().trim());
		mValues.put(DBConstants.SAATHI_COUNTRY_CODE, tvSaathiCountryCode.getText().toString().trim());
		mValues.put(DBConstants.SAATHI_PHONE_NUMBER, etSaathiPhone.getText().toString().trim());
		mValues.put(DBConstants.SAATHI_EMAIL, etSaathiEmail.getText().toString().trim());
		new SaathiDB().saveSaathi(mContext, mValues);

	}

	private void saveHumCredentials(String saathiPhone) {

		Hum hum = new Hum();
		hum.setCountryCode(tvHumCountryCode.getText().toString().trim());
		hum.setDateOfBirth(tvDateOfBirth.getText().toString().trim());
		hum.setEmail(userEmail);
		hum.setGender(gender);
		// hum.setId();
		hum.setImeiNumber(imei);
		hum.setIsGeoEnabled(isGeo);
		hum.setName(etHumName.getText().toString().trim());
		hum.setPhone(etHumPhoneNumber.getText().toString().trim());
		hum.setSubscriberId(subId);
		PreferenceUtil.saveUserClass((Activity) mContext, hum);
	}

	private void sendSMStoSaathi(String saathiPhone) {

		System.out.println("Send Sms called >> --------------------");

		String message = etHumName.getText().toString().trim()
				+ " has added you as a Saathi! Please install the Seva60Plus Saathi app from Google Play Store to monitor the well-being of "
				+ etHumName.getText().toString().trim() + " \nhttps://play.google.com/store/apps/details?id=com.seva60plus.saathi&hl=en";
		System.out.println("Message:: " + message);

		sendSMS(saathiPhone, message);

		System.out.println("SMS Sent >> -----------------------");
		startActivity(new Intent(mContext, DashboardActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
		finish();
	}

	private void sendSMS(String phone, String message) {

		String SENT = "SMS_SENT";
		PendingIntent sentPI = PendingIntent.getBroadcast(RegistrationActivity.this, 0, new Intent(SENT), 0);

		registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {

				switch (getResultCode()) {
				case Activity.RESULT_OK:

					Toast.makeText(RegistrationActivity.this, "SMS sent", Toast.LENGTH_SHORT).show();
					finish();
					break;
				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:

					Toast.makeText(RegistrationActivity.this, "Generic failure", Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_NO_SERVICE:

					Toast.makeText(RegistrationActivity.this, "No service", Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_NULL_PDU:

					Toast.makeText(RegistrationActivity.this, "Null PDU", Toast.LENGTH_SHORT).show();
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

	public void onHomeClick(View v) {

	}
}

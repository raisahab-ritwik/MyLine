package com.seva60plus.hum.activity;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.seva60plus.hum.R;
import com.seva60plus.hum.Listeners.HumDetailsAsyncListener;
import com.seva60plus.hum.Listeners.UpdateHumDetailsAsyncListener;
import com.seva60plus.hum.asynctask.HumDetailsAsync;
import com.seva60plus.hum.asynctask.UpdateHumDetails;
import com.seva60plus.hum.model.Hum;
import com.seva60plus.hum.util.PreferenceUtil;
import com.seva60plus.hum.util.Util;

@SuppressWarnings("unused")
public class HumDetailsView extends BaseActivity implements HumDetailsAsyncListener, UpdateHumDetailsAsyncListener {

	private String imei;
	private EditText et_name, et_dateOfBirth;
	private TextView tv_phoneNumber, tv_CountryCode;
	private Context mContext;
	private String subscriberId;
	private RadioButton rb_Male, rb_Female;
	private String gender = "M";
	private RadioGroup rgGender;
	private Hum mHum;
	private static final int DATE_PICKER_ID = 1111;
	private int year;
	private int month;
	private int day;
	private String output;
	private RelativeLayout rl_isGeo;
	private CheckBox cb_isGeo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_details);
		mContext = HumDetailsView.this;

		initView();
	}

	private void initView() {

		et_name = (EditText) findViewById(R.id.et_name);
		et_dateOfBirth = (EditText) findViewById(R.id.et_dateOfBirth);
		tv_phoneNumber = (TextView) findViewById(R.id.tv_phoneNumber);
		tv_CountryCode = (TextView) findViewById(R.id.tv_CountryCode);
		rgGender = (RadioGroup) findViewById(R.id.rg_gender);
		rb_Male = (RadioButton) findViewById(R.id.rb_Male);
		rb_Female = (RadioButton) findViewById(R.id.rb_female);
		rl_isGeo = (RelativeLayout) findViewById(R.id.rl_isGeo);
		cb_isGeo = (CheckBox) findViewById(R.id.cb_isGeo);
		rgGender.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_Male:
					gender = "M";
					break;

				case R.id.rb_female:
					gender = "F";
					break;

				}

			}
		});
		findViewById(R.id.llMenu).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent myIntent = new Intent(mContext, MenuLay.class);
				startActivity(myIntent);
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

			}
		});
		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
		et_dateOfBirth.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				showDialog(DATE_PICKER_ID);

			}
		});
		rl_isGeo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (cb_isGeo.isChecked())
					cb_isGeo.setChecked(false);
				else if (!cb_isGeo.isChecked())
					cb_isGeo.setChecked(true);
			}
		});

		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		telephonyManager.getDeviceId();

		imei = telephonyManager.getDeviceId();
		subscriberId = telephonyManager.getSubscriberId().toString().trim();

		HumDetailsAsync async = new HumDetailsAsync(mContext, subscriberId);
		async.mListener = this;
		async.execute();

	}

	@Override
	public void getHumDetailsJson(Hum mHum) {

		this.mHum = mHum;
		if (mHum != null) {
			et_name.setText(mHum.getName());
			et_dateOfBirth.setText(mHum.getDateOfBirth());
			tv_CountryCode.setText(mHum.getCountryCode());
			tv_phoneNumber.setText(mHum.getPhone());
			output = mHum.getDateOfBirth();

			if (mHum.getGender() != null && mHum.getGender().trim().equalsIgnoreCase("M")) {
				rb_Male.setSelected(true);
				rb_Female.setSelected(false);
				gender = "M";
			} else {
				rb_Male.setSelected(false);
				rb_Female.setSelected(true);
				gender = "F";
			}
			if (mHum.getIsGeoEnabled() != null && mHum.getIsGeoEnabled().trim().equalsIgnoreCase("1"))
				cb_isGeo.setChecked(true);
			else
				cb_isGeo.setChecked(false);
			PreferenceUtil.saveUserClass(HumDetailsView.this, mHum);
		}
	}

	public void onSaveButtonClick(View v) {
		mHum.setName(et_name.getText().toString().trim());
		mHum.setGender(gender);
		mHum.setDateOfBirth(output);
		if (cb_isGeo.isChecked()) {
			mHum.setIsGeoEnabled("1");
		} else if (!cb_isGeo.isChecked()) {
			mHum.setIsGeoEnabled("0");
		}
		UpdateHumDetails mDetails = new UpdateHumDetails(mContext, mHum);
		mDetails.mListener = this;
		mDetails.execute();

	}

	public void onCancelButtonClick(View v) {

		onBackPressed();
	}

	@Override
	public void getUpdateStatus(String result, String isGeo) {

		Log.d("Update Status", "---------\n " + result);

		if (result.equalsIgnoreCase(UpdateHumDetails.RESPONSE_SUCCESS)) {
			try {
				Toast.makeText(mContext, "Hum Details Updated.", Toast.LENGTH_SHORT).show();
				SharedPreferences.Editor mEditor = Util.getSharedEditor(mContext);
				mEditor.putString("geoLocation", isGeo).commit();
				finish();
				// PreferenceUtil.saveUserClass(HumDetailsView.this, mHum);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			Toast.makeText(mContext, "Oops! Something went wrong.", Toast.LENGTH_SHORT).show();
		}
	}

	private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.
		@Override
		public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {

			year = selectedYear;
			month = selectedMonth;
			day = selectedDay;
			StringBuilder superStringBuilder = new StringBuilder().append(day).append("/").append(month + 1).append("/").append(year).append(" ");
			output = superStringBuilder.toString();
			et_dateOfBirth.setText(output, TextView.BufferType.EDITABLE);

		}
	};

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_PICKER_ID:

			return new DatePickerDialog(this, pickerListener, year, month, day);
		}
		return null;
	}

	public void onHomeClick(View v) {
		startActivity(new Intent(mContext, DashboardActivity.class));
		finish();
	}
}

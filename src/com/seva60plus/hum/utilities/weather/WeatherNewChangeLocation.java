package com.seva60plus.hum.utilities.weather;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.seva60plus.hum.R;
import com.seva60plus.hum.activity.DashboardActivity;
import com.seva60plus.hum.activity.HumDetailsView;
import com.seva60plus.hum.activity.MenuLay;
import com.seva60plus.hum.custom.NoInternetPage;
import com.seva60plus.hum.util.Util;

public class WeatherNewChangeLocation extends Activity {

	public static final String MY_PREFS_NAME = "MyPrefsFile";

	EditText editText;

	ImageView backBtn, menuIcon;
	RelativeLayout backSetup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weather_new_activity_location);

		backBtn = (ImageView) findViewById(R.id.iv_back);

		backBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				finish();

			}
		});
		backSetup = (RelativeLayout) findViewById(R.id.back_settings);

		backSetup.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (Util.isInternetAvailable(WeatherNewChangeLocation.this)) {
					Intent i = new Intent(WeatherNewChangeLocation.this, HumDetailsView.class);
					startActivity(i);
					overridePendingTransition(R.anim.slide_in_right, R.anim.slide_leave_right);
				} else {
					Intent i = new Intent(WeatherNewChangeLocation.this, NoInternetPage.class);
					startActivity(i);
					overridePendingTransition(0, 0);
				}
			}
		});

		findViewById(R.id.llMenu).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intObj = new Intent(getApplicationContext(), MenuLay.class);
				startActivity(intObj);
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

			}
		});
		editText = (EditText) findViewById(R.id.autoCompleteCity);
		editText.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					onSearchButtonClick();
					return true;
				} else
					return false;
			}
		});
		Button searchBtn = (Button) findViewById(R.id.btnSearchByCity);

		searchBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				onSearchButtonClick();
			}
		});

	}

	private void onSearchButtonClick() {

		String city = editText.getText().toString();

		SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
		editor.putString("shareWCity", city.replace(" ", ""));
		editor.commit();

		Intent intObj = new Intent(WeatherNewChangeLocation.this, WeatherNewMain.class);
		startActivity(intObj);
		finish();
	}
	public void onHomeClick(View v) {
		startActivity(new Intent(WeatherNewChangeLocation.this, DashboardActivity.class));
		finish();
	}
}

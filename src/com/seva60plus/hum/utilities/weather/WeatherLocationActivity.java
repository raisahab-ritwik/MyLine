package com.seva60plus.hum.utilities.weather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.seva60plus.hum.R;
import com.seva60plus.hum.activity.DashboardActivity;
import com.seva60plus.hum.activity.HumDetailsView;
import com.seva60plus.hum.activity.MenuLay;
import com.seva60plus.hum.custom.NoInternetPage;
import com.seva60plus.hum.util.Util;

public class WeatherLocationActivity extends Activity implements OnClickListener {
	String greg = "locationActivity";

	Activity locationActivity = this;
	private AutoCompleteTextView tvEnterSity;
	private EditText lat;
	private EditText lon;
	Intent intent = new Intent();

	ImageView backBtn, menuIcon;
	RelativeLayout backSetup;

	public void onCreate(Bundle savedInstanceState) {
		WeatherMyLog.d(greg, "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weather_activity_location);

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
				if (Util.isInternetAvailable(WeatherLocationActivity.this)) {
					Intent i = new Intent(WeatherLocationActivity.this, HumDetailsView.class);
					startActivity(i);
					overridePendingTransition(R.anim.slide_in_right, R.anim.slide_leave_right);
				} else {
					Intent i = new Intent(WeatherLocationActivity.this, NoInternetPage.class);
					startActivity(i);
					overridePendingTransition(0, 0);
				}
			}
		});

		findViewById(R.id.llMenu).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intObj = new Intent(getApplicationContext(), MenuLay.class);
				startActivity(intObj);
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			}
		});

		tvEnterSity = (AutoCompleteTextView) findViewById(R.id.autoCompleteCity);

		tvEnterSity.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					onSearchButtonClick();
					return true;
				} else
					return false;
			}
		});
		String[] cities = getResources().getStringArray(R.array.city);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, cities);
		tvEnterSity.setAdapter(adapter);

		Button btnSearchByCity = (Button) findViewById(R.id.btnSearchByCity);
		btnSearchByCity.setOnClickListener(this);

		lat = (EditText) findViewById(R.id.editLat);
		lon = (EditText) findViewById(R.id.editLon);

		InputFilter filter = new InputFilter() {
			@Override
			public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
				Toast toast = Toast.makeText(locationActivity, "����������� ���������� ���������",
						Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.TOP, 0, 0);
				for (int i = start; i < end; i++) {
					if (!isEnglish(source.charAt(i))) {
						toast.show();
						return "";
					}
				}
				return null;
			}

			private boolean isEnglish(char charAt) {
				String validationString = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM, ";
				if (validationString.indexOf(charAt) == -1)
					return false;
				else
					return true;
			}
		};
		tvEnterSity.setFilters(new InputFilter[] { filter });

		Button btnSearchByCrd = (Button) findViewById(R.id.btnSearchByCrd);
		btnSearchByCrd.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		//Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.btnSearchByCity:
			onSearchButtonClick();
			break;
		case R.id.btnSearchByCrd:
			String lat = this.lat.getText().toString();
			String lon = this.lon.getText().toString();
			WeatherMyLog.d(greg, "read lat " + lat + "lon " + lon);
			intent.putExtra("lat", lat);
			intent.putExtra("lon", lon);
			setResult(2, intent);
			break;
		}
		finish();
	}

	private void onSearchButtonClick() {
		String str2 = tvEnterSity.getText().toString();
		String str = str2.replace(" ", "");

		WeatherMyLog.d(greg, "read city " + str);
		intent.putExtra("city", str);
		intent.putExtra("city2", str2);
		setResult(1, intent);
	}
	public void onHomeClick(View v) {
		startActivity(new Intent(WeatherLocationActivity.this, DashboardActivity.class));
		finish();
	}
}

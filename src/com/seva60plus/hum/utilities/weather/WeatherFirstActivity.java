package com.seva60plus.hum.utilities.weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.seva60plus.hum.R;
import com.seva60plus.hum.activity.DashboardActivity;
import com.seva60plus.hum.activity.HumDetailsView;
import com.seva60plus.hum.activity.MenuLay;
import com.seva60plus.hum.custom.NoInternetPage;
import com.seva60plus.hum.sharelocation.MapActivity;
import com.seva60plus.hum.util.ConnectionDetector;
import com.seva60plus.hum.util.Util;

public class WeatherFirstActivity extends Activity {

	private static final String greg = "firstActivity";
	public static WeatherData newData;

	WeatherMyTabListener nowListener = new WeatherMyTabListener(this, newData, "now", WeatherFragmentNow.class);
	WeatherMyTabListener forecastListener = new WeatherMyTabListener(this, newData, "forecast", WeatherFragmentForecast.class);

	JSONObject weatherJson;
	Menu menu;
	//public ActionBar bar;
	public Tab now;
	Tab forecast;
	public ImageView imv;
	boolean refreshAnim = false;
	MenuItem refreshItem;
	WeatherUrlTask urlTask = null;
	public boolean visibleOnScreen = false;
	public boolean showNewData = false;
	public SharedPreferences mSettings;

	ImageView backBtn, menuIcon;
	RelativeLayout backSetup;
	RelativeLayout changeCityBtn;
	TextView text_city;
	//---by Dibyendu
	// flag for Internet connection status
	Boolean isInternetPresent = false;

	// Connection detector class
	ConnectionDetector cd;

	FragmentTransaction ft;
	@SuppressWarnings("rawtypes")
	private final Class mClass = WeatherFragmentNow.class;
	Fragment mFragment;

	//------------NOW Fragment
	RelativeLayout LL, LL2;
	Button forcastBtn, nowBtn;

	ScrollView LLScroll;
	TextView city;
	ImageView imWeatherIcon;
	TextView temperature;
	TextView descript;
	TextView windSpeed;
	TextView humidity;
	TextView pressure;
	ProgressBar ProgressBar;
	WeatherDayWeather dw;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weather_first_activity);

		LLScroll = (ScrollView) findViewById(R.id.scroll);

		cd = new ConnectionDetector(getApplicationContext());

		text_city = (TextView) findViewById(R.id.tv_fin_advisory);

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

				if (Util.isInternetAvailable(WeatherFirstActivity.this)) {
					Intent i = new Intent(WeatherFirstActivity.this, HumDetailsView.class);
					startActivity(i);
					overridePendingTransition(R.anim.slide_in_right, R.anim.slide_leave_right);
				} else {
					Intent i = new Intent(WeatherFirstActivity.this, NoInternetPage.class);
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

				//finish();
			}
		});
		changeCityBtn = (RelativeLayout) findViewById(R.id.change_city_lay);
		changeCityBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				startChangeActivity();

			}
		});
		LayoutInflater mInflater = LayoutInflater.from(this);

		View mCustomView = mInflater.inflate(R.layout.header, null);

		mSettings = getSharedPreferences("LAST_DATA", Context.MODE_PRIVATE);
		newData = new WeatherData();
		if (mSettings.contains("title")) { // get last save data
			WeatherMyLog.d(greg, "get last save data");
			newData.title = mSettings.getString("title", "");
			newData.title2 = mSettings.getString("title2", "");
			newData.urlStrDay = mSettings.getString("urlStrDay", "");
			newData.urlStrForecast = mSettings.getString("urlStrForecast", null);

			WeatherDayWeather dw = new WeatherDayWeather();
			try {
				WeatherMyLog.d(greg, "try pars from last data");

				dw.parsDay(new JSONObject(mSettings.getString("joDay", null)), this);
				newData.setNowWeather(dw);

				JSONObject jo = new JSONObject(mSettings.getString("joForecast", null));
				JSONArray list = jo.getJSONArray("list");
				int d = list.length();
				WeatherDayWeather[] forecast = new WeatherDayWeather[d];

				for (int i = 0; i <= d - 1; i++) {
					JSONObject day = (JSONObject) list.get(i);
					WeatherMyLog.d(greg, "forecast for (i=" + i + ")");
					forecast[i] = new WeatherDayWeather();
					forecast[i].parsForecast(day, this);
				}
				newData.setForecast(forecast);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			text_city.setText(newData.title2);
			//	bar.setTitle(newData.title);
		} else {

			// ask user chose city or coordinate
			WeatherMyLog.d(greg, "startChangeActivity");
			startChangeActivity();
		}

		LL = (RelativeLayout) findViewById(R.id.FNLinearL);
		LL2 = (RelativeLayout) findViewById(R.id.FNLinearL2);

		forcastBtn = (Button) findViewById(R.id.tab_button2);
		forcastBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				forcastBtn.setTextColor(Color.parseColor("#ffffff"));
				forcastBtn.setBackgroundColor(Color.parseColor("#404041"));
				nowBtn.setTextColor(Color.parseColor("#404041"));
				nowBtn.setBackgroundColor(Color.parseColor("#ffffff"));

				LLScroll.setVisibility(View.GONE);
				//LL.setVisibility(View.GONE);
				LL2.setVisibility(View.VISIBLE);
			}
		});

		nowBtn = (Button) findViewById(R.id.tab_button1);
		nowBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				nowBtn.setTextColor(Color.parseColor("#ffffff"));
				nowBtn.setBackgroundColor(Color.parseColor("#404041"));
				forcastBtn.setTextColor(Color.parseColor("#404041"));
				forcastBtn.setBackgroundColor(Color.parseColor("#ffffff"));
				LL2.setVisibility(View.GONE);
				//LL.setVisibility(View.VISIBLE);
				LLScroll.setVisibility(View.VISIBLE);

			}
		});

		RelativeLayout refresh_btn = (RelativeLayout) findViewById(R.id.refreshLay);
		refresh_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				refresh();
			}
		});

		LL2.setVisibility(View.GONE);

		ProgressBar = (ProgressBar) findViewById(R.id.FNProgressBar);

		if (null != WeatherFirstActivity.newData.getNowWeather()) {

			WeatherMyLog.d(greg, "get data");
			dw = WeatherFirstActivity.newData.getNowWeather();

			WeatherMyLog.d(greg, "FragmentNow post.run setText");

			city = (TextView) findViewById(R.id.city);
			imWeatherIcon = (ImageView) findViewById(R.id.imWeatherIcon);
			temperature = (TextView) findViewById(R.id.temperature);
			descript = (TextView) findViewById(R.id.description);
			windSpeed = (TextView) findViewById(R.id.windSpeed);
			humidity = (TextView) findViewById(R.id.humidity);
			pressure = (TextView) findViewById(R.id.pressure);
			city.setText(dw.city);
			imWeatherIcon.setImageResource(dw.imageId);
			temperature.setText(dw.dayTemperature + "°C");
			descript.setText(dw.description);
			windSpeed.setText(dw.windSpeed);
			humidity.setText(dw.humidity);
			pressure.setText(dw.pressure);
			//LL.setVisibility(View.VISIBLE);
			LLScroll.setVisibility(View.VISIBLE);

			WeatherDayWeather[] dw = WeatherFirstActivity.newData.getForecast();
			WeatherForecastAdapter adapter = new WeatherForecastAdapter(getApplicationContext(), dw);
			ExpandableListView elvDay = (ExpandableListView) findViewById(R.id.ELday);
			elvDay.setAdapter(adapter);

			ProgressBar.setVisibility(View.GONE);

		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		WeatherMyLog.d("gerg", "onResume");
		if (showNewData) {
			afterUrlTask();
			showNewData = false;
		}
		visibleOnScreen = true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		WeatherMyLog.d("gerg", "onPause");
		visibleOnScreen = false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		WeatherMyLog.d("anim", "onCreateOptionsMenu");
		getMenuInflater().inflate(R.menu.main, menu);

		if (null == refreshItem) {
			// first time create menu
			refreshItem = menu.findItem(R.id.refresh);
			return true;
		}

		if (!refreshAnim && null != refreshItem.getActionView()) {
			// stop animation
			WeatherMyLog.d("anim", "set action view null");
			refreshItem.getActionView().clearAnimation();
			return true;
		} else {
			// start animation
			refreshItem = menu.findItem(R.id.refresh);
			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			imv = (ImageView) inflater.inflate(R.layout.weather_imv_refresh, null);
			Animation an = AnimationUtils.loadAnimation(this, R.anim.weather_loadingrotate);
			an.setRepeatCount(Animation.INFINITE);
			imv.startAnimation(an);

			WeatherMyLog.d("anim", "set action view imv");
			refreshItem.setActionView(imv);
			refreshItem.setIcon(null);
			return true;
		}
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.editCity:
			startChangeActivity();
			break;
		case R.id.refresh:
			refresh();
			break;
		}
		return true;
	}

	private void refresh() {
		// enough data to query
		if (null == newData.urlStrForecast || null == newData.urlStrDay || null == newData.title) {

			Toast.makeText(this, "No Data", Toast.LENGTH_LONG).show();

			return;
		}
		stopQuery();
		// start new query with last data
		urlTask = (WeatherUrlTask) new WeatherUrlTask(this, newData, newData.urlStrDay, newData.urlStrForecast, newData.title, newData.title2).execute();
	}

	private void stopQuery() {// stop another query
		if (null != urlTask && (!urlTask.isCancelled() || AsyncTask.Status.FINISHED != urlTask.getStatus()))
			urlTask.cancel(false);
	}

	public void startChangeActivity() {
		WeatherMyLog.d(greg, "startChangeActivity");
		Intent intent = new Intent(this, WeatherLocationActivity.class);
		startActivityForResult(intent, 1);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		WeatherMyLog.d(greg, "onActivityResult");
		switch (resultCode) {
		case 1:
			// start new query with "city"
			String city = intent.getStringExtra("city");
			String city2 = intent.getStringExtra("city2");
			WeatherMyLog.d(greg, "city " + city);
			stopQuery();
			WeatherMyLog.d(greg, "start newURL request code 1");
			urlTask = (WeatherUrlTask) new WeatherUrlTask(this, newData, newData.strWeather + "q=" + city + "&lang=en", newData.strForecast + "q=" + city
					+ "&cnt=7" + "&lang=en", city, city2).execute();

			break;

		case 2:
			// start new query with coordinate
			String lat = intent.getStringExtra("lat");
			String lon = intent.getStringExtra("lon");
			WeatherMyLog.d(greg, "request code 2");
			stopQuery();
			/*
			 * urlTask = (WeatherUrlTask) new WeatherUrlTask(this, newData,
			 * newData.strWeather + "lat=" + lat + "&lon=" + lon+"&lang=en",
			 * newData.strForecast + "lat=" + lat + "&lon=" + lon +
			 * "&cnt=7"+"&lang=en", lat + " " + lon) .execute();
			 */
			break;
		}
	}

	public void loadAnimationStart() {
		// start loading animation
		if (!refreshAnim) {
			WeatherMyLog.d("anim", "animation successful start ");
			refreshAnim = true;
			invalidateOptionsMenu();
		}
	}

	public void loadAnimationStop() {
		// stop loading animation
		if (refreshAnim) {
			WeatherMyLog.d("anim", "animation successful stop");
			refreshAnim = false;
			invalidateOptionsMenu();
		}
	}

	public void afterUrlTask() {
		//if (now != null) {d
		//now.select();d

		text_city.setText(newData.title2);

		LL2.setVisibility(View.GONE);
		if (null != WeatherFirstActivity.newData.getNowWeather()) {

			WeatherMyLog.d(greg, "get data");
			dw = WeatherFirstActivity.newData.getNowWeather();

			WeatherMyLog.d(greg, "FragmentNow post.run setText");

			city.setText(dw.city);
			imWeatherIcon.setImageResource(dw.imageId);
			temperature.setText(dw.dayTemperature + "°C");
			descript.setText(dw.description);
			windSpeed.setText(dw.windSpeed);
			humidity.setText(dw.humidity);
			pressure.setText(dw.pressure);
			//LL.setVisibility(View.VISIBLE);
			LLScroll.setVisibility(View.VISIBLE);
			WeatherDayWeather[] dw = WeatherFirstActivity.newData.getForecast();
			WeatherForecastAdapter adapter = new WeatherForecastAdapter(getApplicationContext(), dw);
			ExpandableListView elvDay = (ExpandableListView) findViewById(R.id.ELday);
			elvDay.setAdapter(adapter);

			ProgressBar.setVisibility(View.GONE);

		} else {
			Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
		}

		//bar.setTitle(newData.title); d
		//}d
	}
	public void onHomeClick(View v) {
		startActivity(new Intent(WeatherFirstActivity.this, DashboardActivity.class));
		finish();
	}
}

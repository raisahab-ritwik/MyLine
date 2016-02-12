package com.seva60plus.hum.utilities.weather;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.seva60plus.hum.R;
import com.seva60plus.hum.activity.DashboardActivity;
import com.seva60plus.hum.activity.HumDetailsView;
import com.seva60plus.hum.activity.MenuLay;
import com.seva60plus.hum.custom.NoInternetPage;
import com.seva60plus.hum.util.Util;

public class WeatherNewForcastList extends Activity implements SwipeRefreshLayout.OnRefreshListener {

	public static final String MY_PREFS_NAME = "MyPrefsFile";
	String wCity = "";
	private SwipeRefreshLayout swipeRefreshLayout;

	String eMsg = "";
	JSONObject json2 = null;
	JSONArray jArray2;

	String jCity, jCountry;

	ArrayList<WeatherNewListData> fDatas = new ArrayList<WeatherNewListData>();
	Dialog progress;
	private AnimationDrawable progressAnimation;
	ListView list;

	ImageView backBtn, menuIcon;
	RelativeLayout backSetup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weather_new_forcast_list);

		//------START------------For Progress Spinner--------------

		progress = new Dialog(WeatherNewForcastList.this);
		progress.getWindow().setBackgroundDrawableResource(R.drawable.spinner_dialog_backround);

		//Remove the Title
		progress.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

		//progress.setTitle("");

		//Set the View of the Dialog - Custom
		progress.setContentView(R.layout.custom_progress_dialog);

		//Set the title of the Dialog
		//dialog.setTitle("Title...");

		ImageView progressSpinner = (ImageView) progress.findViewById(R.id.progressSpinner);

		//Set the background of the image - In this case an animation (/res/anim folder)
		progressSpinner.setBackgroundResource(R.anim.spinner_progress_animation);

		//Get the image background and attach the AnimationDrawable to it.
		progressAnimation = (AnimationDrawable) progressSpinner.getBackground();

		//Start the animation after the dialog is displayed.
		progress.setOnShowListener(new OnShowListener() {
			@Override
			public void onShow(DialogInterface dialog) {
				progressAnimation.start();
			}

		});

		progress.setCanceledOnTouchOutside(false);
		//-------END-----------For Progress Spinner--------------    €‹

		fDatas.clear();

		SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
		wCity = prefs.getString("shareWCity", "");

		System.out.println("LISTCITYSHARED: " + wCity);

		swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
		list = (ListView) findViewById(R.id.list);

		if (wCity.equals("") || wCity == null) {

			Intent intObj = new Intent(WeatherNewForcastList.this, WeatherNewChangeLocation.class);
			startActivity(intObj);
			finish();
		} else {

			new GetWeatherData().execute();
		}

		backBtn = (ImageView) findViewById(R.id.iv_back);

		backBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// Take action.

				finish();

			}
		});
		backSetup = (RelativeLayout) findViewById(R.id.back_settings);

		backSetup.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				if (Util.isInternetAvailable(WeatherNewForcastList.this)) {

					Intent i = new Intent(WeatherNewForcastList.this, HumDetailsView.class);
					startActivity(i);
					overridePendingTransition(R.anim.slide_in_right, R.anim.slide_leave_right);

				} else {
					Intent i = new Intent(WeatherNewForcastList.this, NoInternetPage.class);
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

		RelativeLayout changeCity = (RelativeLayout) findViewById(R.id.change_city_lay);
		changeCity.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intObj = new Intent(getApplicationContext(), WeatherNewChangeLocation.class);
				startActivity(intObj);
				finish();

			}
		});

		Button nowBtn = (Button) findViewById(R.id.now_button);
		nowBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intObj = new Intent(WeatherNewForcastList.this, WeatherNewMain.class);
				startActivity(intObj);
				finish();

			}
		});

		Button forcastBtn = (Button) findViewById(R.id.forcast_button);
		forcastBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		swipeRefreshLayout.setOnRefreshListener(this);

		/**
		 * Showing Swipe Refresh animation on activity create As animation won't
		 * start on onCreate, post runnable is used
		 */
		swipeRefreshLayout.post(new Runnable() {
			@Override
			public void run() {
				swipeRefreshLayout.setRefreshing(true);

				fDatas.clear();
				new GetWeatherData().execute();
			}
		});

	}

	private class GetWeatherData extends AsyncTask<Void, Integer, String> {

		@Override
		protected void onPreExecute() {
			// setting progress bar to zero
			progress.show();
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			return getWeather();
		}

		private String getWeather() {

			fDatas.clear();

			String str2 = "";
			HttpResponse response2;
			HttpClient myClient2 = new DefaultHttpClient();
			HttpPost myConnection2 = new HttpPost("http://api.openweathermap.org/data/2.5/forecast/daily?mode=json&q=" + wCity + "&cnt=8"
					+ "&APPID=a726d4859fa320ffc01b593e3df2acbe");

			try {
				response2 = myClient2.execute(myConnection2);
				str2 = EntityUtils.toString(response2.getEntity(), "UTF-8");

			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("Json Sarted.... ");
			try {
				json2 = new JSONObject(str2);

				String jCode = json2.getString("cod");

				if (!jCode.equals("200")) {

					System.out.println("CODE.... " + jCode);
				} else {

					JSONObject json3 = json2.getJSONObject("city");

					jCity = json3.getString("name");
					jCountry = json3.getString("country");

					//fData.city = jCity;
					//fData.country = jCountry;

					System.out.println("CITY: " + jCity + " : " + jCountry);

					jArray2 = json2.getJSONArray("list");

					System.out.println("List " + jArray2);

					for (int i = 0; i < jArray2.length(); i++) {

						WeatherNewListData fData = new WeatherNewListData();

						JSONObject json4 = jArray2.getJSONObject(i);

						Long jDate = json4.getLong("dt");

						//

						Calendar mydate = Calendar.getInstance();
						mydate.setTimeInMillis(jDate * 1000);

						int mDate = mydate.get(Calendar.DAY_OF_MONTH);
						int mMonth = mydate.get(Calendar.MONTH) + 1;
						int mYear = mydate.get(Calendar.YEAR);

						String gDate = mDate + "/" + mMonth + "/" + mYear;

						SimpleDateFormat newDateFormat = new SimpleDateFormat("dd/MM/yyyy");
						Date MyDate = newDateFormat.parse(gDate);
						newDateFormat.applyPattern("EEEE d MMM yyyy");
						String MyDates = newDateFormat.format(MyDate);

						System.out.println(jDate + " DATE: " + MyDates);

						//
						fData.date = MyDates;//String.valueOf(jDate);

						JSONObject json5 = json4.getJSONObject("temp");
						double jTempDay = json5.getDouble("day");
						double jTempMor = json5.getDouble("morn");
						double jTempEven = json5.getDouble("eve");
						double jTempNight = json5.getDouble("night");
						double jTempMin = json5.getDouble("max");
						double jTempMax = json5.getDouble("min");

						fData.tempDay = String.valueOf(jTempDay);
						fData.tempMorn = String.valueOf(jTempMor);
						fData.tempEve = String.valueOf(jTempEven);
						fData.tempNight = String.valueOf(jTempNight);
						fData.tempMax = String.valueOf(Math.round((jTempMax - 275.15))) + "°C";
						fData.tempMin = String.valueOf(Math.round((jTempMin - 275.15))) + "°C";

						double jPressure = json4.getDouble("pressure");
						double jHumidity = json4.getDouble("humidity");

						double jCloud = json4.getDouble("clouds");
						double jSpeed = json4.getDouble("speed");
						double jRain = 0.0;//json4.getDouble("rain");
						double jDeg = json4.getDouble("deg");

						fData.pressure = String.valueOf(jPressure);
						fData.humidity = String.valueOf(jHumidity);
						fData.clouds = String.valueOf(jCloud);
						fData.speed = String.valueOf(jSpeed);
						fData.rain = String.valueOf(jRain);
						fData.deg = String.valueOf(jDeg);

						JSONArray jArray3 = json4.getJSONArray("weather");
						System.out.println("nameArr " + jArray3);

						for (int j = 0; j < jArray3.length(); j++) {
							JSONObject json6 = jArray3.getJSONObject(j);

							String jWname = json6.getString("main");
							String jWdes = json6.getString("description");

							String jIcon = json6.getString("icon");

							System.out.println("name " + jWname + " : " + jWdes);
							//ListData fData2 = new ListData(); 
							fData.wName = jWname;
							fData.wDescription = jWdes;

							fData.wIcon = "w_" + jIcon;

							//fDatas.add(fData2);
						}

						fDatas.add(fData);
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
				eMsg = e.toString();
			}

			return eMsg;
		}

		@Override
		protected void onPostExecute(String result) {
			//Log.e(TAG, "Response from server: " + result);

			if (fDatas.isEmpty()) {
				progress.dismiss();
				swipeRefreshLayout.setRefreshing(false);
			} else {

				WeatherNewListLazyAdapter adapter = new WeatherNewListLazyAdapter(getApplicationContext(), fDatas);
				list.setAdapter(adapter);

				TextView cityText = (TextView) findViewById(R.id.imWeatherCity);
				cityText.setText(jCity + "," + jCountry);

				progress.dismiss();
				swipeRefreshLayout.setRefreshing(false);
			}

			super.onPostExecute(result);
		}

	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		fDatas.clear();
		new GetWeatherData().execute();
	}
	public void onHomeClick(View v) {
		startActivity(new Intent(WeatherNewForcastList.this, DashboardActivity.class));
		finish();
	}
}

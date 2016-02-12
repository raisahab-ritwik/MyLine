package com.seva60plus.hum.utilities.weather;

import java.io.IOException;

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
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.seva60plus.hum.R;
import com.seva60plus.hum.activity.DashboardActivity;
import com.seva60plus.hum.activity.HumDetailsView;
import com.seva60plus.hum.activity.MenuLay;
import com.seva60plus.hum.custom.NoInternetPage;
import com.seva60plus.hum.util.Util;

public class WeatherNewMain extends Activity {

	public static final String MY_PREFS_NAME = "MyPrefsFile";

	String wCity = "";
	Dialog progress;
	private AnimationDrawable progressAnimation;

	TextView textCity, textTemp, textDes, textSpeed, textPressure, textHumidity;
	ImageView imageTemp;

	ImageView backBtn, menuIcon;
	RelativeLayout backSetup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weather_new_activity_main);
		//------START------------For Progress Spinner--------------

		progress = new Dialog(WeatherNewMain.this);
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
		//-------END-----------For Progress Spinner--------------    â€‹

		textCity = (TextView) findViewById(R.id.imWeatherCity);
		textTemp = (TextView) findViewById(R.id.temperature);
		textDes = (TextView) findViewById(R.id.description);
		textSpeed = (TextView) findViewById(R.id.windSpeed);
		textHumidity = (TextView) findViewById(R.id.humidity);
		textPressure = (TextView) findViewById(R.id.pressure);

		imageTemp = (ImageView) findViewById(R.id.imWeatherIcon);

		SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
		wCity = prefs.getString("shareWCity", "");
		System.out.println("CITYSHARED: " + wCity);

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
				if (Util.isInternetAvailable(WeatherNewMain.this)) {

					Intent i = new Intent(WeatherNewMain.this, HumDetailsView.class);
					startActivity(i);
					overridePendingTransition(R.anim.slide_in_right, R.anim.slide_leave_right);

				} else {
					Intent i = new Intent(WeatherNewMain.this, NoInternetPage.class);
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

			}
		});

		Button forcastBtn = (Button) findViewById(R.id.forcast_button);
		forcastBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intObj = new Intent(WeatherNewMain.this, WeatherNewForcastList.class);
				startActivity(intObj);
				finish();
			}
		});

		if (wCity.equals("") || wCity == null) {
			Intent intObj = new Intent(WeatherNewMain.this, WeatherNewChangeLocation.class);
			startActivity(intObj);
			finish();
		} else {

			new GetNowWeatherData().execute();

		}

		RelativeLayout refreshlay = (RelativeLayout) findViewById(R.id.refreshLay);
		refreshlay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (wCity.equals("") || wCity == null) {
					Intent intObj = new Intent(WeatherNewMain.this, WeatherNewChangeLocation.class);
					startActivity(intObj);
					finish();
				} else {

					new GetNowWeatherData().execute();

				}

			}
		});

	}

	private class GetNowWeatherData extends AsyncTask<Void, Integer, String> {

		JSONObject json2;
		String eMsg = "";

		String jCity = "", jCountry = "";
		String jTemp = "", jPressure = "", jWspeed = "", jHumidity = "";
		String jIcon = "";
		String jWdes = "";

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

			String str2 = "";
			HttpResponse response2;
			HttpClient myClient2 = new DefaultHttpClient();
			HttpPost myConnection2 = new HttpPost("http://api.openweathermap.org/data/2.5/weather?q=" + wCity + "&APPID=a726d4859fa320ffc01b593e3df2acbe");

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

					jCity = json2.getString("name");

					JSONObject json3 = json2.getJSONObject("sys");

					jCountry = json3.getString("country");

					JSONArray jsonArry = json2.getJSONArray("weather");
					for (int i = 0; i < jsonArry.length(); i++) {

						JSONObject json4 = jsonArry.getJSONObject(i);

						jWdes = json4.getString("description");
						jIcon = "w_" + json4.getString("icon");

					}

					JSONObject json5 = json2.getJSONObject("main");
					jTemp = String.valueOf(Math.round((json5.getDouble("temp") - 275.15))) + "°C";
					jPressure = String.valueOf(json5.getDouble("pressure"));
					jHumidity = String.valueOf(json5.getDouble("humidity"));

					JSONObject json6 = json2.getJSONObject("wind");
					jWspeed = String.valueOf(json6.getDouble("speed"));

				}
			} catch (Exception e) {
				e.printStackTrace();
				eMsg = e.toString();
			}

			return eMsg;
		}

		@Override
		protected void onPostExecute(String result) {

			progress.dismiss();

			textCity.setText(jCity + "," + jCountry);

			imageTemp.setImageResource(((Context) getApplicationContext()).getResources().getIdentifier(String.valueOf(jIcon), "drawable",
					((Context) getApplicationContext()).getPackageName()));

			textTemp.setText(jTemp);
			textDes.setText(jWdes);
			textSpeed.setText(jWspeed + " m/s");
			textHumidity.setText(jHumidity + " %");
			textPressure.setText(jPressure + " kPa");

			super.onPostExecute(result);
		}

	}
	public void onHomeClick(View v) {
		startActivity(new Intent(WeatherNewMain.this, DashboardActivity.class));
		finish();
	}
}

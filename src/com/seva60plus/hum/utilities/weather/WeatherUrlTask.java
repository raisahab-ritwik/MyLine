package com.seva60plus.hum.utilities.weather;

import java.io.IOException;

import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.widget.Toast;


public class WeatherUrlTask extends AsyncTask<Void, Integer, Integer> {
	private String urlStrDay;
	private String urlStrForecast;
	private String title;
	private String title2;
	private WeatherData data;
	private WeatherFirstActivity firstActivity;
	private String greg = "UrlTask";
	private WeatherDayWeather nowWeather;
	private WeatherDayWeather[] forecastWeather;
	JSONObject joDay;
	JSONObject joForecast;

	public WeatherUrlTask(WeatherFirstActivity firstActivity, WeatherData d, String str1,
			String str2, String title,String title2) {
		WeatherMyLog.d(greg, "constructor ");
		data = d;
		this.title = title;
		this.title2 = title2;
		urlStrDay = str1; // day
		urlStrForecast = str2; // forecast
		this.firstActivity = firstActivity;

	}

	@Override
	protected void onPreExecute() {
		firstActivity.loadAnimationStart();
		super.onPreExecute();
	}

	@Override
	protected Integer doInBackground(Void... arg0) {
		if (isCancelled())
			return null;

		try {// day
			System.out.println("*****UR*****L"+urlStrDay);
			
			WeatherMyLog.d(greg, urlStrDay);
			joDay = urlConnect2(urlStrDay);
			parsDay(joDay);
		} catch (JSONException e) {
			e.printStackTrace();
			return 1;
		} catch (IOException e) {
			e.printStackTrace();
			return 2;
		} catch (ParseException e) {
			e.printStackTrace();
			return 3;
		}

		if (isCancelled())
			return null;

		// try {
		// Thread.sleep(10000); // openweather ask wait 10 seconds between url
		// // request
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// if (isCancelled()) return null;

		try {// forecast
			WeatherMyLog.d(greg, urlStrForecast);
			joForecast = urlConnect2(urlStrForecast);
			parsForcast(joForecast);
		} catch (JSONException e) {
			e.printStackTrace();
			return 1;

		} catch (IOException e) {
			e.printStackTrace();
			return 2;
		} catch (ParseException e) {
			e.printStackTrace();
			return 3;
		}
		return 0;
	}

	@Override
	protected void onPostExecute(Integer result) {
		switch (result) {
		// if data was not received
		case 2:
			Toast.makeText(firstActivity, "�������� ����������� � ���������",
					Toast.LENGTH_LONG).show();
			break;
		case 1:
			Toast.makeText(firstActivity,
					"������ �� ����� �������� �� ������ ������",
					Toast.LENGTH_LONG).show();
			break;
		case 3:
			Toast.makeText(firstActivity,
					"error", 
					Toast.LENGTH_LONG).show();
			break;
			
		// if data has been successfully received
		case 0:
			// save query string and title to refresh data next time
			data.urlStrDay = urlStrDay;
			data.urlStrForecast = urlStrForecast;
			data.title = title;
			data.title2 = title2;
			data.setForecast(forecastWeather);
			data.setNowWeather(nowWeather);

			Editor editor = firstActivity.mSettings.edit();
			editor.putString("title", title);
			editor.putString("title2", title2);
			editor.putString("urlStrDay", urlStrDay);
			editor.putString("urlStrForecast", urlStrForecast);
			editor.putString("joDay", String.valueOf(joDay));
			editor.putString("joForecast", String.valueOf(joForecast));
			editor.apply();

			// refresh visible activity
			if (firstActivity.visibleOnScreen)
				firstActivity.afterUrlTask();
			else
				firstActivity.showNewData = true;
			break;
		}
		firstActivity.loadAnimationStop();
	}



	JSONObject urlConnect2(String url) throws ParseException,
			ClientProtocolException, JSONException, IOException {
		JSONObject jo = new JSONObject(
				EntityUtils.toString(new DefaultHttpClient().execute(
						new HttpGet(url)).getEntity()));
		return jo;
	}

	void parsForcast(JSONObject weatherJson) throws JSONException {
		WeatherMyLog.d(greg, "try pars jsonForecast: " + weatherJson);
		if (weatherJson.has("cnt")) {
			WeatherMyLog.d(greg, "try pars forecast start");
			int d = weatherJson.getInt("cnt");
			WeatherDayWeather[] forecast = new WeatherDayWeather[d];
			JSONArray list = weatherJson.getJSONArray("list");
			for (int i = 0; i <= d-1; i++) {
				JSONObject day = (JSONObject) list.get(i);
				WeatherMyLog.d("greg", "forecast for (i=" + i + ")");
				forecast[i] = new WeatherDayWeather();
				forecast[i].parsForecast(day, firstActivity);
			}
			forecastWeather = forecast;
			return;
		}
		throw new JSONException("server error");
	}

	void parsDay(JSONObject weatherJson) throws JSONException {
		WeatherMyLog.d(greg, "try pars jsonDay: " + weatherJson);
		if (weatherJson.has("main")) {
			WeatherMyLog.d(greg, "try pars nowweather start");
			WeatherDayWeather nowWeather = new WeatherDayWeather();
			nowWeather.parsDay(weatherJson, firstActivity);
			this.nowWeather = nowWeather;
			return;
		}
		throw new JSONException("server error");
	}

}

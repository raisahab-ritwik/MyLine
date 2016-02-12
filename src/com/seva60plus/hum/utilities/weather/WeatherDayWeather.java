package com.seva60plus.hum.utilities.weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.seva60plus.hum.R;

public class WeatherDayWeather {
	String greg = "DayWeather";
	public String date;
	public String city;
	public String dayTemperature;
	public String nightTemperature;
	public String eveTemperature;
	public String mornTemperature;
	public String windSpeed;
	public String humidity;
	public String nowWeather;
	public int imageId;
	public String pressure;
	public String description;
	Context cntxt;

	public void parsDay(JSONObject json, Context context) throws JSONException {
		WeatherMyLog.d(greg, "start pars day weather " + json);

		cntxt = (Context) context;
		JSONObject main;

		main = (JSONObject) json.get("main");

		JSONArray weather = (JSONArray) json.get("weather");
		JSONObject weather2 = (JSONObject) weather.get(0);
		JSONObject wind = (JSONObject) json.get("wind");
		//city = (String) json.get("name");
		city = json.getString("name");

		System.out.println("City Check :: " + city);

		dayTemperature = temperatureEdit(main.getDouble("temp"));

		pressure = String.valueOf(main.get("pressure")) + context.getString(R.string.pressureUnit);
		humidity = String.valueOf(main.get("humidity")) + context.getString(R.string.percent);
		windSpeed = context.getString(windDir(wind.getInt("deg"))) + " " + String.valueOf(wind.getInt("speed")) + " " + context.getString(R.string.speedUnit);
		imageId = ((Context) context).getResources().getIdentifier("w_" + String.valueOf(weather2.get("icon")), "drawable",
				((Context) context).getPackageName());

		description = context.getString(((Context) context).getResources().getIdentifier("d" + String.valueOf(weather2.get("id")), "string",
				((Context) context).getPackageName()));
	};

	public void parsForecast(JSONObject json, Context context) throws JSONException {
		WeatherMyLog.d(greg, "pars day forecast " + json);
		cntxt = (Context) context;

		JSONArray weather = (JSONArray) json.get("weather");
		JSONObject weather2 = (JSONObject) weather.get(0);
		JSONObject temp = (JSONObject) json.get("temp");

		date = new java.text.SimpleDateFormat("dd.MM.yyyy").format(new java.util.Date(json.getLong("dt") * 1000));

		//System.out.println("******DAY****"+date.get(Calendar.DAY_OF_WEEK));

		dayTemperature = temperatureEdit(temp.getDouble("day"));
		nightTemperature = temperatureEdit(temp.getDouble("night"));
		mornTemperature = temperatureEdit(temp.getDouble("morn"));
		eveTemperature = temperatureEdit(temp.getDouble("eve"));

		pressure = context.getString(R.string.pressure) + String.valueOf(json.get("pressure")) + context.getString(R.string.pressureUnit);
		humidity = context.getString(R.string.humidity) + String.valueOf(json.get("humidity")) + context.getString(R.string.percent);
		windSpeed = context.getString(R.string.wind) + " " + context.getString(windDir(json.getInt("deg"))) + " " + String.valueOf(json.getInt("speed")) + " "
				+ context.getString(R.string.speedUnit);
		imageId = ((Context) context).getResources().getIdentifier("w_" + String.valueOf(weather2.get("icon")), "drawable",
				((Context) context).getPackageName());

		description = context.getString(((Context) context).getResources().getIdentifier("d" + String.valueOf(weather2.get("id")), "string",
				((Context) context).getPackageName()));

	};

	private String temperatureEdit(Double t) {
		int T = (int) Math.round(t - 273.15);
		String str = String.valueOf(T);
		if (T > 0)
			str = "+" + str;
		return str;
	};

	private int windDir(int i) {
		if (i <= 22 | i > 337)
			return (R.string.N);
		if (i > 22 && i <= 67)
			return (R.string.NE);
		if (i > 67 && i <= 112)
			return (R.string.E);
		if (i > 112 && i <= 157)
			return (R.string.SE);
		if (i > 157 && i <= 202)
			return (R.string.S);
		if (i > 202 && i <= 247)
			return (R.string.SW);
		if (i > 247 && i <= 292)
			return (R.string.W);
		if (i > 292 && i <= 337)
			return (R.string.NW);
		return (R.string.hello_world);
	}
}

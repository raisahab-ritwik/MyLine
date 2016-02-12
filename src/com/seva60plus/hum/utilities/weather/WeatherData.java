package com.seva60plus.hum.utilities.weather;

public class WeatherData {
	String greg = "dataClass";

	private WeatherDayWeather nowWeather;
	private WeatherDayWeather[] forecast;


	public final String strWeather = "http://api.openweathermap.org/data/2.5/weather?";
	public final String strForecast = "http://api.openweathermap.org/data/2.5/forecast/daily?";
	public String urlStrDay;
	public String urlStrForecast;
	public String title;
	public String title2;

	public WeatherDayWeather getNowWeather() {
		return nowWeather;
	}

	public void setNowWeather(WeatherDayWeather nowWeather) {
		WeatherMyLog.d(greg, "setWeather");
		this.nowWeather = nowWeather;
	}

	public WeatherDayWeather[] getForecast() {
		return forecast;
	}

	public void setForecast(WeatherDayWeather forecast[]) {
		WeatherMyLog.d(greg, "setForecast");
		this.forecast = forecast;
	}
}

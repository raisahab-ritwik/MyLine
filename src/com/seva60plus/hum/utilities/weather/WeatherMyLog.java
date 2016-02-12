package com.seva60plus.hum.utilities.weather;



import com.seva60plus.hum.BuildConfig;

import android.util.Log;

public class WeatherMyLog {
	public static void d( String tag, String message) {
  if (BuildConfig.DEBUG) {
      Log.d(tag, message);
  }
} 
	
}

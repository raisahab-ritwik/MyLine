package com.seva60plus.hum.alarmnotification;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Handler;
import android.os.IBinder;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import com.seva60plus.hum.network.PostDataStatices;
import com.seva60plus.hum.util.Contact;
import com.seva60plus.hum.util.DatabaseHandler;
import com.seva60plus.hum.util.GPSTracker;
import com.seva60plus.hum.util.UserFunctions;
import com.seva60plus.hum.util.Util;

public class NotificationAlarmService extends Service {
	private String TAG = "NotificationAlarmService";
	//MediaPlayer player;
	private Handler handler;
	private Thread myThread = null;
	private int i = 0;
	private DatabaseHandler db;

	private JSONObject json;
	private String code = "";

	private Boolean isInternetPresent = false;

	private String geoLocation = "";
	private String humPhone = "";

	private final String MY_PREFS_NAME = "MyPrefsFile";

	GPSTracker gps;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {

		Log.d(TAG, "onCreate");

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		db = new DatabaseHandler(this);
		try {
			humPhone = Util.getSharedPreference(NotificationAlarmService.this).getString("humPhone", "");

		} catch (Exception e) {
			e.printStackTrace();
		}
		handler = new Handler();

		Runnable myRunnableThread = new CountDownRunner();
		myThread = new Thread(myRunnableThread);
	}

	@Override
	public void onDestroy() {

		Log.d(TAG, "onDestroy");

	}

	@Override
	public void onStart(Intent intent, int startid) {

		Log.d(TAG, "onStart");

		myThread.start();

	}

	private void startAlarmSleep() {

		Log.i(TAG, "1 Alarm fired.");
		Intent intent = new Intent(this, PostDataStatices.class);
		intent.setAction("com.seva60Plus.alarm.ACTION");
		PendingIntent pendingIntent = PendingIntent.getBroadcast(NotificationAlarmService.this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		Calendar calendar = Calendar.getInstance();
		//time 10:10:10
		calendar.set(Calendar.HOUR_OF_DAY, 10);
		calendar.set(Calendar.MINUTE, 10);
		calendar.set(Calendar.SECOND, 10);
		AlarmManager alarm = (AlarmManager) NotificationAlarmService.this.getSystemService(Context.ALARM_SERVICE);
		alarm.cancel(pendingIntent);
		alarm.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
	}

	private void startAlarmMood() {
		Log.i(TAG, "Mood Alarm fired.");
		/**
		 * call broadcost reciver for send sms
		 */
		Intent intent = new Intent(this, PostDataStatices.class);
		intent.setAction("com.seva60Plus.alarm.ACTION");
		PendingIntent pendingIntent = PendingIntent.getBroadcast(NotificationAlarmService.this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		Calendar calendar = Calendar.getInstance();
		//time 11:10:10
		calendar.set(Calendar.HOUR_OF_DAY, 11);
		calendar.set(Calendar.MINUTE, 10);
		calendar.set(Calendar.SECOND, 10);
		AlarmManager alarm = (AlarmManager) NotificationAlarmService.this.getSystemService(Context.ALARM_SERVICE);
		alarm.cancel(pendingIntent);
		alarm.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
	}

	private void startAlarmExercise() {
		Log.i(TAG, "Exercise Alarm fired.");
		/**
		 * call broadcost reciver for send sms
		 */
		Intent intent = new Intent(this, PostDataStatices.class);
		intent.setAction("com.seva60Plus.alarm.ACTION");
		PendingIntent pendingIntent = PendingIntent.getBroadcast(NotificationAlarmService.this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		Calendar calendar = Calendar.getInstance();
		//time 17:10:10
		calendar.set(Calendar.HOUR_OF_DAY, 17);
		calendar.set(Calendar.MINUTE, 10);
		calendar.set(Calendar.SECOND, 10);
		AlarmManager alarm = (AlarmManager) NotificationAlarmService.this.getSystemService(Context.ALARM_SERVICE);
		alarm.cancel(pendingIntent);
		alarm.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
	}

	private Location getLocation() {
		Location mLocation = null;
		gps = new GPSTracker(NotificationAlarmService.this);

		// check if GPS enabled    
		if (gps.canGetLocation()) {

			mLocation = gps.getLocation();
		} else {

			gps.showSettingsAlert();
		}
		return mLocation;
	}

	private class CountDownRunner implements Runnable {

		public void run() {

			while (!Thread.currentThread().isInterrupted()) {

				try {
					doWork();
					Thread.sleep(1000); // Pause of 1 Second

				} catch (InterruptedException e) {
					e.printStackTrace();
					Thread.currentThread().interrupt();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		@SuppressWarnings("deprecation")
		private void doWork() {

			runOnUiThread(new Runnable() {

				public void run() {

					try {

						Date dt = new Date();

						int hours = dt.getHours();
						int minutes = dt.getMinutes();
						int seconds = dt.getSeconds();

						String curTime = hours + ":" + minutes + ":" + seconds;

						for (i = 0; i < 24; i++) {

							String mHour = Integer.toString(i);
							String myTime = mHour + ":1:0";

							if (myTime.contains(curTime)) {

								geoLocation = Util.getSharedPreference(NotificationAlarmService.this).getString("geoLocation", "");

								if (!geoLocation.equals("1")) {

									System.out.println("Location Not submitted due User Permission");

								} else {

									Location mLocation = getLocation();
									String MyLat = Double.toString(mLocation.getLatitude());
									String MyLang = Double.toString(mLocation.getLongitude());
									Log.i(TAG, "Latitude : " + mLocation.getLatitude() + "\nLongitude : " + mLocation.getLongitude());

									try {

										UserFunctions userFunction = new UserFunctions();
										json = userFunction.sendLatlang(humPhone, MyLat, MyLang, curTime);

										code = json.getString("code");

									} catch (Exception e) {

										e.printStackTrace();
										System.out.println(e.toString());
									}
									if (i == 23) {

										i = 0;
										System.out.println("i value: " + i);

									} else {
										System.out.println("i value from else: " + i);
									}

								}
							}

						}

						if (curTime.contains("10:10:10")) {

							startAlarmSleep();

						} else if (curTime.contains("11:10:10")) {

							startAlarmMood();
						} else if (curTime.contains("17:10:10")) {

							startAlarmExercise();
						}

					} catch (Exception e) {

						e.printStackTrace();
					}
				}
			});
		}
	}

	private void runOnUiThread(Runnable runnable) {

		handler.post(runnable);

	}

}

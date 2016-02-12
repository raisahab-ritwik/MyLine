package com.seva60plus.hum.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.seva60plus.hum.R;
import com.seva60plus.hum.Listeners.AlertDialogCallBack;
import com.seva60plus.hum.Listeners.OnCheckUserListener;
import com.seva60plus.hum.alarmnotification.NotificationAlarmService;
import com.seva60plus.hum.asynctask.CheckUser;
import com.seva60plus.hum.custom.NoInternetPage;
import com.seva60plus.hum.custom.ProgressHUD;
import com.seva60plus.hum.custom.SlowConnectionPage;
import com.seva60plus.hum.custom.SlowInternetPage;
import com.seva60plus.hum.model.Hum;
import com.seva60plus.hum.model.UserClass;
import com.seva60plus.hum.network.PostDataStatices;
import com.seva60plus.hum.util.PreferenceUtil;
import com.seva60plus.hum.util.SpeedTestService;
import com.seva60plus.hum.util.Util;
import com.seva60plus.hum.wellbeing.sync.Constant;

@SuppressWarnings("unused")
public class SplashActivity extends Activity implements OnCheckUserListener, OnCancelListener {

	private Button go, termsBtn, privacyBtn;
	private String code1 = "";
	private String subId;
	private TelephonyManager tm;

	private String regValue = "";
	private static final String MY_PREFS_NAME = "MyPrefsFile";

	private String eMsg = "";
	private ProgressDialog progress;

	private Context mContext;
	private String TAG = "SplashActivity";
	private Account appAccount;
	MyReceiver myReceiver;
	ProgressHUD mProgressHUD;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_activity);
		Log.v(TAG, "On Create");
		// Log.v(null, null);
		mContext = SplashActivity.this;
		initializeWellbeingSyncAdapter();
		stopService(new Intent(this, NotificationAlarmService.class));
		startService(new Intent(this, NotificationAlarmService.class));

		Thread myThread = null;

		Runnable myRunnableThread = new CountDownRunner();
		myThread = new Thread(myRunnableThread);
		myThread.start();

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		try {
			Log.i("Device id", tm.getDeviceId());
			Log.v("Subscriber Id", "" + tm.getSubscriberId().toString());
			subId = tm.getSubscriberId().toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
		regValue = prefs.getString("regValue", "");

		ImageView logo = (ImageView) findViewById(R.id.iv_app_logo);
		logo.setBackgroundResource(R.drawable.logo_animation);

		AnimationDrawable frameAnimation = (AnimationDrawable) logo.getBackground();

		frameAnimation.start();

		progress = new ProgressDialog(SplashActivity.this);
		progress.setCancelable(true);
		progress.setMessage("Please wait..");
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

		go = (Button) findViewById(R.id.btn_tap_to_enter);
		go.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				Hum mHum = PreferenceUtil.fetchUserClass(SplashActivity.this);
				if (mHum != null && mHum.getPhone().length() > 0) {

					System.out.println("Phone not null");
					startActivity(new Intent(SplashActivity.this, DashboardActivity.class));
				} else {
					if (Util.isInternetAvailable(mContext))
						speedCheck();
					else if (!Util.isInternetAvailable(mContext)) {
						Intent i = new Intent(SplashActivity.this, NoInternetPage.class);
						startActivity(i);
						overridePendingTransition(0, 0);
					}
				}

			}
		});

		termsBtn = (Button) findViewById(R.id.btn_terms_of_service);
		termsBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent myIntent = new Intent(SplashActivity.this, Terms.class);
				startActivity(myIntent);

			}
		});

		privacyBtn = (Button) findViewById(R.id.btn_privacy_policy);
		privacyBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent myIntent = new Intent(SplashActivity.this, Privacy.class);
				startActivity(myIntent);
			}
		});

	}

	@Override
	public void onCheckUser(String status) {

		if (status.equals("200")) {
			// Registered
			SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
			editor.putString("regValue", "OK");
			editor.commit();

			Intent myIntent = new Intent(SplashActivity.this, DashboardActivity.class);
			startActivity(myIntent);
			finish();

		} else {
			// not Registered
			Intent myIntent = new Intent(SplashActivity.this, RegistrationActivity.class);
			startActivity(myIntent);
		}

	}

	/**
	 * call broadcost reciver for send sms
	 */
	private void fireAlarm() {

		Intent intent = new Intent(this, PostDataStatices.class);
		intent.setAction("com.seva60Plus.alarm.ACTION");
		PendingIntent pendingIntent = PendingIntent.getBroadcast(SplashActivity.this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 11);
		calendar.set(Calendar.MINUTE, 10);
		calendar.set(Calendar.SECOND, 10);
		AlarmManager alarm = (AlarmManager) SplashActivity.this.getSystemService(Context.ALARM_SERVICE);
		alarm.cancel(pendingIntent);
		alarm.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
	}

	/**
	 * call broadcost reciver for send sms
	 */
	private void fireAlarm2() {

		Intent intent = new Intent(this, PostDataStatices.class);
		intent.setAction("com.seva60Plus.alarm.ACTION");
		PendingIntent pendingIntent = PendingIntent.getBroadcast(SplashActivity.this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 17);
		calendar.set(Calendar.MINUTE, 10);
		calendar.set(Calendar.SECOND, 10);
		AlarmManager alarm = (AlarmManager) SplashActivity.this.getSystemService(Context.ALARM_SERVICE);
		alarm.cancel(pendingIntent);
		alarm.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
	}

	/**
	 * call broadcost reciver for send sms
	 */
	private void fireAlarm3() {

		Intent intent = new Intent(this, PostDataStatices.class);
		intent.setAction("com.seva60Plus.alarm.ACTION");
		PendingIntent pendingIntent = PendingIntent.getBroadcast(SplashActivity.this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 21);
		calendar.set(Calendar.MINUTE, 10);
		calendar.set(Calendar.SECOND, 10);
		AlarmManager alarm = (AlarmManager) SplashActivity.this.getSystemService(Context.ALARM_SERVICE);
		alarm.cancel(pendingIntent);
		alarm.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
	}

	private class CountDownRunner implements Runnable {
		// @Override
		public void run() {
			while (!Thread.currentThread().isInterrupted()) {
				try {
					doWork();
					Thread.sleep(1000); // Pause of 1 Second
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				} catch (Exception e) {
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

						if (curTime.contains("11:10:10")) {
							System.out.println("Time IS1 : " + curTime);
							fireAlarm();
						} else if (curTime.contains("17:10:10")) {
							System.out.println("Time IS2 : " + curTime);
							fireAlarm2();
						} else if (curTime.contains("21:10:10")) {
							System.out.println("Time IS3 : " + curTime);
							fireAlarm3();
						} else {

						}

					} catch (Exception e) {
					}
				}
			});
		}
	}

	/**
	 * Initilaizes the sync adapter with proper Authentication through
	 * authentication service.
	 * **/
	private void initializeWellbeingSyncAdapter() {

		appAccount = new Account(Constant.ACCOUNT_NAME, Constant.ACCOUNT_TYPE);
		AccountManager accountManager = AccountManager.get(this);
		accountManager.addAccountExplicitly(appAccount, null, null);
		ContentResolver.setIsSyncable(appAccount, Constant.PROVIDER, 1);
		ContentResolver.setMasterSyncAutomatically(true);
		ContentResolver.setSyncAutomatically(appAccount, Constant.PROVIDER, true);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.v(TAG, "On Destroy");

	}

	// ===================================================

	private void speedCheck() {
		myReceiver = new MyReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(SpeedTestService.MY_ACTION);
		registerReceiver(myReceiver, intentFilter);

		startService(new Intent(this, SpeedTestService.class));
		mProgressHUD = ProgressHUD.show(mContext, "Checking internet speed...", true, true, this);
	}

	private class MyReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.v("MyReceiver", "MyReceiver");

			mProgressHUD.dismiss();
			boolean isStrongNetwork = intent.getBooleanExtra("NETWORK_CHECK", false);

			unregisterReceiver(myReceiver);

			Log.v("TAG", "isStrongNetwork: " + isStrongNetwork);

			if (isStrongNetwork) {

				System.out.println("No Phone");
				CheckUser checkUserAsync = new CheckUser(mContext, subId);
				checkUserAsync.mListener = SplashActivity.this;
				checkUserAsync.execute();

			} else {

				Intent intObj = new Intent(SplashActivity.this, SlowConnectionPage.class);
				startActivity(intObj);
				overridePendingTransition(0, 0);
			}
		}

	}

	@Override
	public void onCancel(DialogInterface dialog) {
		mProgressHUD.dismiss();

	}

}

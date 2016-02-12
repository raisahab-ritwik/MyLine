package com.seva60plus.hum.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnShowListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.AnimationDrawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.seva60plus.hum.R;
import com.seva60plus.hum.Listeners.CheckUpdateListener;
import com.seva60plus.hum.asynctask.CheckForUpdatesAsync;
import com.seva60plus.hum.care.CareActivity;
import com.seva60plus.hum.custom.NoInternetPage;
import com.seva60plus.hum.custom.ProgressHUD;
import com.seva60plus.hum.custom.SlowConnectionPage;
import com.seva60plus.hum.db.SaathiDB;
import com.seva60plus.hum.kitchen.KitchenActivity;
import com.seva60plus.hum.mediacentre.MediaCentreDashBoard;
import com.seva60plus.hum.nearby.NearByDashboard;
import com.seva60plus.hum.pillreminder.TakeThePill;
import com.seva60plus.hum.reminder.taskreminder.ReminderListActivity;
import com.seva60plus.hum.sathi.Saathi;
import com.seva60plus.hum.sathi.SaathiActivity;
import com.seva60plus.hum.sharelocation.MapShareActivity;
import com.seva60plus.hum.specialoffers.OffersListActivity;
import com.seva60plus.hum.util.CheckUpdates;
import com.seva60plus.hum.util.SpeedTestService;
import com.seva60plus.hum.util.Util;
import com.seva60plus.hum.utilities.Utilities;
import com.seva60plus.hum.wellbeing.WellBeingActivitySleep;

public class DashboardActivity extends Activity implements CheckUpdateListener, OnCancelListener {

	private final String MY_PREFS_NAME = "MyPrefsFile";

	private LinearLayout pill, rl_saathi, location, about, utilities, knowledge, nearby, reminder, welbing;
	private RelativeLayout backBtn, rlHome;
	private LocationManager lm;

	private Dialog progress;
	private AnimationDrawable progressAnimation;

	private String curVersion = "";
	private PackageInfo pInfo;
	private Context mContext;

	private LinearLayout llMenu;
	private String TAG = "DashboardActivity";
	MyReceiver myReceiver;
	ProgressHUD mProgressHUD;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard);
		Log.v(TAG, "On Create");
		mContext = DashboardActivity.this;
		lm = (LocationManager) getSystemService(LOCATION_SERVICE);
		rlHome = (RelativeLayout) findViewById(R.id.rlHome);
		rlHome.setVisibility(View.GONE);

		// ------START------------For Progress Spinner--------------

		progress = new Dialog(DashboardActivity.this);
		progress.getWindow().setBackgroundDrawableResource(R.drawable.spinner_dialog_backround);

		// Remove the Title
		progress.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

		// progress.setTitle("");

		// Set the View of the Dialog - Custom
		progress.setContentView(R.layout.custom_progress_dialog);

		// Set the title of the Dialog
		// dialog.setTitle("Title...");

		ImageView progressSpinner = (ImageView) progress.findViewById(R.id.progressSpinner);

		// Set the background of the image - In this case an animation
		// (/res/anim folder)
		progressSpinner.setBackgroundResource(R.anim.spinner_progress_animation);

		// Get the image background and attach the AnimationDrawable to it.
		progressAnimation = (AnimationDrawable) progressSpinner.getBackground();

		// Start the animation after the dialog is displayed.
		progress.setOnShowListener(new OnShowListener() {
			@Override
			public void onShow(DialogInterface dialog) {
				progressAnimation.start();
			}

		});

		progress.setCanceledOnTouchOutside(false);

		llMenu = (LinearLayout) findViewById(R.id.llMenu);
		llMenu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intObj = new Intent(DashboardActivity.this, MenuLay.class);
				startActivity(intObj);
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			}
		});

		LinearLayout offer = (LinearLayout) findViewById(R.id.offer_btn);
		ImageView iv_special_offers=(ImageView) findViewById(R.id.iv_special_offers);
		iv_special_offers.setBackgroundResource(R.drawable.offer_animation);

		AnimationDrawable frameAnimation = (AnimationDrawable) iv_special_offers.getBackground();

		// Start the animation (looped playback by default).
		frameAnimation.start();

		offer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(DashboardActivity.this, OffersListActivity.class);
				startActivity(i);
				overridePendingTransition(0, 0);
			}
		});

		pill = (LinearLayout) findViewById(R.id.pill_btn);
		pill.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(DashboardActivity.this, TakeThePill.class);
				startActivity(i);
				overridePendingTransition(0, 0);
			}
		});

		rl_saathi = (LinearLayout) findViewById(R.id.rl_saathi);
		rl_saathi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (Util.isInternetAvailable(mContext)) {
					Intent i = new Intent(DashboardActivity.this, SaathiActivity.class);
					startActivity(i);
					overridePendingTransition(0, 0);
				} else {

					ArrayList<Saathi> saathiList = new ArrayList<Saathi>();
					saathiList = new SaathiDB().getSaathiList(mContext);
					if (saathiList.size() > 0) {
						// go to SaathiActivity
						Intent i = new Intent(DashboardActivity.this, SaathiActivity.class);
						startActivity(i);
						overridePendingTransition(0, 0);
					} else {
						Intent i = new Intent(DashboardActivity.this, NoInternetPage.class);
						startActivity(i);
						overridePendingTransition(0, 0);
					}
				}
			}
		});

		welbing = (LinearLayout) findViewById(R.id.well_bing_btn);
		welbing.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(DashboardActivity.this, WellBeingActivitySleep.class);
				startActivity(i);
				overridePendingTransition(0, 0);
				// finish();
			}
		});

		location = (LinearLayout) findViewById(R.id.location_btn);
		location.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (Util.isInternetAvailable(mContext)) {

					if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
						// GPS Connection is Present

						Intent i = new Intent(DashboardActivity.this, MapShareActivity.class);
						startActivity(i);
						overridePendingTransition(0, 0);

					} else {
						final Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						startActivity(intent);
						overridePendingTransition(0, 0);
					}

				} else {
					// Internet connection is not present
					Intent i = new Intent(DashboardActivity.this, NoInternetPage.class);
					startActivity(i);
					overridePendingTransition(0, 0);

				}

			}
		});

		nearby = (LinearLayout) findViewById(R.id.nearby_btn);
		nearby.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (Util.isInternetAvailable(mContext)) {
					// Internet Connection is Present

					if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
						// GPS Connection is Present
						progress.dismiss();
						Intent i = new Intent(DashboardActivity.this, NearByDashboard.class);
						startActivity(i);
						overridePendingTransition(0, 0);

					} else {
						final Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						startActivity(intent);
						overridePendingTransition(0, 0);
					}

				}

				else {
					// Internet connection is not present
					Intent i = new Intent(DashboardActivity.this, NoInternetPage.class);
					startActivity(i);

				}
			}
		});

		about = (LinearLayout) findViewById(R.id.seva60_btn);
		about.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(DashboardActivity.this, AboutSeva60.class);
				startActivity(i);
				overridePendingTransition(0, 0);
				// finish();
			}
		});

		knowledge = (LinearLayout) findViewById(R.id.knownledge_base_btn);
		knowledge.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(DashboardActivity.this, MediaCentreDashBoard.class);
				startActivity(i);

			}
		});

		utilities = (LinearLayout) findViewById(R.id.utilities_btn);
		utilities.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(DashboardActivity.this, Utilities.class);
				startActivity(i);
				overridePendingTransition(0, 0);
				// finish();
			}
		});

		reminder = (LinearLayout) findViewById(R.id.money_reminder_btn);
		reminder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent i = new Intent(DashboardActivity.this, ReminderListActivity.class);
				startActivity(i);
				overridePendingTransition(0, 0);

			}
		});

		backBtn = (RelativeLayout) findViewById(R.id.back_settings);

		backBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (Util.isInternetAvailable(mContext)) {

					Intent i = new Intent(DashboardActivity.this, HumDetailsView.class);
					startActivity(i);
					overridePendingTransition(R.anim.slide_in_right, R.anim.slide_leave_right);
				} else {
					Intent i = new Intent(DashboardActivity.this, NoInternetPage.class);
					startActivity(i);
					overridePendingTransition(0, 0);
				}
			}
		});
		speedCheck();
	}

	@Override
	public void onBackPressed() {

	}

	// ====================================
	/** From here onwards Ritwik's coding 29-1-2016 **/

	public void onKitchenClick(View v) {
		startActivity(new Intent(mContext, KitchenActivity.class));

	}

	public void onCareClick(View v) {
		startActivity(new Intent(mContext, CareActivity.class));
	}

	private void checkForUpdates() {
		// *****************Check Updates *******************
		if (Util.isInternetAvailable(mContext)) {

			try {
				pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
			curVersion = pInfo.versionName;
			CheckForUpdatesAsync async = new CheckForUpdatesAsync(mContext);
			async.mListener = this;
			async.execute();

		} else {

		}

		// *******************End Check Updates
	}

	@Override
	public void getUpdateVersion(String updateVersion) {
		String newVersion = updateVersion;

		SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
		editor.putString("prvNewVersion", newVersion);
		editor.commit();

		System.out.println("cVersion: " + curVersion + "\n newVersion: " + newVersion);
		try {
			if (Double.parseDouble(curVersion) != Double.parseDouble(newVersion)) {

				Intent intObj = new Intent(DashboardActivity.this, CheckUpdates.class);
				startActivity(intObj);

			} else {
				// low version DO Nothing

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// ==================================
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
				checkForUpdates();
			} else {
				Intent intObj = new Intent(DashboardActivity.this, SlowConnectionPage.class);
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

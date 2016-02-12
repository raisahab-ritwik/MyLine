package com.seva60plus.hum.utilities;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.seva60plus.hum.R;
import com.seva60plus.hum.activity.DashboardActivity;
import com.seva60plus.hum.activity.HumDetailsView;
import com.seva60plus.hum.activity.MenuLay;
import com.seva60plus.hum.custom.NoInternetPage;
import com.seva60plus.hum.util.ConnectionDetector;
import com.seva60plus.hum.util.Util;
import com.seva60plus.hum.utilities.phonecontact.ContactListActivity;
import com.seva60plus.hum.utilities.weather.WeatherNewMain;

public class Utilities extends Activity {

	public static final String MY_PREFS_NAME = "MyPrefsFile";

	private RelativeLayout heartrate, contact, weather;
	private ImageView backBtn, menuIcon;
	private RelativeLayout backSetup;
	private ComponentName cn;
	private Camera camera = null;
	private boolean isFlashOn;
	private boolean hasFlash;
	private Parameters params;
	private RelativeLayout btnSwitch;

	// Connection detector class
	ConnectionDetector cd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.utilities_dashboard);

		btnSwitch = (RelativeLayout) findViewById(R.id.tourch_btn);
		hasFlash = getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

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
				if (Util.isInternetAvailable(Utilities.this)) {
					Intent i = new Intent(Utilities.this, HumDetailsView.class);
					startActivity(i);
					overridePendingTransition(R.anim.slide_in_right, R.anim.slide_leave_right);
				} else {
					Intent i = new Intent(Utilities.this, NoInternetPage.class);
					startActivity(i);
					overridePendingTransition(0, 0);
				}
			}
		});

		findViewById(R.id.llMenu).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intObj = new Intent(Utilities.this, MenuLay.class);
				startActivity(intObj);
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

			}
		});

		heartrate = (RelativeLayout) findViewById(R.id.heartbeat_btn);
		heartrate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				getCamera();

				if (camera != null) {
					camera.stopPreview();
					camera.setPreviewCallback(null);

					camera.release();
					camera = null;

					Intent i = new Intent(Utilities.this, HeartRateInstraction.class);
					startActivity(i);

					//finish();
				}
			}
		});

		// Switch button click event to toggle flash on/off
		btnSwitch.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				System.out.println("----Toarch Clik--");

				// get the camera
				getCamera();

				// displaying button image
				toggleButtonImage();

				if (hasFlash) {

					if (isFlashOn) {
						// turn off flash
						turnOffFlash();
					} else {
						// turn on flash
						turnOnFlash();
					}

				} else {
					System.out.println("!!---Flash : " + hasFlash);
					Toast.makeText(getApplicationContext(), "Sorry Flash is not available", Toast.LENGTH_SHORT).show();
				}

			}
		});

		weather = (RelativeLayout) findViewById(R.id.weather_btn);
		weather.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (Util.isInternetAvailable(getApplicationContext())) {

					Intent i = new Intent(Utilities.this, WeatherNewMain.class);
					startActivity(i);
					overridePendingTransition(0, 0);

				} else {
					Intent i = new Intent(Utilities.this, NoInternetPage.class);
					startActivity(i);
					overridePendingTransition(0, 0);

				}
			}
		});

		contact = (RelativeLayout) findViewById(R.id.contact_btn);
		contact.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent i = new Intent(Utilities.this, ContactListActivity.class);
				i.setType(ContactsContract.Contacts.CONTENT_ITEM_TYPE);

				startActivity(i);

			}
		});

	}

	// Get the camera
	private void getCamera() {
		if (camera == null) {
			try {
				camera = Camera.open();
				params = camera.getParameters();
			} catch (RuntimeException e) {
				Log.e("Camera Error. Failed to Open. Error: ", e.getMessage());
			}
		}
	}

	private void toggleButtonImage() {
		if (isFlashOn) {
			btnSwitch.setBackgroundResource(R.drawable.tourch_on);
		} else {
			btnSwitch.setBackgroundResource(R.drawable.tourch);
		}
	}

	// Turning On flash
	private void turnOnFlash() {
		if (!isFlashOn) {
			if (camera == null || params == null) {
				return;
			}
			// play sound
			//   playSound();

			params = camera.getParameters();
			params.setFlashMode(Parameters.FLASH_MODE_TORCH);
			camera.setParameters(params);
			camera.startPreview();
			isFlashOn = true;

			// changing button/switch image
			toggleButtonImage();
		}

	}

	// Turning Off flash
	private void turnOffFlash() {
		if (isFlashOn) {
			if (camera == null || params == null) {
				return;
			}
			// play sound
			// playSound();

			params = camera.getParameters();
			params.setFlashMode(Parameters.FLASH_MODE_OFF);
			camera.setParameters(params);
			camera.stopPreview();
			isFlashOn = false;

			// changing button/switch image
			toggleButtonImage();
		}
	}
	public void onHomeClick(View v) {
		startActivity(new Intent(Utilities.this, DashboardActivity.class));
		finish();
	}
}

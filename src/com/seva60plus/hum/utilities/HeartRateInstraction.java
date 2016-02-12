package com.seva60plus.hum.utilities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.seva60plus.hum.R;
import com.seva60plus.hum.activity.DashboardActivity;
import com.seva60plus.hum.activity.HumDetailsView;
import com.seva60plus.hum.activity.MenuLay;
import com.seva60plus.hum.custom.NoInternetPage;
import com.seva60plus.hum.util.ConnectionDetector;
import com.seva60plus.hum.util.Util;
import com.wrc.heartratemonitor.HeartRateMonitorMainActivity;

public class HeartRateInstraction extends Activity {

	Button heartStart;
	TextView headerTitleText;
	ImageView backBtn, homeBtn;
	RelativeLayout backSetup;
	//---by Dibyendu
	// flag for Internet connection status
	Boolean isInternetPresent = false;

	// Connection detector class
	ConnectionDetector cd;

	boolean hasFlash;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hart_rate_instruction);

		cd = new ConnectionDetector(getApplicationContext());// by Dibyendu

		hasFlash = this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);//---Dibyendu

		headerTitleText = (TextView) findViewById(R.id.header_title);
		headerTitleText.setText("Seva60Plus");
		Typeface font = Typeface.createFromAsset(getAssets(), "openSansBold.ttf");
		headerTitleText.setTypeface(font);

		backSetup = (RelativeLayout) findViewById(R.id.back_settings);

		backSetup.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				if (Util.isInternetAvailable(HeartRateInstraction.this)) {
					Intent myIntent = new Intent(HeartRateInstraction.this, HumDetailsView.class);
					startActivity(myIntent);
					overridePendingTransition(R.anim.slide_in_right, R.anim.slide_leave_right);

				} else {
					Intent i = new Intent(HeartRateInstraction.this, NoInternetPage.class);
					startActivity(i);
					overridePendingTransition(0, 0);
				}

			}
		});

		backBtn = (ImageView) findViewById(R.id.iv_back);

		backBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// Take action.

				finish();

			}
		});

		findViewById(R.id.llMenu).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// Take action.
				Intent myIntent = new Intent(HeartRateInstraction.this, MenuLay.class);
				startActivity(myIntent);
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

			}
		});

		LinearLayout banner = (LinearLayout) findViewById(R.id.footerLay);

		banner.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// Take action.
				/*
				 * Intent myIntent = new Intent(HeartRateInstraction.this,
				 * AdBanner.class); myIntent.putExtra("banner_value", "1");
				 * startActivity(myIntent);
				 */
				Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.ethnikyarn.com"));
				startActivity(i);

			}
		});

		heartStart = (Button) findViewById(R.id.startHeartBtn);
		heartStart.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// Take action.
				Intent myIntent = new Intent(HeartRateInstraction.this, HeartRateMonitorMainActivity.class);
				startActivity(myIntent);
				//Toast.makeText(getApplicationContext(), "Coming Soon", Toast.LENGTH_SHORT).show();
				/*
				 * if(hasFlash){ System.out.println("Flash : "+hasFlash); Intent
				 * myIntent = new Intent(HeartRateInstraction.this,
				 * HeartRateMonitor.class); startActivity(myIntent);
				 * 
				 * }else{ System.out.println("!!---Flash : "+hasFlash);
				 * Toast.makeText(getApplicationContext(),
				 * "Sorry Flash is not available", Toast.LENGTH_SHORT).show(); }
				 */
				/*
				 * Intent myIntent = new Intent(HeartRateInstraction.this,
				 * HeartRateMonitor.class); startActivity(myIntent);
				 */
			}
		});

	}
	public void onHomeClick(View v) {
		startActivity(new Intent(HeartRateInstraction.this, DashboardActivity.class));
		finish();
	}
}

package com.seva60plus.hum.specialoffers;

import android.app.Activity;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.seva60plus.hum.R;
import com.seva60plus.hum.activity.DashboardActivity;
import com.seva60plus.hum.activity.HumDetailsView;
import com.seva60plus.hum.activity.MenuLay;
import com.seva60plus.hum.custom.NoInternetPage;
import com.seva60plus.hum.util.ConnectionDetector;
import com.seva60plus.hum.util.Util;

public class OffersListElderCareActivity extends Activity {

	RelativeLayout option1, option2, option3, option4;

	RelativeLayout backBtn;
	ImageView menuIcon;
	ImageView backBtnSub;

	RelativeLayout visitWebBtn;
	RelativeLayout visitWebBtn2;
	//---by Dibyendu
	// flag for Internet connection status
	Boolean isInternetPresent = false;

	// Connection detector class
	ConnectionDetector cd;
	LocationManager lm;

	//------End

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.offers_eldercare);

		cd = new ConnectionDetector(getApplicationContext());// by Dibyendu
		lm = (LocationManager) getSystemService(LOCATION_SERVICE);// by Dibyendu

		LinearLayout banner = (LinearLayout) findViewById(R.id.footerLay);

		banner.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// Take action.
				/*
				 * Intent myIntent = new
				 * Intent(OffersListElderCareActivity.this, AdBanner.class);
				 * myIntent.putExtra("banner_value", "2");
				 * startActivity(myIntent);
				 */
				Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.homers.in"));
				startActivity(i);

			}
		});

		backBtnSub = (ImageView) findViewById(R.id.iv_back);
		backBtnSub.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(0, 0);
			}
		});

		findViewById(R.id.llMenu).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intObj = new Intent(OffersListElderCareActivity.this, MenuLay.class);
				startActivity(intObj);
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

				//finish();
			}
		});

		backBtn = (RelativeLayout) findViewById(R.id.back_settings);

		backBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				if (Util.isInternetAvailable(OffersListElderCareActivity.this)) {
					Intent i = new Intent(OffersListElderCareActivity.this, HumDetailsView.class);
					startActivity(i);
					overridePendingTransition(R.anim.slide_in_right, R.anim.slide_leave_right);
				} else {

					Intent i = new Intent(OffersListElderCareActivity.this, NoInternetPage.class);
					startActivity(i);
					overridePendingTransition(0, 0);

				}

			}
		});

		visitWebBtn = (RelativeLayout) findViewById(R.id.visit_btn);
		visitWebBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.ethnikyarn.com"));
				startActivity(i);
			}
		});

		visitWebBtn2 = (RelativeLayout) findViewById(R.id.visit_btn2);
		visitWebBtn2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.ethnikyarn.com"));
				startActivity(i);
			}
		});

	}
	public void onHomeClick(View v) {
		startActivity(new Intent(OffersListElderCareActivity.this, DashboardActivity.class));
		finish();
	}
}
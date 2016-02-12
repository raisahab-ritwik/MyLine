package com.seva60plus.hum.sharelocation;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.seva60plus.hum.R;
import com.seva60plus.hum.activity.DashboardActivity;
import com.seva60plus.hum.activity.HumDetailsView;
import com.seva60plus.hum.activity.MenuLay;
import com.seva60plus.hum.custom.NoInternetPage;
import com.seva60plus.hum.util.ConnectionDetector;

public class ShareLocationInstraction extends Activity {

	Button okBtn;
	TextView headerTitleText;
	ImageView homeBtn;
	RelativeLayout backBtn;

	GoogleMap mMap;
	Marker mMarker;
	LocationManager lm;
	double lat, lng;

	//---by Dibyendu
	// flag for Internet connection status
	Boolean isInternetPresent = false;

	// Connection detector class
	ConnectionDetector cd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.share_location_instruction);

		cd = new ConnectionDetector(getApplicationContext());// by Dibyendu

		headerTitleText = (TextView) findViewById(R.id.header_title);
		headerTitleText.setText("SHARE LOCATION");
		Typeface font = Typeface.createFromAsset(getAssets(), "openSansBold.ttf");
		headerTitleText.setTypeface(font);

		backBtn = (RelativeLayout) findViewById(R.id.back_settings);

		backBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				if (isInternetPresent) {
					Intent i = new Intent(ShareLocationInstraction.this, HumDetailsView.class);
					startActivity(i);
					overridePendingTransition(0, 0);
					finish();
				} else {
					Intent i = new Intent(ShareLocationInstraction.this, NoInternetPage.class);
					startActivity(i);
					overridePendingTransition(0, 0);

				}

			}
		});

		findViewById(R.id.llMenu).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// Take action.
				Intent myIntent = new Intent(ShareLocationInstraction.this, MenuLay.class);
				startActivity(myIntent);
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

			}
		});

		okBtn = (Button) findViewById(R.id.okPressBtn);
		okBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// Take action.

				Intent i = new Intent(ShareLocationInstraction.this, SmsLocation.class);
				startActivity(i);
				overridePendingTransition(0, 0);

				//finish();
			}
		});

	}
	public void onHomeClick(View v) {
		startActivity(new Intent(ShareLocationInstraction.this, DashboardActivity.class));
		finish();
	}
}

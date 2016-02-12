package com.seva60plus.hum.activity;

import android.app.Activity;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.seva60plus.hum.R;
import com.seva60plus.hum.custom.NoInternetPage;
import com.seva60plus.hum.mediacentre.MediaCentreDashBoard;
import com.seva60plus.hum.nearby.NearByDashboard;
import com.seva60plus.hum.pillreminder.TakeThePill;
import com.seva60plus.hum.reminder.taskreminder.ReminderListActivity;
import com.seva60plus.hum.sathi.SaathiActivity;
import com.seva60plus.hum.sharelocation.MapShareActivity;
import com.seva60plus.hum.specialoffers.OffersListActivity;
import com.seva60plus.hum.util.ConnectionDetector;
import com.seva60plus.hum.utilities.Utilities;
import com.seva60plus.hum.wellbeing.WellBeingActivityExercise;

public class MenuLay extends Activity {
	
	ListView listView;
	
	Button close;

	RelativeLayout option1, option2, option3, option4, option5, option6, option7, option8, option9;

	public static final String MY_PREFS_NAME = "MyPrefsFile";

	LinearLayout contentClose;

	ImageView sevaImage;

	Boolean isInternetPresent = false;

	ConnectionDetector cd;
	
	LocationManager lm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_lay_new);

		cd = new ConnectionDetector(getApplicationContext());// by Dibyendu
		lm = (LocationManager) getSystemService(LOCATION_SERVICE);// by Dibyendu

		sevaImage = (ImageView) findViewById(R.id.imageView10);
		sevaImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				Intent i = new Intent(MenuLay.this, DashboardActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
				overridePendingTransition(0, 0);
				finish();

			}
		});

		option1 = (RelativeLayout) findViewById(R.id.rl_hum_training);
		option1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				Intent i = new Intent(MenuLay.this, TakeThePill.class);
				startActivity(i);
				overridePendingTransition(0, 0);
				finish();
			}
		});

		option2 = (RelativeLayout) findViewById(R.id.rl_doctor_speaks);
		option2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				Intent i = new Intent(MenuLay.this, SaathiActivity.class);
				startActivity(i);
				overridePendingTransition(0, 0);
				finish();
			}
		});
		option3 = (RelativeLayout) findViewById(R.id.rl_financial_advisory);
		option3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				Intent i = new Intent(MenuLay.this, WellBeingActivityExercise.class);
				startActivity(i);
				overridePendingTransition(0, 0);
				finish();
			}
		});
		option4 = (RelativeLayout) findViewById(R.id.rl_fitness);
		option4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				Intent i = new Intent(MenuLay.this, MediaCentreDashBoard.class);
				startActivity(i);
				overridePendingTransition(0, 0);
				finish();
			}
		});
		option5 = (RelativeLayout) findViewById(R.id.rl_technology);
		option5.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				isInternetPresent = cd.isConnectingToInternet();

				// check for Internet status
				if (isInternetPresent) {
					// Internet Connection is Present

					if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

						Intent i = new Intent(MenuLay.this, NearByDashboard.class);
						startActivity(i);
						overridePendingTransition(0, 0);
						finish();

					} else {
						final Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						startActivity(intent);
						overridePendingTransition(0, 0);
					}

				}

				else {
					// Internet connection is not present
					Intent i = new Intent(MenuLay.this, NoInternetPage.class);
					startActivity(i);

				}

			}
		});
		option6 = (RelativeLayout) findViewById(R.id.rl_art_and_culture);
		option6.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				isInternetPresent = cd.isConnectingToInternet();

				// check for Internet status
				if (isInternetPresent) {
					// Internet Connection is Present

					if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
						// GPS Connection is Present

						Intent i = new Intent(MenuLay.this, MapShareActivity.class);
						startActivity(i);
						overridePendingTransition(0, 0);
						finish();

					} else {
						final Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						startActivity(intent);
						overridePendingTransition(0, 0);
					}

				} else {
					// Internet connection is not present
					Intent i = new Intent(MenuLay.this, NoInternetPage.class);
					startActivity(i);
					overridePendingTransition(0, 0);

				}

			}
		});
		option7 = (RelativeLayout) findViewById(R.id.rl_food);
		option7.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				Intent i = new Intent(MenuLay.this, Utilities.class);
				startActivity(i);
				overridePendingTransition(0, 0);
				finish();
			}
		});
		option8 = (RelativeLayout) findViewById(R.id.rl_travel);
		option8.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				Intent i = new Intent(MenuLay.this, ReminderListActivity.class);
				startActivity(i);
				overridePendingTransition(0, 0);

				finish();
			}
		});
		option9 = (RelativeLayout) findViewById(R.id.option9);
		option9.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				//---Offer
				Intent i = new Intent(MenuLay.this, OffersListActivity.class);
				startActivity(i);
				overridePendingTransition(0, 0);

				finish();
			}
		});

		close = (Button) findViewById(R.id.closemenu);
		close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				finish();
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

			}
		});

		contentClose = (LinearLayout) findViewById(R.id.ll_content_close);
		contentClose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				//Intent intObj = new Intent(getApplicationContext(),Dashboard.class);
				//startActivity(intObj);
				finish();
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);//R.anim.slide_in_right,R.anim.slide_out_left

			}
		});

	}

	@Override
	public void onBackPressed() {

		finish();
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

	}

}
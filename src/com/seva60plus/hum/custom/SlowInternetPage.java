package com.seva60plus.hum.custom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.seva60plus.hum.R;
import com.seva60plus.hum.activity.DashboardActivity;
import com.seva60plus.hum.nearby.NearByDashboard;
import com.seva60plus.hum.util.ConnectionDetector;

public class SlowInternetPage extends Activity {

	//---by Dibyendu
	// flag for Internet connection status
	Boolean isInternetPresent = false;

	// Connection detector class
	ConnectionDetector cd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.slow_internet_page);
		overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);

		cd = new ConnectionDetector(getApplicationContext());// by Dibyendu

		Button checkBtn = (Button) findViewById(R.id.check_internet);
		checkBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				

				isInternetPresent = cd.isConnectingToInternet();

				// check for Internet status
				if (isInternetPresent) {

					Intent intObj = new Intent(SlowInternetPage.this, NearByDashboard.class);
					//intObj.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intObj);
					overridePendingTransition(0, 0);
					finish();

				} else {

				}

			}
		});

		Button checkBtnCancel = (Button) findViewById(R.id.check_internet_cancel);
		checkBtnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intObj = new Intent(SlowInternetPage.this, DashboardActivity.class);
				//intObj.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intObj);
				overridePendingTransition(0, 0);
				finish();
				overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
			}
		});

	}
}

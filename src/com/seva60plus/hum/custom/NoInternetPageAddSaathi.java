package com.seva60plus.hum.custom;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.seva60plus.hum.R;
import com.seva60plus.hum.util.ConnectionDetector;

public class NoInternetPageAddSaathi extends Activity {

	private Boolean isInternetPresent = false;

	private ConnectionDetector cd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.no_internet_page_add_saathi);
		overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);

		cd = new ConnectionDetector(getApplicationContext());

		Button checkBtn = (Button) findViewById(R.id.check_internet);
		checkBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				isInternetPresent = cd.isConnectingToInternet();

				if (isInternetPresent) {
					finish();
					overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);

				} else {

				}

			}
		});

		Button checkBtnCancel = (Button) findViewById(R.id.check_internet_cancel);
		checkBtnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
				overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
			}
		});

	}
}

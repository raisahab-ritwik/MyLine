package com.seva60plus.hum.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.seva60plus.hum.R;

public class HelpRegistrationPage extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help_registration_page);

	}

	public void onBackButtonClicked(View v) {
		onBackPressed();
	}
}

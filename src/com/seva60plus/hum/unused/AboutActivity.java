package com.seva60plus.hum.unused;

import android.app.Activity;
import android.os.Bundle;

import com.seva60plus.hum.R;

public class AboutActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		setTitle(R.string.about_title);
	}
	
}

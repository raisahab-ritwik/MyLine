package com.seva60plus.hum.custom;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.seva60plus.hum.R;
import com.seva60plus.hum.activity.BaseActivity;

public class SlowConnectionPage extends BaseActivity {

	TextView text_internet;
	Button check_internet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.slow_connection_page);
		text_internet = (TextView) findViewById(R.id.text_internet);
		check_internet = (Button) findViewById(R.id.check_internet);
		Typeface type = Typeface.createFromAsset(getAssets(), "openSansRegular.ttf");
		text_internet.setTypeface(type);
		check_internet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

}

package com.seva60plus.hum.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.seva60plus.hum.R;

public class Terms extends Activity {

	Button close;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.terms);

		WebView termWeb = (WebView) findViewById(R.id.term_web);
		termWeb.loadUrl("file:///android_asset/Hum_Terms_of_Service.html");
		close = (Button) findViewById(R.id.close);
		// close.setTypeface(font);
		close.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// Take action.
				finish();

			}
		});

	}
}
package com.seva60plus.hum.unused;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.seva60plus.hum.R;
import com.seva60plus.hum.activity.DashboardActivity;
import com.seva60plus.hum.activity.HumDetailsView;
import com.seva60plus.hum.activity.MenuLay;
import com.seva60plus.hum.custom.NoInternetPage;
import com.seva60plus.hum.util.ConnectionDetector;
import com.seva60plus.hum.util.Util;

public class AdBanner extends Activity {

	private WebView webView;
	ImageView menuIcon;
	RelativeLayout backBtn;
	String value = "";

	//---by Dibyendu
	// flag for Internet connection status
	Boolean isInternetPresent = false;

	// Connection detector class
	ConnectionDetector cd;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ad_banner);

		cd = new ConnectionDetector(getApplicationContext());// by Dibyendu

		Intent i = getIntent();
		value = i.getStringExtra("banner_value");

		findViewById(R.id.llMenu).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intObj = new Intent(AdBanner.this, MenuLay.class);
				startActivity(intObj);
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

				//finish();
			}
		});

		backBtn = (RelativeLayout) findViewById(R.id.back_settings);

		backBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (Util.isInternetAvailable(AdBanner.this)) {
					Intent i = new Intent(AdBanner.this, HumDetailsView.class);
					startActivity(i);
					overridePendingTransition(R.anim.slide_in_right, R.anim.slide_leave_right);
				} else {
					Intent i = new Intent(AdBanner.this, NoInternetPage.class);
					startActivity(i);
					overridePendingTransition(0, 0);
				}
			}
		});

		final ProgressDialog progressDialog = new ProgressDialog(AdBanner.this);
		WebView webview = (WebView) findViewById(R.id.webView1);

		webview.getSettings().setDomStorageEnabled(true);
		webview.getSettings().setJavaScriptEnabled(true);
		webview.setVerticalScrollBarEnabled(false);
		webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		webview.getSettings().setPluginState(WebSettings.PluginState.ON);
		webview.getSettings().setSupportMultipleWindows(true);
		webview.getSettings().setSupportZoom(true);
		webview.setVerticalScrollBarEnabled(false);
		webview.setHorizontalScrollBarEnabled(false);

		progressDialog.show();

		if (value.equals("1")) {
			webview.loadUrl("http://www.ethnikyarn.com");
		} else if (value.equals("2")) {
			webview.loadUrl("http://www.homers.in");
		} else {
			webview.loadUrl("http://www.homers.in");
		}

		//

		webview.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return false;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				progressDialog.dismiss();
			}

		});

	}
	public void onHomeClick(View v) {
		startActivity(new Intent(AdBanner.this, DashboardActivity.class));
		finish();
	}
}
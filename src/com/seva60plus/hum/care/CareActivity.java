package com.seva60plus.hum.care;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.seva60plus.hum.R;
import com.seva60plus.hum.activity.DashboardActivity;
import com.seva60plus.hum.activity.HumDetailsView;
import com.seva60plus.hum.activity.MenuLay;
import com.seva60plus.hum.custom.NoInternetPage;
import com.seva60plus.hum.util.SocialNetworking;
import com.seva60plus.hum.util.Util;

public class CareActivity extends Activity implements OnClickListener {
	private Context mContext;
	private ImageView backBtn, menuIcon;
	private RelativeLayout backSetup;
	private Button btn_whatsapp, btn_fb, btn_twitter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_care_main);
		mContext = CareActivity.this;
		btn_whatsapp = (Button) findViewById(R.id.whatsapp_btn);
		btn_fb = (Button) findViewById(R.id.facebook_btn);
		btn_twitter = (Button) findViewById(R.id.twitter_btn);
		backBtn = (ImageView) findViewById(R.id.iv_back);

		backBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// Take action.

				finish();

			}
		});
		backSetup = (RelativeLayout) findViewById(R.id.back_settings);

		backSetup.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (Util.isInternetAvailable(mContext)) {
					Intent i = new Intent(mContext, HumDetailsView.class);
					startActivity(i);
					overridePendingTransition(R.anim.slide_in_right, R.anim.slide_leave_right);
				} else {
					Intent i = new Intent(mContext, NoInternetPage.class);
					startActivity(i);
					overridePendingTransition(0, 0);
				}
			}
		});
		findViewById(R.id.llMenu).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intObj = new Intent(mContext, MenuLay.class);
				startActivity(intObj);
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

			}
		});
		btn_whatsapp.setOnClickListener(this);
		btn_fb.setOnClickListener(this);
		btn_twitter.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.whatsapp_btn:
			SocialNetworking.shareAppLinkViaWhatsApp(mContext);
			break;

		case R.id.facebook_btn:
			SocialNetworking.shareAppLinkViaFacebook(mContext);
			break;

		case R.id.twitter_btn:
			SocialNetworking.shareAppLinkViaTwitter(mContext);
			break;

		case R.id.iv_back:
			finish();
			break;
		}
	}
	public void onCallOrderClick(View v) {

		Intent my_callIntent = new Intent(Intent.ACTION_CALL);
		my_callIntent.setData(Uri.parse("tel:" + "08230078529"));
		startActivity(my_callIntent);
	}

	public void onHomeClick(View v) {
		startActivity(new Intent(mContext, DashboardActivity.class));
		finish();
	}
}

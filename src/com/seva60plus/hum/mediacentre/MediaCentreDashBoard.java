package com.seva60plus.hum.mediacentre;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.seva60plus.hum.R;
import com.seva60plus.hum.activity.DashboardActivity;
import com.seva60plus.hum.activity.HumDetailsView;
import com.seva60plus.hum.activity.MenuLay;
import com.seva60plus.hum.custom.NoInternetPage;
import com.seva60plus.hum.util.SocialNetworking;
import com.seva60plus.hum.util.Util;

public class MediaCentreDashBoard extends Activity implements OnClickListener {

	private RelativeLayout rl_hum_training, rl_doctor_speaks, rl_financial_advisory, rl_fitness, rl_technology, rl_art_and_culture, rl_food;
	private RelativeLayout rl_travel;
	private RelativeLayout backBtn;
	private ImageView backBtnSub;
	private Button whatsapp, fb, twitter;
	private Context mContext;
	private LinearLayout llMenu;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.media_centre_dashboard);
		mContext = MediaCentreDashBoard.this;

		initView();

	}

	private void initView() {

		rl_hum_training = (RelativeLayout) findViewById(R.id.rl_hum_training);

		rl_doctor_speaks = (RelativeLayout) findViewById(R.id.rl_doctor_speaks);

		rl_financial_advisory = (RelativeLayout) findViewById(R.id.rl_financial_advisory);

		rl_fitness = (RelativeLayout) findViewById(R.id.rl_fitness);

		rl_technology = (RelativeLayout) findViewById(R.id.rl_technology);

		rl_art_and_culture = (RelativeLayout) findViewById(R.id.rl_art_and_culture);

		rl_food = (RelativeLayout) findViewById(R.id.rl_food);

		rl_travel = (RelativeLayout) findViewById(R.id.rl_travel);

		whatsapp = (Button) findViewById(R.id.whatsapp_btn);

		fb = (Button) findViewById(R.id.facebook_btn);

		twitter = (Button) findViewById(R.id.twitter_btn);

		backBtn = (RelativeLayout) findViewById(R.id.back_settings);

		backBtnSub = (ImageView) findViewById(R.id.iv_back);

		llMenu = (LinearLayout) findViewById(R.id.llMenu);

		rl_hum_training.setOnClickListener(this);

		rl_doctor_speaks.setOnClickListener(this);

		rl_financial_advisory.setOnClickListener(this);

		rl_fitness.setOnClickListener(this);

		rl_technology.setOnClickListener(this);

		rl_art_and_culture.setOnClickListener(this);

		rl_food.setOnClickListener(this);

		rl_travel.setOnClickListener(this);

		whatsapp.setOnClickListener(this);

		fb.setOnClickListener(this);

		twitter.setOnClickListener(this);

		backBtn.setOnClickListener(this);

		backBtnSub.setOnClickListener(this);

		llMenu.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_hum_training:
			Intent intent_hum_training = new Intent(MediaCentreDashBoard.this, MediaCentreHumTraining.class);
			startActivity(intent_hum_training);
			break;
		case R.id.rl_doctor_speaks:
			Intent intent_doctor_speaks = new Intent(MediaCentreDashBoard.this, MediaCentreDocSpeaks.class);
			startActivity(intent_doctor_speaks);
			break;
		case R.id.rl_financial_advisory:
			Intent intent_fin_advisory = new Intent(MediaCentreDashBoard.this, MediaCentreFinAdvisory.class);
			startActivity(intent_fin_advisory);
			break;
		case R.id.rl_fitness:
			Intent intent_fitness = new Intent(MediaCentreDashBoard.this, MediaCentreFitness.class);
			startActivity(intent_fitness);
			break;
		case R.id.rl_technology:
			Intent intent_technology = new Intent(MediaCentreDashBoard.this, MediaCentreTechnology.class);
			startActivity(intent_technology);
			break;
		case R.id.rl_art_and_culture:
			Intent intent_art_and_culture = new Intent(MediaCentreDashBoard.this, MediaCentreArtNCulture.class);
			startActivity(intent_art_and_culture);
			break;
		case R.id.rl_food:
			Intent intent_food = new Intent(MediaCentreDashBoard.this, MediaCentreFood.class);
			startActivity(intent_food);
			break;
		case R.id.rl_travel:
			Intent intent_travel = new Intent(MediaCentreDashBoard.this, MediaCentreTravel.class);
			startActivity(intent_travel);
			break;
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
			overridePendingTransition(0, 0);
			break;
		case R.id.llMenu:
			Intent intent_menu = new Intent(MediaCentreDashBoard.this, MenuLay.class);
			startActivity(intent_menu);
			overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			break;
		case R.id.back_settings:
			if (Util.isInternetAvailable(mContext)) {
				Intent intent_back = new Intent(MediaCentreDashBoard.this, HumDetailsView.class);
				startActivity(intent_back);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_leave_right);
			} else {
				Intent i = new Intent(mContext, NoInternetPage.class);
				startActivity(i);
				overridePendingTransition(0, 0);
			}
			break;
		default:
			break;
		}

	}
	public void onHomeClick(View v) {
		startActivity(new Intent(mContext, DashboardActivity.class));
		finish();
	}
}
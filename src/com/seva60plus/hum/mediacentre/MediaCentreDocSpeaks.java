package com.seva60plus.hum.mediacentre;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.seva60plus.hum.R;
import com.seva60plus.hum.activity.BaseActivity;
import com.seva60plus.hum.activity.DashboardActivity;
import com.seva60plus.hum.activity.HumDetailsView;
import com.seva60plus.hum.activity.MenuLay;
import com.seva60plus.hum.custom.NoInternetPage;
import com.seva60plus.hum.model.Video;
import com.seva60plus.hum.util.SocialNetworking;
import com.seva60plus.hum.util.Util;

public class MediaCentreDocSpeaks extends BaseActivity implements OnClickListener, YoutubePlayListListener {

	private ImageView iv_back;
	private TextView tv_fin_advisory;
	private ListView listView_youtubePlaylist;
	private Button whatsapp, fb, twitter;
	private RelativeLayout backSetup;
	private Context mContext;
	private LinearLayout llMenu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.media_centre_fin_advisory);
		mContext = MediaCentreDocSpeaks.this;
		initView();

	}

	private void initView() {

		tv_fin_advisory = (TextView) findViewById(R.id.tv_fin_advisory);
		backSetup = (RelativeLayout) findViewById(R.id.back_settings);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		llMenu = (LinearLayout) findViewById(R.id.llMenu);
		listView_youtubePlaylist = (ListView) findViewById(R.id.listView_youtubePlaylist);

		whatsapp = (Button) findViewById(R.id.whatsapp_btn);
		fb = (Button) findViewById(R.id.facebook_btn);
		twitter = (Button) findViewById(R.id.twitter_btn);

		iv_back.setOnClickListener(this);
		backSetup.setOnClickListener(this);
		llMenu.setOnClickListener(this);
		whatsapp.setOnClickListener(this);
		fb.setOnClickListener(this);
		twitter.setOnClickListener(this);

		tv_fin_advisory.setText("DOCTOR SPEAKS");

		fetchYoutubePlaylist();
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.iv_back:
			finish();
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
		case R.id.back_settings:
			if (Util.isInternetAvailable(mContext)) {
				Intent intentRegistration = new Intent(mContext, HumDetailsView.class);
				startActivity(intentRegistration);
				overridePendingTransition(0, 0);
				finish();
			} else {
				Intent i = new Intent(mContext, NoInternetPage.class);
				startActivity(i);
				overridePendingTransition(0, 0);
			}
			break;
		case R.id.llMenu:
			Intent intentMenu = new Intent(mContext, MenuLay.class);
			startActivity(intentMenu);
			overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			finish();

			break;
		default:
			break;
		}
	}
	public void onHomeClick(View v) {
		startActivity(new Intent(mContext, DashboardActivity.class));
		finish();
	}
	private void fetchYoutubePlaylist() {

		if (Util.isInternetAvailable(this)) {

			YoutubePlayListAsync mAsyncTask = new YoutubePlayListAsync(mContext, "PLxkAcus7ChPGF_SlE1LW-7JW9tg4SpqGO");
			mAsyncTask.mListener = this;
			mAsyncTask.execute();
		} else {

			Intent i = new Intent(this, NoInternetPage.class);
			startActivity(i);
			overridePendingTransition(0, 0);
		}
	}

	@Override
	public void videoPlaylistAsyncCallback(final ArrayList<Video> result) {
		if (result.size() > 0) {
			YoutubeVideoAdapter mAdapter = new YoutubeVideoAdapter(mContext, result);

			listView_youtubePlaylist.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					Intent i = new Intent(mContext, PlayVideoActivity.class);
					i.putExtra("videocode", result.get(arg2).getUrl());
					i.putExtra("videoDescription", result.get(arg2).getVideoDescription());
					startActivity(i);

				}
			});

			listView_youtubePlaylist.setAdapter(mAdapter);
		}
	}

}

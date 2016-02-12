package com.seva60plus.hum.mediacentre;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
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

public class MediaCentreTravel extends BaseActivity implements OnClickListener, YoutubePlayListListener {

	private ImageView iv_back;
	private ListView listView_youtubePlaylist;
	//	private WebView wv_travel_info;
	private Button whatsapp, fb, twitter;
	private RelativeLayout backSetup;
	private Context mContext;
	private TextView tv_destination_url;
	private LinearLayout llMenu;
	final Animation in = new AlphaAnimation(0.0f, 1.0f);
	final Animation out = new AlphaAnimation(1.0f, 0.0f);

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.media_centre_travel);
		mContext = MediaCentreTravel.this;

		initView();

	}

	private void initView() {

		backSetup = (RelativeLayout) findViewById(R.id.back_settings);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		llMenu = (LinearLayout) findViewById(R.id.llMenu);
		listView_youtubePlaylist = (ListView) findViewById(R.id.listView_youtubePlaylist);
		//wv_travel_info=(WebView) findViewById(R.id.wv_travel_info);
		tv_destination_url = (TextView) findViewById(R.id.tv_destination_url);

		whatsapp = (Button) findViewById(R.id.whatsapp_btn);
		fb = (Button) findViewById(R.id.facebook_btn);
		twitter = (Button) findViewById(R.id.twitter_btn);

		iv_back.setOnClickListener(this);
		backSetup.setOnClickListener(this);
		llMenu.setOnClickListener(this);
		whatsapp.setOnClickListener(this);
		fb.setOnClickListener(this);
		twitter.setOnClickListener(this);

		fetchYoutubePlaylist();
		//wv_travel_info.loadUrl("http://www.seniorcitizenjournal.com/travel-articles/moab-a-destination-for-all-ages/");
		in.setDuration(750);
		out.setDuration(750);

		tv_destination_url.startAnimation(in);

		in.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationEnd(Animation animation) {
				tv_destination_url.startAnimation(out);

			}

			@Override
			public void onAnimationStart(Animation animation) {
				// Do nothing

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// Do nothing

			}
		});

		out.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationEnd(Animation animation) {
				tv_destination_url.startAnimation(in);

			}

			@Override
			public void onAnimationStart(Animation animation) {
				// Do nothing

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// Do nothing

			}
		});

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
	public void onDestinationUrlClick(View v) {
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.seniorcitizenjournal.com/travel-articles/moab-a-destination-for-all-ages/"));
		startActivity(intent);
	}

	private void fetchYoutubePlaylist() {

		if (Util.isInternetAvailable(this)) {

			YoutubePlayListAsync mAsyncTask = new YoutubePlayListAsync(mContext, "PLxkAcus7ChPHpmFOTiWjXot9c-vR4TGCw");
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

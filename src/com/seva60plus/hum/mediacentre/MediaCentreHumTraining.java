package com.seva60plus.hum.mediacentre;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.seva60plus.hum.R;
import com.seva60plus.hum.activity.DashboardActivity;
import com.seva60plus.hum.activity.HumDetailsView;
import com.seva60plus.hum.activity.MenuLay;
import com.seva60plus.hum.custom.NoInternetPage;
import com.seva60plus.hum.model.Video;
import com.seva60plus.hum.util.Util;

public class MediaCentreHumTraining extends Activity implements YoutubePlayListListener {

	private ImageView backBtn;
	private RelativeLayout backSetup;
	private Button whatsapp, fb, twitter;

	private Dialog progress;
	private AnimationDrawable progressAnimation;

	private ListView listView_youtubePlaylist;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.media_hum_traning);

		//------START------------For Progress Spinner--------------

		progress = new Dialog(MediaCentreHumTraining.this);
		progress.getWindow().setBackgroundDrawableResource(R.drawable.spinner_dialog_backround);

		//Remove the Title
		progress.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

		//progress.setTitle("");

		//Set the View of the Dialog - Custom
		progress.setContentView(R.layout.custom_progress_dialog);

		//Set the title of the Dialog
		//dialog.setTitle("Title...");

		ImageView progressSpinner = (ImageView) progress.findViewById(R.id.progressSpinner);

		//Set the background of the image - In this case an animation (/res/anim folder)
		progressSpinner.setBackgroundResource(R.anim.spinner_progress_animation);

		//Get the image background and attach the AnimationDrawable to it.
		progressAnimation = (AnimationDrawable) progressSpinner.getBackground();

		//Start the animation after the dialog is displayed.
		progress.setOnShowListener(new OnShowListener() {
			@Override
			public void onShow(DialogInterface dialog) {
				progressAnimation.start();
			}

		});

		progress.setCanceledOnTouchOutside(false);
		//-------END-----------For Progress Spinner--------------  
		listView_youtubePlaylist = (ListView) findViewById(R.id.listView_youtubePlaylist);

		whatsapp = (Button) findViewById(R.id.whatsapp_btn);
		fb = (Button) findViewById(R.id.facebook_btn);
		twitter = (Button) findViewById(R.id.twitter_btn);

		whatsapp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				shareAppLinkViaWhatsApp();
			}
		});

		fb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				shareAppLinkViaFacebook();
			}
		});

		twitter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				shareAppLinkViaTwitter();
			}
		});

		backBtn = (ImageView) findViewById(R.id.iv_back);

		backBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();

			}
		});

		backSetup = (RelativeLayout) findViewById(R.id.back_settings);

		backSetup.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (Util.isInternetAvailable(MediaCentreHumTraining.this)) {
					Intent i = new Intent(MediaCentreHumTraining.this, HumDetailsView.class);
					startActivity(i);
					overridePendingTransition(0, 0);
					finish();
				} else {
					Intent i = new Intent(MediaCentreHumTraining.this, NoInternetPage.class);
					startActivity(i);
					overridePendingTransition(0, 0);
				}
			}
		});

		findViewById(R.id.llMenu).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// Take action.
				Intent i = new Intent(MediaCentreHumTraining.this, MenuLay.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
				finish();

			}
		});

		if (Util.isInternetAvailable(this))
			showYoutubeVideo();
		else {
			Intent i = new Intent(this, NoInternetPage.class);
			startActivity(i);
			overridePendingTransition(0, 0);
		}
	}
	public void onHomeClick(View v) {
		startActivity(new Intent(MediaCentreHumTraining.this, DashboardActivity.class));
		finish();
	}
	private void shareAppLinkViaFacebook() {
		Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/Seva60Plus"));
		startActivity(i);

	}

	private void shareAppLinkViaTwitter() {
		String urlToShare = "Please spread the word : Seva60Plus HUM Download Link : https://play.google.com/store/apps/details?id=com.seva60plus.hum";

		try {
			Intent intent1 = new Intent();
			intent1.setClassName("com.twitter.android", "com.twitter.android.PostActivity");
			intent1.setAction("android.intent.action.SEND");
			intent1.setType("text/plain");
			intent1.putExtra("android.intent.extra.TEXT", urlToShare);
			startActivity(intent1);
		} catch (Exception e) {
			// If we failed (not native FB app installed), try share through SEND
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("text/plain");
			String sharerUrl = "https://twitter.com/intent/tweet?text=Please spread the word : Seva60Plus HUM";
			//String sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=http://wrctechnologies.com/";
			intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
			startActivity(intent);
		}

	}

	private void shareAppLinkViaWhatsApp() {
		Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
		whatsappIntent.setType("text/plain");
		whatsappIntent.setPackage("com.whatsapp");
		whatsappIntent.putExtra(Intent.EXTRA_TEXT,
				"Please spread the word : Seva60Plus HUM Download Link : https://play.google.com/store/apps/details?id=com.seva60plus.hum");
		try {
			startActivity(whatsappIntent);
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(getApplicationContext(), "Whatsapp have not been installed.", Toast.LENGTH_SHORT).show();
			//ToastHelper.MakeShortText("Whatsapp have not been installed.");
		}
	}

	private void showYoutubeVideo() {

		YoutubePlayListAsync playlistAsync = new YoutubePlayListAsync(MediaCentreHumTraining.this, "PLxkAcus7ChPGTZ37ERZ1Rz2eTuDpRJyQf");
		playlistAsync.mListener = MediaCentreHumTraining.this;
		playlistAsync.execute();
	}

	@Override
	public void videoPlaylistAsyncCallback(final ArrayList<Video> result) {
		if (result.size() > 0) {
			YoutubeVideoAdapter mAdapter = new YoutubeVideoAdapter(MediaCentreHumTraining.this, result);

			listView_youtubePlaylist.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					Intent i = new Intent(MediaCentreHumTraining.this, PlayVideoActivity.class);
					i.putExtra("videocode", result.get(arg2).getUrl());
					i.putExtra("videoDescription", result.get(arg2).getVideoDescription());
					startActivity(i);

				}
			});

			listView_youtubePlaylist.setAdapter(mAdapter);
		}
	}
}

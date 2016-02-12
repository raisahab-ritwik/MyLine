package com.seva60plus.hum.wellbeing;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.seva60plus.hum.R;
import com.seva60plus.hum.pillreminder.PillsDbAdapter;

public class WellbeingWaReminderAlert extends Activity {

	Long rowId;
	private PillsDbAdapter mDbHelper;
	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd kk:mm:ss";
	MediaPlayer mMood;
	TextView mTitleText;
	String mainTime = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wellbeing_wa_reminder_alert);

		mTitleText = (TextView) findViewById(R.id.textView2);
		mTitleText.setText("HOW MANY HOUR DID YOU SLEEP?");

		mMood = MediaPlayer.create(this, R.raw.good_morning);

		mMood.setLooping(true);
		mMood.start();

		Button okBtn = (Button) findViewById(R.id.ok_btn);
		okBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mMood != null && mMood.isPlaying())
					mMood.stop();
				Intent objIntent = new Intent(getApplicationContext(), WellBeingActivitySleep.class);
				startActivity(objIntent);
				finish();
			}
		});

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				if (mMood != null && mMood.isPlaying()) {
					mMood.stop();
				}

			}
		}, 60000);

	}

}

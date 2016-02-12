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

public class WellbeingExReminderAlert extends Activity {

	Long rowId;
	MediaPlayer mEx;
	private PillsDbAdapter mDbHelper;
	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd kk:mm:ss";

	TextView mTitleText;
	String mainTime = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wellbeing_ex_reminder_alert);

		mEx = MediaPlayer.create(this, R.raw.good_morning);

		mEx.setLooping(true);
		mEx.start();

		mTitleText = (TextView) findViewById(R.id.textView2);
		mTitleText.setText("DID YOU EXERCISE TODAY ?");

		Button okBtn = (Button) findViewById(R.id.ok_btn);
		okBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (mEx != null && mEx.isPlaying())
					mEx.stop();
				Intent objIntent = new Intent(getApplicationContext(), WellBeingActivityExercise.class);
				startActivity(objIntent);
				finish();
			}
		});
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				if (mEx != null && mEx.isPlaying()) {
					mEx.stop();
				}

			}
		}, 60000);

	}

}

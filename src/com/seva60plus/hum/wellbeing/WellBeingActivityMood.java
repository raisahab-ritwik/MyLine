package com.seva60plus.hum.wellbeing;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.seva60plus.hum.R;
import com.seva60plus.hum.activity.DashboardActivity;
import com.seva60plus.hum.activity.HumDetailsView;
import com.seva60plus.hum.activity.MenuLay;
import com.seva60plus.hum.custom.NoInternetPage;
import com.seva60plus.hum.model.WellBeing;
import com.seva60plus.hum.staticconstants.ConstantVO;
import com.seva60plus.hum.util.Contact;
import com.seva60plus.hum.util.DatabaseHandler;
import com.seva60plus.hum.util.SocialNetworking;
import com.seva60plus.hum.util.Util;
import com.seva60plus.hum.wellbeing.sync.WellBeingDB;

public class WellBeingActivityMood extends Activity implements OnClickListener {

	private String intent_response = null;
	private ImageView backBtn;
	private RelativeLayout backSetup;
	private Button whatsapp, fb, twitter;

	private Button tab_task, tab_sta;
	private RelativeLayout tab_water, tab_execise, tab_mood;
	private RelativeLayout bad_btn, okay_btn, good_btn;

	private Button tab_water_btn, tab_execise_btn, tab_mood_btn;
	private Button bad_btn_txt, okay_btn_txt, good_btn_txt;

	
	private LinearLayout llMenu;
	private DatabaseHandler db;
	private Calendar c;
	private String setTime = "00:00:01";
	private String dDay, dMonth, dYear;
	private Context mContext;
	private static boolean isAnswered = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welbing_activity_mood);
		mContext = WellBeingActivityMood.this;

		db = new DatabaseHandler(this);
		c = Calendar.getInstance();
		initView();

	}

	private void initView() {

		whatsapp = (Button) findViewById(R.id.whatsapp_btn);
		fb = (Button) findViewById(R.id.facebook_btn);
		twitter = (Button) findViewById(R.id.twitter_btn);
		backBtn = (ImageView) findViewById(R.id.iv_back);
		backSetup = (RelativeLayout) findViewById(R.id.back_settings);
		llMenu = (LinearLayout) findViewById(R.id.llMenu);
		bad_btn = (RelativeLayout) findViewById(R.id.wel_btn);
		bad_btn_txt = (Button) findViewById(R.id.btn_wel_btn);
		okay_btn = (RelativeLayout) findViewById(R.id.wel_btn2);
		okay_btn_txt = (Button) findViewById(R.id.btn_wel_btn2);
		good_btn = (RelativeLayout) findViewById(R.id.wel_btn3);
		good_btn_txt = (Button) findViewById(R.id.btn_wel_btn3);
		tab_task = (Button) findViewById(R.id.task_btn);
		tab_sta = (Button) findViewById(R.id.static_btn);
		tab_water = (RelativeLayout) findViewById(R.id.wel_water_btn);
		tab_water_btn = (Button) findViewById(R.id.btn_water_btn);
		tab_execise = (RelativeLayout) findViewById(R.id.wel_ex_btn);
		tab_execise_btn = (Button) findViewById(R.id.btn_ex_btn);
		tab_mood = (RelativeLayout) findViewById(R.id.wel_mood_btn);
		tab_mood_btn = (Button) findViewById(R.id.btn_mood_btn);

		whatsapp.setOnClickListener(this);
		fb.setOnClickListener(this);
		twitter.setOnClickListener(this);
		//backBtn.setOnClickListener(this);
		backSetup.setOnClickListener(this);
		llMenu.setOnClickListener(this);
		bad_btn.setOnClickListener(this);
		bad_btn_txt.setOnClickListener(this);
		okay_btn.setOnClickListener(this);
		okay_btn_txt.setOnClickListener(this);
		good_btn.setOnClickListener(this);
		good_btn_txt.setOnClickListener(this);
		tab_task.setOnClickListener(this);
		tab_sta.setOnClickListener(this);
		tab_water.setOnClickListener(this);
		tab_water_btn.setOnClickListener(this);
		tab_execise.setOnClickListener(this);
		tab_execise_btn.setOnClickListener(this);
		tab_mood.setOnClickListener(this);
		tab_mood_btn.setOnClickListener(this);
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

		case R.id.back_settings:
			if (Util.isInternetAvailable(WellBeingActivityMood.this)) {
				Intent intentHumDetails = new Intent(WellBeingActivityMood.this, HumDetailsView.class);
				startActivity(intentHumDetails);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_leave_right);
			} else {
				Intent i = new Intent(WellBeingActivityMood.this, NoInternetPage.class);
				startActivity(i);
				overridePendingTransition(0, 0);
			}
			break;

		case R.id.llMenu:

			Intent intentMenu = new Intent(getApplicationContext(), MenuLay.class);
			startActivity(intentMenu);
			overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			break;

		case R.id.wel_btn:

			inserData("sad", "2", "mood");
			break;

		case R.id.btn_wel_btn:

			inserData("sad", "2", "mood");
			break;

		case R.id.wel_btn2:
			inserData("okay", "2", "mood");
			break;

		case R.id.btn_wel_btn2:
			inserData("okay", "2", "mood");
			break;

		case R.id.wel_btn3:
			inserData("good", "2", "mood");
			break;

		case R.id.btn_wel_btn3:

			inserData("good", "2", "mood");
			break;

		case R.id.task_btn:
			Intent intentMood = new Intent(WellBeingActivityMood.this, WellBeingActivityMood.class);
			startActivity(intentMood);
			overridePendingTransition(0, 0);
			finish();
			break;

		case R.id.static_btn:
			Intent intentMoodStat = new Intent(WellBeingActivityMood.this, WellBeingStatisticsMood.class);
			startActivity(intentMoodStat);
			overridePendingTransition(0, 0);
			finish();
			break;

		case R.id.wel_water_btn:
			Intent intentSleep = new Intent(WellBeingActivityMood.this, WellBeingActivitySleep.class);
			startActivity(intentSleep);
			overridePendingTransition(0, 0);
			finish();
			break;

		case R.id.btn_water_btn:
			Intent intentSleepActivity = new Intent(WellBeingActivityMood.this, WellBeingActivitySleep.class);
			startActivity(intentSleepActivity);
			overridePendingTransition(0, 0);
			finish();
			break;

		case R.id.wel_ex_btn:
			Intent intentExercise = new Intent(WellBeingActivityMood.this, WellBeingActivityExercise.class);
			startActivity(intentExercise);
			overridePendingTransition(0, 0);
			finish();
			break;

		case R.id.btn_ex_btn:
			Intent intentExerciseActivity = new Intent(WellBeingActivityMood.this, WellBeingActivityExercise.class);
			startActivity(intentExerciseActivity);
			overridePendingTransition(0, 0);
			finish();
			break;

		case R.id.wel_mood_btn:
			Intent intentMoodActivity = new Intent(WellBeingActivityMood.this, WellBeingActivityMood.class);
			startActivity(intentMoodActivity);
			overridePendingTransition(0, 0);
			finish();
			break;

		case R.id.btn_mood_btn:
			Intent intentMoodActivity2 = new Intent(WellBeingActivityMood.this, WellBeingActivityMood.class);
			startActivity(intentMoodActivity2);
			overridePendingTransition(0, 0);
			finish();
			break;

		default:
			break;
		}

	}

	public void inserData(String result, String status, String mode) {

		Date dt = new Date();
		int hours = dt.getHours();
		int minutes = dt.getMinutes();
		int seconds = dt.getSeconds();

		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DAY_OF_MONTH);

		int mlen = String.valueOf(month).length();
		int dlen = String.valueOf(day).length();

		if (mlen == 1) {
			dMonth = "0" + month;
		} else {
			dMonth = String.valueOf(month);
		}

		if (dlen == 1) {
			dDay = "0" + day;
		} else {
			dDay = String.valueOf(day);
		}

		String toDate1 = dDay + "." + dMonth + "." + year;

		String toDate = year + "-" + dMonth + "-" + dDay;

		String curTime = hours + ":" + minutes + ":" + seconds;

		db.addContact(new Contact(toDate, curTime, result, status, mode));
		Toast.makeText(getApplicationContext(), "Thank you", Toast.LENGTH_SHORT).show();

		//------ Insert data in sync adapter database.
		ContentValues cv = new ContentValues();
		cv.put(WellBeingDB.TIME_STAMP, Util.getCurrentDateTime());
		cv.put(WellBeingDB.TYPE, ConstantVO.MOOD);
		cv.put(WellBeingDB.VALUE, result);
		getContentResolver().insert(WellBeing.CONTENT_URI, cv);
		isAnswered = true;

	}

	@Override
	public void onBackPressed() {

		if (!isAnswered)
			inserData("n/a", "2", "mood");
		finish();
	}
	public void onHomeClick(View v) {
		startActivity(new Intent(mContext, DashboardActivity.class));
		finish();
	}
}
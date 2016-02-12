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
import com.seva60plus.hum.sharelocation.MapActivity;
import com.seva60plus.hum.staticconstants.ConstantVO;
import com.seva60plus.hum.util.Contact;
import com.seva60plus.hum.util.DatabaseHandler;
import com.seva60plus.hum.util.SocialNetworking;
import com.seva60plus.hum.util.Util;
import com.seva60plus.hum.wellbeing.sync.WellBeingDB;

public class WellBeingActivityExercise extends Activity implements OnClickListener {

	public static final String PREFS_NAME = "PrefsFile";
	public static final String ALARMS_KEY = "alarms_enabled";

	private ImageView backBtn;
	private RelativeLayout backSetup;

	private Button tab_task, tab_sta;

	private RelativeLayout tab_water, tab_execise, tab_mood;
	private RelativeLayout yes_btn, no_btn;
	
	private LinearLayout llMenu;

	private Button tab_water_btn, tab_execise_btn, tab_mood_btn;
	private Button yes_btn_text, no_btn_text;
	private Button whatsapp, fb, twitter;
	private DatabaseHandler db;
	private Calendar c;
	private String dDay, dMonth;

	private Context mContext;
	private static boolean isAnswered = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.welbing_activity_exercise);
		mContext = WellBeingActivityExercise.this;
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
		yes_btn = (RelativeLayout) findViewById(R.id.wel_btn);
		yes_btn_text = (Button) findViewById(R.id.btn_wel_btn);
		no_btn = (RelativeLayout) findViewById(R.id.wel_btn2);
		no_btn_text = (Button) findViewById(R.id.btn_wel_btn2);
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
		yes_btn.setOnClickListener(this);
		yes_btn_text.setOnClickListener(this);
		no_btn.setOnClickListener(this);
		no_btn_text.setOnClickListener(this);
		tab_task.setOnClickListener(this);
		tab_sta.setOnClickListener(this);
		tab_water.setOnClickListener(this);
		tab_water_btn.setOnClickListener(this);
		tab_execise.setOnClickListener(this);
		tab_execise_btn.setOnClickListener(this);
		tab_mood.setOnClickListener(this);
		tab_mood_btn.setOnClickListener(this);

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

		String toDate = year + "-" + dMonth + "-" + dDay;
		String curTime = hours + ":" + minutes + ":" + seconds;

		db.addContact(new Contact(toDate, curTime, result, status, mode));
		Toast.makeText(getApplicationContext(), "Thank you", Toast.LENGTH_SHORT).show();

		//-  Insert data in sync adapter database.

		ContentValues cv = new ContentValues();
		cv.put(WellBeingDB.TIME_STAMP, Util.getCurrentDateTime());
		cv.put(WellBeingDB.TYPE, ConstantVO.EXERCISE);
		cv.put(WellBeingDB.VALUE, result);
		getContentResolver().insert(WellBeing.CONTENT_URI, cv);
		isAnswered = true;

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
			if (Util.isInternetAvailable(WellBeingActivityExercise.this)) {
				Intent intentBack = new Intent(WellBeingActivityExercise.this, HumDetailsView.class);
				startActivity(intentBack);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_leave_right);
			} else {
				Intent i = new Intent(WellBeingActivityExercise.this, NoInternetPage.class);
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
			inserData("no", "2", "exercise");
			break;
		case R.id.btn_wel_btn:
			inserData("no", "2", "exercise");
			break;
		case R.id.wel_btn2:
			inserData("yes", "2", "exercise");
			break;
		case R.id.btn_wel_btn2:
			inserData("yes", "2", "exercise");
			break;
		case R.id.task_btn:

			Intent intentTaskBtn = new Intent(WellBeingActivityExercise.this, WellBeingActivityExercise.class);
			startActivity(intentTaskBtn);
			overridePendingTransition(0, 0);
			finish();
			break;
		case R.id.static_btn:
			Intent intentStaticBtn = new Intent(WellBeingActivityExercise.this, WellBeingStatisticsExercise.class);
			startActivity(intentStaticBtn);
			overridePendingTransition(0, 0);
			finish();
			break;
		case R.id.wel_water_btn:
			Intent intentWaterBtn = new Intent(WellBeingActivityExercise.this, WellBeingActivitySleep.class);
			startActivity(intentWaterBtn);
			overridePendingTransition(0, 0);
			finish();
			break;
		case R.id.btn_water_btn:
			Intent intentWaterBtn2 = new Intent(WellBeingActivityExercise.this, WellBeingActivitySleep.class);
			startActivity(intentWaterBtn2);
			overridePendingTransition(0, 0);
			finish();
			break;
		case R.id.wel_ex_btn:
			Intent intentWelExBtn = new Intent(WellBeingActivityExercise.this, WellBeingActivityExercise.class);
			startActivity(intentWelExBtn);
			overridePendingTransition(0, 0);
			finish();
			break;
		case R.id.btn_ex_btn:
			Intent intentWelBeingExercise = new Intent(WellBeingActivityExercise.this, WellBeingActivityExercise.class);
			startActivity(intentWelBeingExercise);
			overridePendingTransition(0, 0);
			finish();
			break;
		case R.id.wel_mood_btn:

			Intent intentWelBeingMood = new Intent(WellBeingActivityExercise.this, WellBeingActivityMood.class);
			startActivity(intentWelBeingMood);
			overridePendingTransition(0, 0);
			finish();
			break;
		case R.id.btn_mood_btn:
			Intent intentMoodBtn = new Intent(WellBeingActivityExercise.this, WellBeingActivityMood.class);
			startActivity(intentMoodBtn);
			overridePendingTransition(0, 0);
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		if (!isAnswered)
			inserData("n/a", "2", "exercise");
		finish();
	}
	public void onHomeClick(View v) {
		startActivity(new Intent(mContext, DashboardActivity.class));
		finish();
	}
}
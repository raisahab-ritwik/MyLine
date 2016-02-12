package com.seva60plus.hum.wellbeing;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.seva60plus.hum.R;
import com.seva60plus.hum.activity.BaseActivity;
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

public class WellBeingActivitySleep extends BaseActivity implements OnClickListener {

	private ImageView backBtn;
	private RelativeLayout backSetup;

	private Button less, equal, more;
	private Button tab_task, tab_sta;
	private RelativeLayout tab_water, tab_execise, tab_mood;
	private Button tab_water_btn, tab_execise_btn, tab_mood_btn;
	private Button whatsapp, fb, twitter;
	private RelativeLayout submit_btn;
	private Button submit_btn_text;
	private DatabaseHandler db;
	private Calendar c;
	private String setTime = "00:00:01";
	private String dDay, dMonth, dYear;
	private Context mContext;
	private static boolean isAnswered = false;
	private LinearLayout llMenu;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welbing_activity_water);
		mContext = WellBeingActivitySleep.this;

		db = new DatabaseHandler(this);
		c = Calendar.getInstance();

		initView();
	}

	private void initView() {
		less = (Button) findViewById(R.id.less_btn);
		equal = (Button) findViewById(R.id.equal_btn);
		more = (Button) findViewById(R.id.more_btn);
		whatsapp = (Button) findViewById(R.id.whatsapp_btn);
		fb = (Button) findViewById(R.id.facebook_btn);
		twitter = (Button) findViewById(R.id.twitter_btn);
		backBtn = (ImageView) findViewById(R.id.iv_back);
		backSetup = (RelativeLayout) findViewById(R.id.back_settings);
		llMenu = (LinearLayout) findViewById(R.id.llMenu);
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
		// backBtn.setOnClickListener(this);
		backSetup.setOnClickListener(this);
		llMenu.setOnClickListener(this);
		less.setOnClickListener(this);
		equal.setOnClickListener(this);
		more.setOnClickListener(this);
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
			if (Util.isInternetAvailable(mContext)) {
				Intent intent_reg = new Intent(mContext, HumDetailsView.class);
				startActivity(intent_reg);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_leave_right);
			} else {
				Intent i = new Intent(mContext, NoInternetPage.class);
				startActivity(i);
				overridePendingTransition(0, 0);
			}
			break;

		case R.id.llMenu:
			Intent intent_menu = new Intent(getApplicationContext(), MenuLay.class);
			startActivity(intent_menu);
			overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			break;

		case R.id.less_btn:
			String glass = "less";
			if (glass.equals("") || glass == null)
				Toast.makeText(getApplicationContext(), "Please enter no. of glass", Toast.LENGTH_SHORT).show();
			else
				inserData(glass, "2", "sleep");
			break;

		case R.id.equal_btn:

			String glass1 = "equal";

			if (glass1.equals("") || glass1 == null)
				Toast.makeText(getApplicationContext(), "Please enter no. of glass", Toast.LENGTH_SHORT).show();
			else
				inserData(glass1, "2", "sleep");
			break;

		case R.id.more_btn:
			String glass2 = "more";
			if (glass2.equals("") || glass2 == null)
				Toast.makeText(getApplicationContext(), "Please enter no. of glass", Toast.LENGTH_SHORT).show();
			else
				inserData(glass2, "2", "sleep");
			break;

		case R.id.task_btn:
			Intent intent_WelbingActivityWater = new Intent(WellBeingActivitySleep.this, WellBeingActivitySleep.class);
			startActivity(intent_WelbingActivityWater);
			overridePendingTransition(0, 0);
			finish();
			break;

		case R.id.static_btn:
			Intent intent_WelbingStaActivityWater = new Intent(WellBeingActivitySleep.this, WellBeingStatisticsSleep.class);
			startActivity(intent_WelbingStaActivityWater);
			overridePendingTransition(0, 0);
			finish();
			break;

		case R.id.wel_water_btn:
			Intent intent_WelbingWater = new Intent(WellBeingActivitySleep.this, WellBeingActivitySleep.class);
			startActivity(intent_WelbingWater);
			overridePendingTransition(0, 0);
			finish();
			break;

		case R.id.btn_water_btn:
			Intent intObj = new Intent(WellBeingActivitySleep.this, WellBeingActivitySleep.class);
			startActivity(intObj);
			overridePendingTransition(0, 0);
			finish();
			break;

		case R.id.wel_ex_btn:
			Intent intent_WelbingActivityExercise = new Intent(WellBeingActivitySleep.this, WellBeingActivityExercise.class);
			startActivity(intent_WelbingActivityExercise);
			overridePendingTransition(0, 0);
			finish();

			break;

		case R.id.btn_ex_btn:
			Intent intent_WelbingExercise = new Intent(WellBeingActivitySleep.this, WellBeingActivityExercise.class);
			startActivity(intent_WelbingExercise);
			overridePendingTransition(0, 0);
			finish();
			break;

		case R.id.wel_mood_btn:
			Intent intent_WelbingActivityMood = new Intent(WellBeingActivitySleep.this, WellBeingActivityMood.class);
			startActivity(intent_WelbingActivityMood);
			overridePendingTransition(0, 0);
			finish();
			break;

		case R.id.btn_mood_btn:
			Intent intent_WelbingMood = new Intent(WellBeingActivitySleep.this, WellBeingActivityMood.class);
			startActivity(intent_WelbingMood);
			overridePendingTransition(0, 0);
			finish();

			break;

		default:
			break;
		}

	}

	private void inserData(String result, String status, String mode) {

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

		// -------Insert data in sync adapter database.

		ContentValues cv = new ContentValues();
		cv.put(WellBeingDB.TIME_STAMP, Util.getCurrentDateTime());
		cv.put(WellBeingDB.TYPE, ConstantVO.SLEEP);
		cv.put(WellBeingDB.VALUE, result);
		getContentResolver().insert(WellBeing.CONTENT_URI, cv);
		isAnswered = true;
	}

	@Override
	public void onBackPressed() {
		if (!isAnswered)
			inserData("n/a", "2", "sleep");
		finish();

	}
	public void onHomeClick(View v) {
		startActivity(new Intent(mContext, DashboardActivity.class));
		finish();
	}

}
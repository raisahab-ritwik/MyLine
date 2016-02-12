package com.seva60plus.hum.pillreminder;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.seva60plus.hum.R;
import com.seva60plus.hum.activity.MenuLay;

public class PillViewActivity extends Activity {

	private TextView mUserText;
	private TextView mPillText;
	private TextView mDaysText;
	private TextView mTimeText;
	private Long mRowId;
	private ImageView backBtn;
	private PillsDbAdapter mDbHelper;
	TextView headerTitleText;
	Button edit, delete;

	private static final int ACTIVITY_EDIT = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pill_view);
		setTitle(R.string.pill_view_title);

		edit = (Button) findViewById(R.id.editButton);
		delete = (Button) findViewById(R.id.deleteButton);
		headerTitleText = (TextView) findViewById(R.id.header_title);
		headerTitleText.setText("Pill Detail");
		Typeface font = Typeface.createFromAsset(getAssets(), "openSansBold.ttf");
		headerTitleText.setTypeface(font);

		backBtn = (ImageView) findViewById(R.id.iv_back);

		backBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// Take action.
				finish();

			}
		});

		findViewById(R.id.llMenu).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// Take action.
				Intent myIntent = new Intent(PillViewActivity.this, MenuLay.class);
				startActivity(myIntent);
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

			}
		});

		//BBDD
		mDbHelper = new PillsDbAdapter(this);
		mDbHelper.open();

		mUserText = (TextView) findViewById(R.id.text_user);
		mPillText = (TextView) findViewById(R.id.text_pill);

		mDaysText = (TextView) findViewById(R.id.view_textDays);
		mTimeText = (TextView) findViewById(R.id.view_textHours);

		mRowId = (savedInstanceState == null) ? null : (Long) savedInstanceState.getSerializable(PillsDbAdapter.KEY_ROWID);
		if (mRowId == null) {
			Bundle extras = getIntent().getExtras();
			mRowId = extras != null ? extras.getLong(PillsDbAdapter.KEY_ROWID) : null;
		}

		populateFields();
	}

	/**
	 * Rellena los campos de edicion de pil, solo si no es una nueva pill.
	 */
	private void populateFields() {

		//Toast.makeText(getApplicationContext(), "ID : "+mRowId, Toast.LENGTH_SHORT).show();

		if (mRowId != null) {
			//Cursor
			Cursor pillcursor = mDbHelper.fetchPill(mRowId);
			startManagingCursor(pillcursor);//Android lo gestiona

			//Fija el nombre del usuario
			mUserText.setText(pillcursor.getString(pillcursor.getColumnIndexOrThrow(PillsDbAdapter.KEY_USER)));
			//Fija el nombre de la pill
			mPillText.setText(pillcursor.getString(pillcursor.getColumnIndexOrThrow(PillsDbAdapter.KEY_PILL)));
			//Fija los dias de la semana
			mDaysText.setText(pillcursor.getString(pillcursor.getColumnIndexOrThrow(PillsDbAdapter.KEY_DAYS)));
			//Fija las horas de la pill
			/*
			 * String time =
			 * pillcursor.getString(pillcursor.getColumnIndexOrThrow
			 * (PillsDbAdapter.KEY_HOUR)); String s= time ;
			 * 
			 * DateFormat f1 = new SimpleDateFormat("kk:mm"); Date d = null; try
			 * { d = f1.parse(s); DateFormat f2 = new SimpleDateFormat("h:mma");
			 * time = f2.format(d).toUpperCase(); // "12:18am"
			 * System.out.println(time);
			 * 
			 * } catch (ParseException e) {
			 * 
			 * // TODO Auto-generated catch block e.printStackTrace(); }
			 */
			mTimeText.setText(pillcursor.getString(pillcursor.getColumnIndexOrThrow(PillsDbAdapter.KEY_HOUR)));

		}

		edit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// Take action.
				Intent i = new Intent(PillViewActivity.this, PillEdit.class);
				i.putExtra(PillsDbAdapter.KEY_ROWID, mRowId);
				startActivityForResult(i, ACTIVITY_EDIT);
			}
		});

		delete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// Take action.
				cancelAlarms(mRowId);
				mDbHelper.deletePill(mRowId);
				Intent i = new Intent(PillViewActivity.this, TakeThePill.class);
				startActivity(i);
			}
		});

	}

	private void cancelAlarms(long rowId) {
		Cursor elim = mDbHelper.fetchPill(rowId);
		int alarms = elim.getInt(elim.getColumnIndexOrThrow(PillsDbAdapter.KEY_ALARMS));

		Intent intent = new Intent(this, RepeatingAlarm.class);

		for (int i = 0; i < alarms; i++) {
			PendingIntent sender = PendingIntent.getBroadcast(this, (int) rowId * 10000 + i, intent, 0);
			AlarmManager am = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
			am.cancel(sender);
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mDbHelper.close();
	}

}

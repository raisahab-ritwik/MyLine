
package com.seva60plus.hum.reminder.taskreminder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.seva60plus.hum.R;
import com.seva60plus.hum.activity.DashboardActivity;
import com.seva60plus.hum.pillreminder.PillsDbAdapter;
import com.seva60plus.hum.util.ConnectionDetector;

public class ReminderEditActivity extends Activity {

	// 
	// Dialog Constants
	//
	private static final int DATE_PICKER_DIALOG = 0;
	private static final int TIME_PICKER_DIALOG = 1;

	// 
	// Date Format 
	//
	private static final String DATE_FORMAT = "yyyy-MM-dd"; 
	private static final String TIME_FORMAT = "kk:mm";
	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd kk:mm:ss";

	private EditText mTitleText;
	private EditText mBodyText;
	private Button mDateButton;
	private Button mTimeButton;
	private Button mConfirmButton;
	private Long mRowId;
	private PillsDbAdapter mDbHelper;
	private Calendar mCalendar;  


	RelativeLayout backBtn;
	ImageView menuIcon;
	ImageView backBtnSub;

	//---by Dibyendu
	// flag for Internet connection status
	Boolean isInternetPresent = false;

	// Connection detector class
	ConnectionDetector cd;
	LocationManager lm;

	//------End

	LinearLayout date_lay,time_lay;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_reminder_edit);


		mDbHelper = new PillsDbAdapter(this);
		mDbHelper.open(); 
		
		mRowId = (savedInstanceState == null) ? null :
			(Long) savedInstanceState.getSerializable(PillsDbAdapter.RKEY_ROWID);
		if (mRowId == null) {
			Bundle extras = getIntent().getExtras();
			mRowId = extras != null ? extras.getLong(PillsDbAdapter.RKEY_ROWID) : null;
		}
		
		cd = new ConnectionDetector(getApplicationContext());// by Dibyendu
		lm = (LocationManager) getSystemService(LOCATION_SERVICE);// by Dibyendu

		backBtnSub = (ImageView)findViewById(R.id.iv_back);
		backBtnSub.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(0, 0);
			}
		});


		findViewById(R.id.llMenu).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				System.out.println("MENU----------");


				//finish();
			}
		});

		backBtn=(RelativeLayout)findViewById(R.id.back_settings);

		backBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// Take action.

				System.out.println("backre----------");


			/*	Intent i = new Intent(ReminderEditActivity.this,Dashboard.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_leave_right);
			*/

			}
		});

		mCalendar = Calendar.getInstance(); 
		mTitleText = (EditText) findViewById(R.id.title);
		mBodyText = (EditText) findViewById(R.id.body);
		mDateButton = (Button) findViewById(R.id.reminder_date);
		mDateButton.setText("Enter date");
		mTimeButton = (Button) findViewById(R.id.reminder_time);
		mTimeButton.setText("Enter time");

		mConfirmButton = (Button) findViewById(R.id.confirm);



		//registerButtonListenersAndSetDefaultText();
		//------------
		mDateButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(DATE_PICKER_DIALOG);
				updateDateButtonText();
			}
		}); 


		mTimeButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(TIME_PICKER_DIALOG); 
				updateTimeButtonText();
			}
		}); 

		mConfirmButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				saveState(); 
				setResult(RESULT_OK);
				// Toast.makeText(ReminderEditActivity.this, getString(R.string.task_saved_message), Toast.LENGTH_SHORT).show();
				finish(); 
			}

		});

		date_lay = (LinearLayout)findViewById(R.id.reminder_date_lay);
		date_lay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(DATE_PICKER_DIALOG);
				updateDateButtonText();
			}
		});

		time_lay = (LinearLayout)findViewById(R.id.reminder_time_lay);
		time_lay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(TIME_PICKER_DIALOG); 
				updateTimeButtonText();
			}
		});

		// 
		// 
		//----------

	}

	private void setRowIdFromIntent() {
		if (mRowId == null) {
			Bundle extras = getIntent().getExtras();            
			mRowId = extras != null ? extras.getLong(PillsDbAdapter.RKEY_ROWID) 
					: null;

		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		mDbHelper.close(); 
	}

	@Override
	protected void onResume() {
		super.onResume();
		mDbHelper.open(); 
		setRowIdFromIntent();
		populateFields();
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch(id) {
		case DATE_PICKER_DIALOG: 
			return showDatePicker();
		case TIME_PICKER_DIALOG: 
			return showTimePicker(); 
		}
		return super.onCreateDialog(id);
	}

	private DatePickerDialog showDatePicker() {


		DatePickerDialog datePicker = new DatePickerDialog(ReminderEditActivity.this, new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				mCalendar.set(Calendar.YEAR, year);
				mCalendar.set(Calendar.MONTH, monthOfYear);
				mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				updateDateButtonText(); 
			}
		}, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH)); 
		return datePicker; 
	}

	private TimePickerDialog showTimePicker() {

		TimePickerDialog timePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {

			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
				mCalendar.set(Calendar.MINUTE, minute); 
				updateTimeButtonText(); 
			}
		}, mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE), true); 

		return timePicker; 
	}

	private void registerButtonListenersAndSetDefaultText() {

		mDateButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(DATE_PICKER_DIALOG);  
			}
		}); 


		mTimeButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(TIME_PICKER_DIALOG); 
			}
		}); 

		mConfirmButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				saveState(); 
				setResult(RESULT_OK);
				Toast.makeText(ReminderEditActivity.this, getString(R.string.task_saved_message), Toast.LENGTH_SHORT).show();
				finish(); 
			}

		});

		updateDateButtonText(); 
		updateTimeButtonText();
	}

	private void populateFields()  {



		// Only populate the text boxes and change the calendar date
		// if the row is not null from the database. 
		if (mRowId != null) {
			Cursor reminder = mDbHelper.fetchReminder(mRowId);
			startManagingCursor(reminder);
			mTitleText.setText(reminder.getString(
					reminder.getColumnIndexOrThrow(PillsDbAdapter.RKEY_TITLE)));
			mBodyText.setText(reminder.getString(
					reminder.getColumnIndexOrThrow(PillsDbAdapter.RKEY_BODY)));


			// Get the date from the database and format it for our use. 
			SimpleDateFormat dateTimeFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
			Date date = null;
			try {
				String dateString = reminder.getString(reminder.getColumnIndexOrThrow(PillsDbAdapter.RKEY_DATE_TIME)); 
				date = dateTimeFormat.parse(dateString);
				mCalendar.setTime(date); 
			} catch (ParseException e) {
				Log.e("ReminderEditActivity", e.getMessage(), e); 
			} 
		} else {
			// This is a new WELLBEING - add defaults from preferences if set. 
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this); 
			String defaultTitleKey = getString(R.string.pref_task_title_key); 
			String defaultTimeKey = getString(R.string.pref_default_time_from_now_key); 

			String defaultTitle = prefs.getString(defaultTitleKey, null);
			String defaultTime = prefs.getString(defaultTimeKey, null); 

			if(defaultTitle != null)
				mTitleText.setText(defaultTitle); 

			if(defaultTime != null)
				mCalendar.add(Calendar.MINUTE, Integer.parseInt(defaultTime));

		}

		updateDateButtonText(); 
		updateTimeButtonText(); 

	}

	private void updateTimeButtonText() {
		// Set the time button text based upon the value from the database
		SimpleDateFormat timeFormat = new SimpleDateFormat(TIME_FORMAT); 
		String timeForButton = timeFormat.format(mCalendar.getTime()); 
		mTimeButton.setText(timeForButton);
	}

	private void updateDateButtonText() {
		// Set the date button text based upon the value from the database 
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT); 
		String dateForButton = dateFormat.format(mCalendar.getTime()); 
		mDateButton.setText(dateForButton);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putLong(PillsDbAdapter.RKEY_ROWID, mRowId);
	}



	private void saveState() {
		String title = mTitleText.getText().toString();
		String body = mBodyText.getText().toString();

		SimpleDateFormat dateTimeFormat = new SimpleDateFormat(DATE_TIME_FORMAT); 
		String reminderDateTime = dateTimeFormat.format(mCalendar.getTime());

		if (mRowId == null) {

			long id = mDbHelper.createReminder(title, body, reminderDateTime);
			if (id > 0) {
				mRowId = id;
			}
		} else {
			mDbHelper.updateReminder(mRowId, title, body, reminderDateTime);
		}

		new ReminderManager(this).setReminder(mRowId, mCalendar); 
	}

	public void onHomeClick(View v) {
		startActivity(new Intent(ReminderEditActivity.this, DashboardActivity.class));
		finish();
	}

}

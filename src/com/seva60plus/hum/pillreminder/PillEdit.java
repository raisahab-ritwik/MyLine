package com.seva60plus.hum.pillreminder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.seva60plus.hum.R;
import com.seva60plus.hum.activity.DashboardActivity;
import com.seva60plus.hum.activity.HumDetailsView;
import com.seva60plus.hum.activity.MenuLay;
import com.seva60plus.hum.custom.NoInternetPage;
import com.seva60plus.hum.util.ConnectionDetector;
import com.seva60plus.hum.util.Util;

public class PillEdit extends Activity {

	MyCustomAdapter dataAdapter = null;
	ArrayList<String> myDay = new ArrayList<String>();
	ArrayList<PillDay> dayList = new ArrayList<PillDay>();

	CheckBox check1, check2, check3, check4, check5, check6, check7;

	int totalHeight;
	Button whatsapp, fb, twitter;

	boolean backbuttom = false;

	private ImageView backBtn;
	RelativeLayout backSetup;
	//Elements visibles:
	private AutoCompleteTextView mUserText;
	private AutoCompleteTextView mPillText;
	private TextView mDaysText, mHoursText;

	private LinearLayout deleteTimeButton, addTimeButton;
	Button addTimeButtonView, deleteTimeButtonView;
	//BBDD
	private Long mRowId;
	private PillsDbAdapter mDbHelper;

	//---by Dibyendu
	// flag for Internet connection status
	Boolean isInternetPresent = false;

	// Connection detector class
	ConnectionDetector cd;

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mDbHelper.close();
	}

	//Time
	private int mHour;
	private int mMinute;
	private ArrayList<String> mArrayHours = new ArrayList<String>();
	private int mAlarms = 0;

	//Days
	private boolean[] mArrayDays = new boolean[7];
	private boolean[] mRemoveTime;

	//Dialogs
	private static final int TIME_DIALOG_ID = 0;
	private static final int DIALOG_MULTIPLE_CHOICE = 1;
	private static final int DIALOG_REMOVE_TIME = 2;

	//Autocompletado Nombres
	private ArrayList<String> mPeopleList;

	/**
	 * Metodo llamado al crear la actividad
	 */

	RelativeLayout helpLay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//BBDD
		mDbHelper = new PillsDbAdapter(this);
		mDbHelper.open();
		//Vistas
		setContentView(R.layout.pill_edit);
		setTitle(R.string.edit_pill);

		//-------------
		dayList.clear();
		/*
		 * //Array list of countries
		 * 
		 * PillDay day = new PillDay("Sunday",false); dayList.add(day); day =
		 * new PillDay("Monday",false); dayList.add(day); day = new
		 * PillDay("Tuesday",false); dayList.add(day); day = new
		 * PillDay("Wednesday",false); dayList.add(day); day = new
		 * PillDay("Thursday",false); dayList.add(day); day = new
		 * PillDay("Friday",false); dayList.add(day); day = new
		 * PillDay("Saturday",false); dayList.add(day); //---------
		 */

		helpLay = (RelativeLayout) findViewById(R.id.helpLay);
		helpLay.setVisibility(View.VISIBLE);
		helpLay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intObj = new Intent(PillEdit.this, HelpPillReminderPage.class);
				startActivity(intObj);
			}
		});

		check1 = (CheckBox) findViewById(R.id.checkBox1);
		check2 = (CheckBox) findViewById(R.id.checkBox2);
		check3 = (CheckBox) findViewById(R.id.checkBox3);
		check4 = (CheckBox) findViewById(R.id.checkBox4);
		check5 = (CheckBox) findViewById(R.id.checkBox5);
		check6 = (CheckBox) findViewById(R.id.checkBox6);
		check7 = (CheckBox) findViewById(R.id.checkBox7);

		check1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (check1.isChecked()) {
					myDay.add("Sunday");
					String myDay1 = myDay.toString().replace("[", "");
					String myDay2 = myDay1.replace("]", "");
					System.out.println("DAY2 :" + myDay2);

					mDaysText.setText(myDay2);

				} else {
					myDay.remove("Sunday");
					String myDay1 = myDay.toString().replace("[", "");
					String myDay2 = myDay1.replace("]", "");
					System.out.println("DAY2 :" + myDay2);

					mDaysText.setText(myDay2);
				}
			}
		});
		check2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (check2.isChecked()) {
					myDay.add("Monday");
					String myDay1 = myDay.toString().replace("[", "");
					String myDay2 = myDay1.replace("]", "");
					System.out.println("DAY2 :" + myDay2);

					mDaysText.setText(myDay2);
				} else {
					myDay.remove("Monday");
					String myDay1 = myDay.toString().replace("[", "");
					String myDay2 = myDay1.replace("]", "");
					System.out.println("DAY2 :" + myDay2);

					mDaysText.setText(myDay2);
				}
			}
		});

		check3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (check3.isChecked()) {
					myDay.add("Tuesday");
					String myDay1 = myDay.toString().replace("[", "");
					String myDay2 = myDay1.replace("]", "");
					System.out.println("DAY2 :" + myDay2);

					mDaysText.setText(myDay2);
				} else {
					myDay.remove("Tuesday");
					String myDay1 = myDay.toString().replace("[", "");
					String myDay2 = myDay1.replace("]", "");
					System.out.println("DAY2 :" + myDay2);

					mDaysText.setText(myDay2);
				}
			}
		});

		check4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (check4.isChecked()) {
					myDay.add("Wednesday");
					String myDay1 = myDay.toString().replace("[", "");
					String myDay2 = myDay1.replace("]", "");
					System.out.println("DAY2 :" + myDay2);

					mDaysText.setText(myDay2);
				} else {
					myDay.remove("Wednesday");
					String myDay1 = myDay.toString().replace("[", "");
					String myDay2 = myDay1.replace("]", "");
					System.out.println("DAY2 :" + myDay2);

					mDaysText.setText(myDay2);
				}
			}
		});

		check5.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (check5.isChecked()) {
					myDay.add("Thursday");
					String myDay1 = myDay.toString().replace("[", "");
					String myDay2 = myDay1.replace("]", "");
					System.out.println("DAY2 :" + myDay2);

					mDaysText.setText(myDay2);
				} else {
					myDay.remove("Thursday");
					String myDay1 = myDay.toString().replace("[", "");
					String myDay2 = myDay1.replace("]", "");
					System.out.println("DAY2 :" + myDay2);

					mDaysText.setText(myDay2);
				}
			}
		});

		check6.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (check6.isChecked()) {
					myDay.add("Friday");
					String myDay1 = myDay.toString().replace("[", "");
					String myDay2 = myDay1.replace("]", "");
					System.out.println("DAY2 :" + myDay2);

					mDaysText.setText(myDay2);
				} else {
					myDay.remove("Friday");
					String myDay1 = myDay.toString().replace("[", "");
					String myDay2 = myDay1.replace("]", "");
					System.out.println("DAY2 :" + myDay2);

					mDaysText.setText(myDay2);
				}
			}
		});

		check7.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (check7.isChecked()) {
					myDay.add("Saturday");
					String myDay1 = myDay.toString().replace("[", "");
					String myDay2 = myDay1.replace("]", "");
					System.out.println("DAY2 :" + myDay2);

					mDaysText.setText(myDay2);
				} else {
					myDay.remove("Saturday");
					String myDay1 = myDay.toString().replace("[", "");
					String myDay2 = myDay1.replace("]", "");
					System.out.println("DAY2 :" + myDay2);

					mDaysText.setText(myDay2);
				}
			}
		});

		LinearLayout banner = (LinearLayout) findViewById(R.id.footerLay);

		banner.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// Take action.
				/*
				 * Intent myIntent = new Intent(PillEdit.this, AdBanner.class);
				 * myIntent.putExtra("banner_value", "2");
				 * startActivity(myIntent);
				 */
				Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.homers.in"));
				startActivity(i);

			}
		});

		whatsapp = (Button) findViewById(R.id.whatsapp_btn);
		fb = (Button) findViewById(R.id.facebook_btn);
		twitter = (Button) findViewById(R.id.twitter_btn);

		whatsapp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				shareAppLinkViaWhatsApp();
			}
		});

		fb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				shareAppLinkViaFacebook();
			}
		});

		twitter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				shareAppLinkViaTwitter();
			}
		});

		backBtn = (ImageView) findViewById(R.id.iv_back);

		backBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// Take action.
				finish();

			}
		});

		backSetup = (RelativeLayout) findViewById(R.id.back_settings);

		cd = new ConnectionDetector(getApplicationContext());// by Dibyendu

		backSetup.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				if (Util.isInternetAvailable(PillEdit.this)) {
					Intent i = new Intent(PillEdit.this, HumDetailsView.class);
					startActivity(i);
					overridePendingTransition(0, 0);
					finish();
				} else {

					Intent i = new Intent(PillEdit.this, NoInternetPage.class);
					startActivity(i);
					overridePendingTransition(0, 0);

				}

			}
		});

		findViewById(R.id.llMenu).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// Take action.
				Intent myIntent = new Intent(PillEdit.this, MenuLay.class);
				startActivity(myIntent);
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

			}
		});

		

		//Recuperamos elementos visuales
		mUserText = (AutoCompleteTextView) findViewById(R.id.user);
		mPillText = (AutoCompleteTextView) findViewById(R.id.pill);
		mDaysText = (TextView) findViewById(R.id.textDays);
		mHoursText = (TextView) findViewById(R.id.textHours);
		//Recuperamos botones
		Button confirmButton = (Button) findViewById(R.id.confirm);
		Button checkBox = (Button) findViewById(R.id.add_days);
		addTimeButton = (LinearLayout) findViewById(R.id.add_time);
		addTimeButtonView = (Button) findViewById(R.id.reminder_time);
		deleteTimeButtonView = (Button) findViewById(R.id.reminder_time1);
		deleteTimeButton = (LinearLayout) findViewById(R.id.delete_time);
		deleteTimeButton.setEnabled(false);

		/*
		 * Buscamos la fila de la pill a editar. Si estamos creando una, mRowId
		 * valdra null y se recuperan extras del indent, sino valdra el numero
		 * de fila de la pill a editar.
		 */
		mRowId = (savedInstanceState == null) ? null : (Long) savedInstanceState.getSerializable(PillsDbAdapter.KEY_ROWID);
		if (mRowId == null) {
			Bundle extras = getIntent().getExtras();
			mRowId = extras != null ? extras.getLong(PillsDbAdapter.KEY_ROWID) : null;
		}
		//Se rellenan los campos como corresponda	
		populateFields();

		cancelAlarms(mAlarms);

		//Listener para el boton de confirmar
		confirmButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				if (mUserText.getText().length() == 0) {
					Toast toast1 = Toast.makeText(getApplicationContext(), R.string.error_user, Toast.LENGTH_SHORT);
					toast1.show();
				} else if (mPillText.getText().length() == 0) {
					Toast toast1 = Toast.makeText(getApplicationContext(), R.string.error_pill, Toast.LENGTH_SHORT);
					toast1.show();
				} else if (mDaysText.getText().toString().equals(getResources().getString(R.string.no_days))) {
					Toast toast1 = Toast.makeText(getApplicationContext(), R.string.error_day, Toast.LENGTH_SHORT);
					toast1.show();
				} else if (mArrayHours.isEmpty()) {
					Toast toast1 = Toast.makeText(getApplicationContext(), R.string.error_hour, Toast.LENGTH_SHORT);
					toast1.show();
				} else {
					setResult(RESULT_OK);
					finish();
					updateAlarms();
				}
			}

		});

		//Listener para el boton de a�adir horas
		addTimeButton.setOnClickListener(new View.OnClickListener() {

			final Calendar c = Calendar.getInstance();

			public void onClick(View view) {
				//codigo para cuando se pulsa el boton: lanza el time dialog
				mHour = c.get(Calendar.HOUR_OF_DAY);
				mMinute = c.get(Calendar.MINUTE);
				showDialog(TIME_DIALOG_ID);
			}

		});

		addTimeButtonView.setOnClickListener(new View.OnClickListener() {

			final Calendar c = Calendar.getInstance();

			public void onClick(View view) {
				//codigo para cuando se pulsa el boton: lanza el time dialog
				mHour = c.get(Calendar.HOUR_OF_DAY);
				mMinute = c.get(Calendar.MINUTE);
				showDialog(TIME_DIALOG_ID);
			}

		});

		displayListView();

		//Listener para el boton que muestra la seleccion de dias de la semana
		checkBox.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				showDialog(DIALOG_MULTIPLE_CHOICE);
			}

		});

		//Listener para el boton de borrar horas
		deleteTimeButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				showDialog(DIALOG_REMOVE_TIME);
			}

		});

		deleteTimeButtonView.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				showDialog(DIALOG_REMOVE_TIME);
			}

		});

		String[] pillsNames = getResources().getStringArray(R.array.pill_names);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.pills_item_auto, pillsNames);
		mPillText.setAdapter(adapter);

		populatePeopleList();

		String[] userNames = mPeopleList.toArray(new String[0]);
		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, R.layout.users_item_auto, userNames);
		mUserText.setAdapter(adapter2);

	}

	private void cancelAlarms(int alarms) {

		Intent intent = new Intent(this, RepeatingAlarm.class);

		for (int i = 0; i < alarms; i++) {
			PendingIntent sender = PendingIntent.getBroadcast(this, mRowId.intValue() * 10000 + i, intent, 0);
			AlarmManager am = (AlarmManager) PillEdit.this.getSystemService(Context.ALARM_SERVICE);
			am.cancel(sender);
		}
	}

	/**
	 * Actualiza las alarmas tras confirmar los cambios.
	 */
	private void updateAlarms() {
		if (mRowId == null) {
			saveState();

			for (int i = 0; i < mArrayDays.length; i++) {
				mArrayDays[i] = (mDaysText.getText().toString().indexOf(getResources().getStringArray(R.array.select_dialog_day)[i])) != -1;
			}

		}
		mAlarms = 0;
		Calendar calendar = Calendar.getInstance();
		Calendar currentDay;
		int day = calendar.get(Calendar.DAY_OF_WEEK);
		Intent intent = new Intent(this, RepeatingAlarm.class);
		intent.putExtra("user", mUserText.getText().toString());
		intent.putExtra("pill", mPillText.getText().toString());

		for (int i = 0; i < mArrayDays.length; i++) {
			if ((day - 1 + i) == mArrayDays.length)
				day = day - 7;
			if (mArrayDays[day - 1 + i]) {
				String h;
				int hourOfDay;
				int min;

				for (int j = 0; j < mArrayHours.size(); j++) {
					h = mArrayHours.get(j);
					hourOfDay = Integer.parseInt(h.split(":")[0]);
					min = Integer.parseInt(h.split(":")[1]);
					currentDay = Calendar.getInstance();
					currentDay.set(Calendar.HOUR_OF_DAY, hourOfDay);
					currentDay.set(Calendar.MINUTE, min);
					currentDay.set(Calendar.SECOND, 0);
					currentDay.set(Calendar.MILLISECOND, 0);
					if (i == 0
							&& currentDay.get(Calendar.HOUR_OF_DAY) < Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
							|| (currentDay.get(Calendar.MINUTE) <= Calendar.getInstance().get(Calendar.MINUTE) && currentDay.get(Calendar.HOUR_OF_DAY) == Calendar
									.getInstance().get(Calendar.HOUR_OF_DAY)))
						currentDay.add(Calendar.DAY_OF_YEAR, i + 7);
					else
						currentDay.add(Calendar.DAY_OF_YEAR, i);
					long firstTime = currentDay.getTimeInMillis();
					PendingIntent sender = PendingIntent.getBroadcast(this, mRowId.intValue() * 10000 + mAlarms, intent, 0);

					AlarmManager am = (AlarmManager) PillEdit.this.getSystemService(Context.ALARM_SERVICE);
					am.setRepeating(AlarmManager.RTC_WAKEUP, firstTime, 7 * 24 * 3600 * 1000, sender);
					mAlarms++;
				}
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			backbuttom = true;
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * Pasa de ArrayList a String las horas
	 * 
	 * @return String cn las horas
	 */
	private String hourArrayToString() {
		String hourString = "";
		for (int i = 0; i < mArrayHours.size(); i++) {
			hourString = hourString + mArrayHours.get(i);
			if (i < (mArrayHours.size() - 1))
				hourString = hourString + ", ";
		}
		return hourString;
	}

	/**
	 * Actualiza los dias a partir del dialogo de seleccion de dias y lo fija
	 * como texto en el elemento visual correspondiente.
	 */
	private void updateDays() {
		String stringDays = "";
		for (int i = 0; i < mArrayDays.length; i++) {
			if (mArrayDays[i]) {
				if (stringDays != "")
					stringDays = stringDays + ", ";
				stringDays = stringDays + getResources().getStringArray(R.array.select_dialog_day)[i];
			}
		}
		if (stringDays == "")
			mDaysText.setText(R.string.no_days);
		else
			mDaysText.setText(stringDays);

		System.out.println("*******====DAYS==" + stringDays);
	}

	/**
	 * Actualiza la lista de las horas en su elemento visual.
	 */
	private void updateTextTime() {
		String stringHours = "";
		for (int i = 0; i < mArrayHours.size(); i++) {
			if (mArrayHours.get(i) != "") {
				if (stringHours != "")
					stringHours = stringHours + ", ";
				stringHours = stringHours + mArrayHours.get(i).toString();
			}
		}
		if (stringHours == "") {
			mHoursText.setVisibility(View.GONE);
			//mHoursText.setText(R.string.no_hours);

			deleteTimeButton.setEnabled(false);
		} else {
			mHoursText.setVisibility(View.VISIBLE);
			mHoursText.setText(stringHours);

			deleteTimeButton.setEnabled(true);
		}

	}

	/**
	 * A�ade al ArrayList de horas la hora dada por parametros
	 * 
	 * @param hourOfDay
	 *            Hora
	 * @param minute
	 *            Minuto
	 */
	private void updateTime(int hourOfDay, int minute) {
		String h = new StringBuilder().append(pad(hourOfDay)).append(":").append(pad(minute)).toString();
		if (!mArrayHours.contains(h)) {
			mArrayHours.add(h);
			updateTextTime();
		}
	}

	/**
	 * A�ade un cero a la izquierda del entero dato.
	 * 
	 * @param c
	 *            Entero a a�adir digito
	 * @return String con cero a la izquierda + c.
	 */
	private static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}

	private void removeTime(boolean[] mRemoveTime) {
		for (int i = mRemoveTime.length - 1; i > -1; i--) {
			if (mRemoveTime[i]) {
				mArrayHours.remove(i);
			}
		}
		updateTextTime();
		removeDialog(DIALOG_REMOVE_TIME);
	}

	/**
	 * Rellena los campos de edicion de pil, solo si no es una nueva pill.
	 */
	private void populateFields() {

		if (mRowId != null) {

			dayList.clear();
			//Cursor
			Cursor pillcursor = mDbHelper.fetchPill(mRowId);
			startManagingCursor(pillcursor);//Android lo gestiona

			//Fija el nombre del usuario
			mUserText.setText(pillcursor.getString(pillcursor.getColumnIndexOrThrow(PillsDbAdapter.KEY_USER)));
			//Fija el nombre de la pill
			mPillText.setText(pillcursor.getString(pillcursor.getColumnIndexOrThrow(PillsDbAdapter.KEY_PILL)));
			//Fija los dias de la semana
			mDaysText.setText(pillcursor.getString(pillcursor.getColumnIndexOrThrow(PillsDbAdapter.KEY_DAYS)));
			//prepara el dialogo de dias	

			for (int i = 0; i < mArrayDays.length; i++) {
				mArrayDays[i] = (mDaysText.getText().toString().indexOf(getResources().getStringArray(R.array.select_dialog_day)[i])) != -1;
			}

			//-------------by Dibyendu
			String getMyDay = mDaysText.getText().toString();

			if (getMyDay.contains("Sunday")) {
				PillDay day = new PillDay("Sunday", true);
				dayList.add(day);
				check1.setChecked(true);
				myDay.add("Sunday");
			} else {
				PillDay day = new PillDay("Sunday", false);
				dayList.add(day);
				check1.setChecked(false);
			}

			if (getMyDay.contains("Monday")) {

				PillDay day = new PillDay("Monday", true);
				dayList.add(day);
				check2.setChecked(true);
				myDay.add("Monday");
			} else {
				PillDay day = new PillDay("Monday", false);
				dayList.add(day);
				check2.setChecked(false);
			}

			if (getMyDay.contains("Tuesday")) {

				PillDay day = new PillDay("Tuesday", true);
				dayList.add(day);
				check3.setChecked(true);
				myDay.add("Tuesday");
			} else {
				PillDay day = new PillDay("Tuesday", false);
				dayList.add(day);
				check3.setChecked(false);
			}

			if (getMyDay.contains("Wednesday")) {

				PillDay day = new PillDay("Wednesday", true);
				dayList.add(day);
				check4.setChecked(true);
				myDay.add("Wednesday");
			} else {
				PillDay day = new PillDay("Wednesday", false);
				dayList.add(day);
				check4.setChecked(false);
			}

			if (getMyDay.contains("Thursday")) {

				PillDay day = new PillDay("Thursday", true);
				dayList.add(day);
				check5.setChecked(true);
				myDay.add("Thursday");
			} else {

				PillDay day = new PillDay("Thursday", false);
				dayList.add(day);
				check5.setChecked(false);
			}

			if (getMyDay.contains("Friday")) {

				PillDay day = new PillDay("Friday", true);
				dayList.add(day);
				check6.setChecked(true);
				myDay.add("Friday");
			} else {
				PillDay day = new PillDay("Friday", false);
				dayList.add(day);
				check6.setChecked(false);
			}
			if (getMyDay.contains("Saturday")) {

				PillDay day = new PillDay("Saturday", true);
				dayList.add(day);
				check7.setChecked(true);
				myDay.add("Saturday");
			} else {
				PillDay day = new PillDay("Saturday", false);
				dayList.add(day);
				check7.setChecked(false);
			}

			String hourString = pillcursor.getString(pillcursor.getColumnIndexOrThrow(PillsDbAdapter.KEY_HOUR));
			mArrayHours = new ArrayList<String>(Arrays.asList(hourString.split(", ")));

			updateTextTime();
			//Fija el numero de alarmas
			mAlarms = pillcursor.getInt(pillcursor.getColumnIndexOrThrow(PillsDbAdapter.KEY_ALARMS));

		} else {
			dayList.clear();
			System.out.println("ROW ID : " + mRowId);

			PillDay day = new PillDay("Sunday", false);
			dayList.add(day);
			day = new PillDay("Monday", false);
			dayList.add(day);
			day = new PillDay("Tuesday", false);
			dayList.add(day);
			day = new PillDay("Wednesday", false);
			dayList.add(day);
			day = new PillDay("Thursday", false);
			dayList.add(day);
			day = new PillDay("Friday", false);
			dayList.add(day);
			day = new PillDay("Saturday", false);
			dayList.add(day);
			//---------
		}

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {

		super.onSaveInstanceState(outState);
		saveState();

		outState.putSerializable(PillsDbAdapter.KEY_ROWID, mRowId);
	}

	@Override
	protected void onPause() {
		super.onPause();
		saveState();

	}

	@Override
	protected void onResume() {
		super.onResume();
		populateFields();
	}

	/**
	 * Salva el estado antes de pasar a otra actividad
	 */
	private void saveState() {

		if (!backbuttom) {
			String user = mUserText.getText().toString();
			String pill = mPillText.getText().toString();
			String days = mDaysText.getText().toString();
			String hour = hourArrayToString();

			System.out.println("```````````ROW id:" + mRowId);

			if (mRowId == null) {
				if (pill.equals("") || pill == null || days.equals("") || days == null || hour.equals("") || hour == null) {
				} else {
					long id = mDbHelper.createPill(user, pill, days, hour, mAlarms);
					if (id >= 0) {//dibyednu
						mRowId = id;
						System.out.println(user + "****ROW id:" + mRowId + ":" + "pill" + ":" + mAlarms + ":" + days + ":" + hour);
						//mDbHelper.updatePill(mRowId, user, pill, days, hour, mAlarms);

					}
				}
			} else {

				mDbHelper.updatePill(mRowId, user, pill, days, hour, mAlarms);
				System.out.println(user + "-----update ROW id:" + mRowId + ":" + "pill" + ":" + mAlarms + ":" + days + ":" + hour);

			}

		}
	}

	/**
	 * Gestiona los distintos dialogos que pueden crearse, segun su id
	 * 
	 * @param id
	 *            Id del dialogo.
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case TIME_DIALOG_ID:
			return new TimePickerDialog(this, mTimeSetListener, mHour, mMinute, false);
		case DIALOG_MULTIPLE_CHOICE:
			return new AlertDialog.Builder(this).setIcon(R.drawable.ic_popup_reminder).setTitle(R.string.alert_dialog_multi_choice)
					.setMultiChoiceItems(R.array.select_dialog_day, mArrayDays, new DialogInterface.OnMultiChoiceClickListener() {
						public void onClick(DialogInterface dialog, int whichButton, boolean isChecked) {
						}
					}).setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {

							updateDays();
						}
					}).create();
		case DIALOG_REMOVE_TIME:
			mRemoveTime = new boolean[mArrayHours.size()];
			return new AlertDialog.Builder(this)
					.setIcon(R.drawable.ic_popup_reminder)
					.setTitle(R.string.alert_dialog_remove_time)
					.setMultiChoiceItems(mArrayHours.toArray(new CharSequence[mArrayHours.size()]), mRemoveTime,
							new DialogInterface.OnMultiChoiceClickListener() {
								public void onClick(DialogInterface dialog, int whichButton, boolean isChecked) {
								}
							}).setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {

							removeTime(mRemoveTime);
						}
					}).create();
		}
		return null;
	}

	/**
	 * Se le llama antes de crear un dialogo
	 */
	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		switch (id) {
		case TIME_DIALOG_ID:
			((TimePickerDialog) dialog).updateTime(mHour, mMinute);
			break;

		}
	}

	//Listener del TimePiker (atributo!!)
	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mHour = hourOfDay;
			mMinute = minute;
			updateTime(mHour, mMinute);
		}
	};

	private void populatePeopleList() {

		mPeopleList = new ArrayList<String>();
		mPeopleList.clear();

		Cursor people = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, new String[] {}, null, null, null);

		while (people.moveToNext()) {
			String contactName = people.getString(people.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			if (contactName != null)
				mPeopleList.add(contactName);
		}
		people.close();

	}

	//-------------Days.........by Dibyendu 13.07.15

	private void displayListView() {

		myDay.clear();

		//Array list of countries

		//create an ArrayAdaptar from the String Array
		dataAdapter = new MyCustomAdapter(this, R.layout.pill_list_day, dayList);
		ListView listView = (ListView) findViewById(R.id.listView1);
		// Assign adapter to ListView
		listView.setAdapter(dataAdapter);

		listView.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_MOVE) {
					return true; // Indicates that this has been handled by you and will not be forwarded further.
				}
				return false;
			}

		});

		int lisH = getListViewHeight(listView);
		System.out.println("*******DiB*****LIST Hight" + lisH);
		/*
		 * for (int i = 0; i < dataAdapter.getCount(); i++) { View listItem =
		 * dataAdapter.getView(i, null, listView); listItem.measure(0, 0);
		 * 
		 * totalHeight += listItem.getMeasuredHeight();
		 * 
		 * 
		 * totalHeight = totalHeight + (listView.getDividerHeight() *
		 * (dataAdapter.getCount() - 1)+30);
		 * System.out.println("Listview height : "+totalHeight); }
		 * System.out.println("Listview height2 : "+totalHeight);
		 */
		listView.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, lisH));

		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// When clicked, show a toast with the TextView text
				PillDay day = (PillDay) parent.getItemAtPosition(position);
				Toast.makeText(getApplicationContext(), "Clicked on Row: " + day.getday(), Toast.LENGTH_LONG).show();
			}
		});

	}

	//-------------adapter
	private class MyCustomAdapter extends ArrayAdapter<PillDay> {

		private ArrayList<PillDay> dayList;
		private String responseText = "";

		public MyCustomAdapter(Context context, int textViewResourceId, ArrayList<PillDay> dayList) {
			super(context, textViewResourceId, dayList);
			this.dayList = new ArrayList<PillDay>();
			this.dayList.addAll(dayList);
		}

		private class ViewHolder {
			TextView code;
			CheckBox name;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder = null;
			Log.v("ConvertView", String.valueOf(position));

			if (convertView == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = vi.inflate(R.layout.pill_list_day, null);

				holder = new ViewHolder();
				holder.code = (TextView) convertView.findViewById(R.id.code);
				holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
				convertView.setTag(holder);

				holder.name.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						CheckBox cb = (CheckBox) v;
						PillDay day = (PillDay) cb.getTag();
						Toast.makeText(getApplicationContext(), "Clicked on Checkbox: " + cb.getText() + " is " + cb.isChecked(), Toast.LENGTH_LONG).show();
						day.setSelected(cb.isChecked());

						if (cb.isChecked()) {

							if (myDay.contains(cb.getText().toString())) {
							} else {
								myDay.add(cb.getText().toString());
							}

						} else {

							if (myDay.contains(cb.getText().toString())) {
								myDay.remove(cb.getText().toString());
								System.out.println("RE- " + cb.getText().toString());
							} else {
								myDay.remove(cb.getText().toString());
							}
						}

						if (myDay.size() == 0) {

							mDaysText.setText(R.string.no_days);

						} else {
							mDaysText.setText("");
							System.out.println("DAY :" + myDay + myDay.size());
							String myDay1 = myDay.toString().replace("[", "");
							String myDay2 = myDay1.replace("]", "");
							System.out.println("DAY2 :" + myDay2);

							mDaysText.setText(myDay2);

						}

					}
				});

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			PillDay day = dayList.get(position);
			//   holder.code.setText(" (" +  day.getCode() + ")");
			holder.name.setText(day.getday());

			holder.name.setChecked(day.isSelected());
			holder.name.setTag(day);

			return convertView;

		}

	}

	private int getListViewHeight(ListView list) {
		ListAdapter adapter = list.getAdapter();

		int listviewHeight = 0;

		list.measure(MeasureSpec.makeMeasureSpec(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

		listviewHeight = list.getMeasuredHeight() * adapter.getCount() + (adapter.getCount() * list.getDividerHeight());

		return listviewHeight;
	}

	private void shareAppLinkViaFacebook() {
		/*
		 * String urlToShare = "seva60plus.co.in";
		 * 
		 * try { Intent intent1 = new Intent();
		 * intent1.setClassName("com.facebook.katana",
		 * "com.facebook.katana.activity.composer.ImplicitShareIntentHandler");
		 * intent1.setAction("android.intent.action.SEND");
		 * intent1.setType("text/plain");
		 * intent1.putExtra("android.intent.extra.TEXT", urlToShare);
		 * startActivity(intent1); } catch (Exception e) { // If we failed (not
		 * native FB app installed), try share through SEND Intent intent = new
		 * Intent(Intent.ACTION_SEND); intent.setType("text/plain"); String
		 * sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=" +
		 * urlToShare; //String sharerUrl =
		 * "https://www.facebook.com/sharer/sharer.php?u=http://wrctechnologies.com/"
		 * ; intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
		 * startActivity(intent); }
		 */

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
	public void onHomeClick(View v) {
		startActivity(new Intent(PillEdit.this, DashboardActivity.class));
		finish();
	}
}

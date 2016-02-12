package com.seva60plus.hum.pillreminder;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.seva60plus.hum.R;
import com.seva60plus.hum.activity.DashboardActivity;
import com.seva60plus.hum.activity.HumDetailsView;
import com.seva60plus.hum.activity.MenuLay;
import com.seva60plus.hum.activity.PreferencesActivity;
import com.seva60plus.hum.custom.NoInternetPage;
import com.seva60plus.hum.util.ConnectionDetector;
import com.seva60plus.hum.util.Util;

public class TakeThePill extends ListActivity {

	TextView headerTitleText, editDeleteBtn;
	private ImageView back;
	RelativeLayout backBtn;
	Button addPillBtn, settingsBtn;
	RelativeLayout addPillBtnMini;
	Button whatsapp, fb, twitter;
	private static final int ACTIVITY_CREATE = 0;
	private static final int ACTIVITY_EDIT = 1;

	private PillsDbAdapter mDbHelper;

	//PREFS
	public static final String PREFS_NAME = "PrefsFile";
	public static final String EMAIL_KEY = "email";
	public static final String PHONE_KEY = "phone";
	public static final String ALARMS_KEY = "alarms_enabled";

	private static Context ctx;

	/**
	 * Metodo al que se llama al crear la actividad
	 */

	RelativeLayout helpLay;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ctx = this;
		setContentView(R.layout.pills_list);

		headerTitleText = (TextView) findViewById(R.id.header_title);
		headerTitleText.setText("Seva60Plus");
		Typeface font = Typeface.createFromAsset(getAssets(), "openSansBold.ttf");
		headerTitleText.setTypeface(font);

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

		backBtn = (RelativeLayout) findViewById(R.id.back_settings);

		backBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (Util.isInternetAvailable(TakeThePill.this)) {
					Intent i = new Intent(TakeThePill.this, HumDetailsView.class);
					startActivity(i);
					overridePendingTransition(0, 0);
					finish();
				} else {
					Intent i = new Intent(TakeThePill.this, NoInternetPage.class);
					startActivity(i);
					overridePendingTransition(0, 0);
				}

			}
		});

		back = (ImageView) findViewById(R.id.iv_back);

		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				finish();
			}
		});

		findViewById(R.id.llMenu).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				Intent myIntent = new Intent(TakeThePill.this, MenuLay.class);
				startActivity(myIntent);
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

			}
		});

		LinearLayout banner = (LinearLayout) findViewById(R.id.footerLay);

		banner.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.ethnikyarn.com"));
				startActivity(i);

			}
		});

		addPillBtn = (Button) findViewById(R.id.addPill);

		addPillBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				Intent i = new Intent(TakeThePill.this, PillEdit.class);
				startActivityForResult(i, ACTIVITY_CREATE);

			}
		});

		addPillBtnMini = (RelativeLayout) findViewById(R.id.add_pill_reminder);

		addPillBtnMini.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				Intent i = new Intent(TakeThePill.this, PillEdit.class);
				startActivityForResult(i, ACTIVITY_CREATE);
			}
		});

		settingsBtn = (Button) findViewById(R.id.settings);

		settingsBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				Intent i = new Intent(TakeThePill.this, PreferencesActivity.class);
				startActivity(i);

			}
		});

		helpLay = (RelativeLayout) findViewById(R.id.helpLay);
		helpLay.setVisibility(View.VISIBLE);
		helpLay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intObj = new Intent(TakeThePill.this, HelpPillReminderPage.class);
				startActivity(intObj);
			}
		});

		mDbHelper = new PillsDbAdapter(this);
		mDbHelper.open();
		fillData();

		registerForContextMenu(getListView());
	}

	/**
	 * Rellena cada pills_row con informacion de la BBDD
	 */
	private void fillData() {
		Cursor pillsCursor = mDbHelper.fetchAllPills(); //Coge todas las filas de la BBDD
		startManagingCursor(pillsCursor);//android lo gestiona automaticamente

		if (pillsCursor.getCount() == 0) {
			addPillBtn.setVisibility(View.VISIBLE);
			Toast.makeText(getApplicationContext(), "No Pill in database", Toast.LENGTH_LONG).show();
		} else if (pillsCursor.getCount() > 0) {
			addPillBtn.setVisibility(View.INVISIBLE);
		}

		// Create an array to specify the fields we want to display in the list.
		String[] from = new String[] { PillsDbAdapter.KEY_USER, PillsDbAdapter.KEY_PILL, PillsDbAdapter.KEY_DAYS, PillsDbAdapter.KEY_HOUR,

		};

		// and an array of the fields we want to bind those fields to.
		int[] to = new int[] { R.id.user, R.id.pill, R.id.days, R.id.hour };

		// Now create a simple cursor adapter and set it to display.

		// Change Made DibyenduCall

		MyCursorAdapter pills = new MyCursorAdapter(this, R.layout.pills_row, pillsCursor, from, to);

		setListAdapter(pills);

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		final long id = info.id;

		switch (item.getItemId()) {
		case R.id.delete_id:
			System.out.println("Alert-DEl---*****" + id);

			AlertDialog.Builder adb = new AlertDialog.Builder(TakeThePill.this);
			adb.setTitle(getResources().getString(R.string.delete_question));
			adb.setMessage(getResources().getString(R.string.sure_question));
			adb.setNegativeButton(getResources().getString(R.string.alert_dialog_cancel), null);
			adb.setPositiveButton(getResources().getString(R.string.alert_dialog_ok), new AlertDialog.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {

					cancelAlarms(id);
					mDbHelper.deletePill(id);
					fillData();
				}
			});
			adb.show();
			return true;
		case R.id.edit_id:

			System.out.println("Alert-Edit---*****" + id);

			Intent i = new Intent(this, PillEdit.class);
			i.putExtra(PillsDbAdapter.KEY_ROWID, id);
			startActivityForResult(i, ACTIVITY_EDIT);
			return true;
		}

		return super.onContextItemSelected(item);
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
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		/*
		 * Intent i1 = new Intent(this, PillViewActivity.class);
		 * i1.putExtra(PillsDbAdapter.KEY_ROWID, id); startActivity(i1);
		 * overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
		 */

	}

	/**
	 * Metodo por el que se vuelve desde otra actividad.
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		fillData();
	}

	public static boolean getAlarmsEnabled() {
		SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, 0);
		return settings.getBoolean(ALARMS_KEY, true);
	}

	public static String getAppName() {
		return ctx.getResources().getString(R.string.app_name);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mDbHelper.close();
	}

	//*****************DibyenduAdapter---------

	//extend the SimpleCursorAdapter to create a custom class where we 
	//can override the getView to change the row colors
	private class MyCursorAdapter extends SimpleCursorAdapter {

		Context context;
		Cursor c;

		public MyCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {

			super(context, layout, c, from, to);
			this.context = context;
			this.c = c;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			//get reference to the row
			View view = super.getView(position, convertView, parent);

			position = c.getColumnIndex(mDbHelper.KEY_ROWID);
			final int row_id = c.getInt(position);
			//check for odd or even to set alternate colors to the row background

			RelativeLayout editBtn = (RelativeLayout) view.findViewById(R.id.edit_btn);
			RelativeLayout delBtn = (RelativeLayout) view.findViewById(R.id.del_btn);

			editBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					System.out.println("*******EDIT*****" + row_id);
					Intent i = new Intent(getApplicationContext(), PillEdit.class);
					i.putExtra(PillsDbAdapter.KEY_ROWID, (long) row_id); // cast to long from int ROW_ID
					startActivityForResult(i, ACTIVITY_EDIT);

				}
			});

			delBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					System.out.println("-----DEL------" + row_id);
					cancelAlarms(row_id);
					mDbHelper.deletePill(row_id);
					fillData();

				}
			});

			return view;
		}

	}

	//*****************DibyenduAdapter---------END	 

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
		}
	}
	public void onHomeClick(View v) {
		startActivity(new Intent(TakeThePill.this, DashboardActivity.class));
		finish();
	}
}

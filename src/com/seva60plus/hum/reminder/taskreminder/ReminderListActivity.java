package com.seva60plus.hum.reminder.taskreminder;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;

import com.seva60plus.hum.R;
import com.seva60plus.hum.activity.DashboardActivity;
import com.seva60plus.hum.activity.HumDetailsView;
import com.seva60plus.hum.activity.MenuLay;
import com.seva60plus.hum.custom.NoInternetPage;
import com.seva60plus.hum.pillreminder.PillsDbAdapter;
import com.seva60plus.hum.util.ConnectionDetector;
import com.seva60plus.hum.util.Util;

public class ReminderListActivity extends ListActivity {
	private static final int ACTIVITY_CREATE = 0;
	private static final int ACTIVITY_EDIT = 1;

	private PillsDbAdapter mDbHelper;

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

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_reminder_list);
		mDbHelper = new PillsDbAdapter(this);
		mDbHelper.open();
		fillData();
		registerForContextMenu(getListView());

		cd = new ConnectionDetector(getApplicationContext());// by Dibyendu
		lm = (LocationManager) getSystemService(LOCATION_SERVICE);// by Dibyendu

		backBtnSub = (ImageView) findViewById(R.id.iv_back);
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
				Intent intObj = new Intent(ReminderListActivity.this, MenuLay.class);
				startActivity(intObj);
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

				//finish();
			}
		});

		backBtn = (RelativeLayout) findViewById(R.id.back_settings);

		backBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (Util.isInternetAvailable(ReminderListActivity.this)) {

					Intent i = new Intent(ReminderListActivity.this, HumDetailsView.class);
					startActivity(i);
					overridePendingTransition(R.anim.slide_in_right, R.anim.slide_leave_right);
				} else {
					Intent i = new Intent(ReminderListActivity.this, NoInternetPage.class);
					startActivity(i);
					overridePendingTransition(0, 0);
				}

			}
		});

		Button addBtn = (Button) findViewById(R.id.add_btn);
		addBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				createReminder();
			}
		});

	}

	private void fillData() {
		Cursor remindersCursor = mDbHelper.fetchAllReminders();
		startManagingCursor(remindersCursor);

		// Create an array to specify the fields we want to display in the list (only TITLE)
		String[] from = new String[] { PillsDbAdapter.RKEY_TITLE, PillsDbAdapter.RKEY_DATE_TIME };

		// and an array of the fields we want to bind those fields to (in this case just text1)
		int[] to = new int[] { R.id.text1, R.id.days };

		// Now create a simple cursor adapter and set it to display
		/*
		 * SimpleCursorAdapter reminders = new SimpleCursorAdapter(this,
		 * R.layout.reminder_row, remindersCursor, from, to);
		 * setListAdapter(reminders);
		 */

		MyCursorAdapter reminders = new MyCursorAdapter(this, R.layout.new_reminder_row, remindersCursor, from, to);

		setListAdapter(reminders);
	}

	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) {
	 * super.onCreateOptionsMenu(menu); MenuInflater mi = getMenuInflater();
	 * mi.inflate(R.menu.list_menu, menu); return true; }
	 * 
	 * @Override public boolean onMenuItemSelected(int featureId, MenuItem item)
	 * { switch(item.getItemId()) { case R.id.menu_insert: createReminder();
	 * return true; case R.id.menu_settings: Intent i = new Intent(this,
	 * TaskPreferences.class); startActivity(i); return true; }
	 * 
	 * return super.onMenuItemSelected(featureId, item); }
	 * 
	 * @Override public void onCreateContextMenu(ContextMenu menu, View v,
	 * ContextMenuInfo menuInfo) { super.onCreateContextMenu(menu, v, menuInfo);
	 * MenuInflater mi = getMenuInflater();
	 * mi.inflate(R.menu.list_menu_item_longpress, menu); }
	 * 
	 * @Override public boolean onContextItemSelected(MenuItem item) {
	 * switch(item.getItemId()) { case R.id.menu_delete: AdapterContextMenuInfo
	 * info = (AdapterContextMenuInfo) item.getMenuInfo();
	 * mDbHelper.deleteReminder(info.id); fillData(); return true; } return
	 * super.onContextItemSelected(item); }
	 */
	private void createReminder() {
		Intent i = new Intent(this, ReminderEditActivity.class);
		startActivityForResult(i, ACTIVITY_CREATE);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		/*
		 * Intent i = new Intent(this, ReminderEditActivity.class);
		 * i.putExtra(PillsDbAdapter.KEY_ROWID, id); startActivityForResult(i,
		 * ACTIVITY_EDIT);
		 */
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		fillData();
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

			position = c.getColumnIndex(mDbHelper.RKEY_ROWID);
			final int row_id = c.getInt(position);
			//check for odd or even to set alternate colors to the row background

			RelativeLayout editBtn = (RelativeLayout) view.findViewById(R.id.edit_btn);
			RelativeLayout delBtn = (RelativeLayout) view.findViewById(R.id.del_btn);

			editBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					System.out.println("*******EDIT*****" + row_id);
					Intent i = new Intent(getApplicationContext(), ReminderEditActivity.class);
					i.putExtra(PillsDbAdapter.RKEY_ROWID, (long) row_id); // cast to long from int ROW_ID
					startActivityForResult(i, ACTIVITY_EDIT);

				}
			});

			delBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					System.out.println("-----DEL------" + row_id);

					mDbHelper.deleteReminder(row_id);
					fillData();

				}
			});

			return view;
		}

	}

	//*****************DibyenduAdapter---------END	 
	public void onHomeClick(View v) {
		startActivity(new Intent(ReminderListActivity.this, DashboardActivity.class));
		finish();
	}
}

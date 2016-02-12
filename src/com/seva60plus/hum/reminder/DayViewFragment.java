package com.seva60plus.hum.reminder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.seva60plus.hum.R;
import com.seva60plus.hum.activity.DashboardActivity;
import com.seva60plus.hum.activity.HumDetailsView;
import com.seva60plus.hum.activity.MenuLay;
import com.seva60plus.hum.custom.NoInternetPage;
import com.seva60plus.hum.reminder.service.ScheduleClient;
import com.seva60plus.hum.util.Util;

//import android.widget.//Toast;

public class DayViewFragment extends Fragment {

	RelativeLayout backBtn;

	ImageView backBtnSub;

	String remMailId = new String();
	String exisMailId = new String();
	ArrayAdapter<String> dataAdapter;
	int spinnerPosition = 0;

	String mailId;
	EditText editMailId;

	String todayMailRemList = new String();
	ScheduleClient scheduleClient;
	static final int CUSTOM_DIALOG_ID = 0;
	MenuItem wklyDateUpdate;

	CheckBox chkMailTime;
	ArrayList<HashMap<String, String>> completedList, todayRemList, detailedRemList, tmrwRemList, wklyRemList, mnthlyRemList, yrlyRemList;
	DBController db;
	DBDateUpdate dbdu;
	ExpandableListAdapter listAdapter;
	ExpandableListView expListView;
	Button noImage;
	List<String> listDataHeader;
	HashMap<String, List<HashMap<String, String>>> listDataChild;
	String s_remDate;
	SimpleDateFormat fmtDateAndTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	Calendar cal = Calendar.getInstance();

	private void updateLabel() {

		//24 hour format
		int hr = cal.get(Calendar.HOUR_OF_DAY);
		int min = cal.get(Calendar.MINUTE);

		if (hr > 9 || ((hr == 9) && (min > 0))) {
			cal.add(Calendar.DATE, 1);
		}

		cal.set(Calendar.HOUR_OF_DAY, 9);
		cal.set(Calendar.MINUTE, 0);

		cal.set(Calendar.SECOND, 0);//cal.set(Calendar.AM_PM,Calendar.AM);

		s_remDate = fmtDateAndTime.format(cal.getTime());
		//Toast.makeText(getActivity(), "You'll receive an email from : " + s_remDate , //Toast.LENGTH_SHORT).show(); 
	}

	/* Initiating Menu XML file (menu.xml) */
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);

		// inflater.inflate(R.layout.menu, menu);

	}

	public void showAlert(String msg, final int cnt) {
		AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

		alert.setTitle("Alert!! ");
		alert.setMessage(msg);
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

				if (cnt == 1) {
					editMailId.requestFocus();
					getMailIdAndSendNotification();

				}

				//  chkflag =0;
				//errflg =1;
			}
		});

		alert.show();

	}

	/**
	 * Event Handling for Individual menu item selected Identify single menu
	 * item by it's id
	 * */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		return true;
	}

	public int sendSampleMail(String mailId) {
		int rv;
		Mail m = new Mail("suganya.rahulkanna@gmail.com", "nieo5822");

		String mailBody = "Test Mail";

		String[] toArr = { mailId };
		m.setTo(toArr);
		m.setFrom("suganya.rahulkanna@gmail.com");
		m.setSubject("Do It Bubloo!!! Test Mail ...");
		m.setBody(mailBody);

		try {

			if (m.send()) {
				rv = 1;
			} else {
				rv = 0;
			}
		} catch (Exception e) {

			rv = 0;
		}
		return rv;

	}

	public void sendMailNotification(String toMailId) {

		Mail m = new Mail("suganya.rahulkanna@gmail.com", "nieo5822");

		String mailBody = m.getTodayRemInMailBody(getActivity().getApplicationContext());

		if (!(mailBody.length() == 0)) {
			String[] toArr = { toMailId };
			m.setTo(toArr);
			m.setFrom("suganya.rahulkanna@gmail.com");
			m.setSubject("Reminders for today.");
			m.setBody(mailBody);

			try {

				if (m.send()) {
					Log.i("MailApp", "Email was sent successfully.");
					//Toast.makeText(getActivity(), "Email was sent successfully.", //Toast.LENGTH_LONG).show(); 
				} else {
					Log.i("MailApp", "Email was not sent. Reconnect to network");
					//Toast.makeText(getActivity(), "Email was not sent. Reconnect to network", //Toast.LENGTH_LONG).show(); 
				}
			} catch (Exception e) {

				Log.e("MailApp", "Exception: Could not send email- Check the mail Id", e);
				//Toast.makeText(getActivity(), "Check the mail Id", //Toast.LENGTH_LONG).show(); 

			}

		} else {
			Log.i("MailApp", "No reminder for today");
			//Toast.makeText(getActivity(), "No reminder for today", //Toast.LENGTH_LONG).show(); 

		}

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		//remMailIdList.add(0,"Choose one existing mailID to Stop");

		View view = inflater.inflate(R.layout.l_day_view_reminder, container, false);
		expListView = (ExpandableListView) view.findViewById(R.id.lvExp);
		noImage = (Button) view.findViewById(R.id.lvImage);
		backBtn = (RelativeLayout) view.findViewById(R.id.back_settings);

		scheduleClient = new ScheduleClient(getActivity());
		scheduleClient.doBindService();

		db = new DBController(getActivity());

		dbdu = new DBDateUpdate(getActivity().getApplicationContext());
		dbdu.doWklyDateUpdates();
		dbdu.doMonthlyDateUpdates();
		dbdu.doYrlyDateUpdates();

		todayRemList = db.getTodayReminderList();
		tmrwRemList = db.getTmrwReminderList();
		wklyRemList = db.getWklyReminderList();
		mnthlyRemList = db.getMonthlyReminderList();
		yrlyRemList = db.getYrlyReminderList();

		completedList = db.getCompletedReminderList();

		prepareListData();

		listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);

		expListView.setAdapter(listAdapter);

		int c1 = listAdapter.getChildrenCount(0);
		int c2 = listAdapter.getChildrenCount(1);
		int c3 = listAdapter.getChildrenCount(2);
		int c4 = listAdapter.getChildrenCount(3);
		int c5 = listAdapter.getChildrenCount(4);
		int c6 = listAdapter.getChildrenCount(5);

		if (c1 != 0 || c2 != 0 || c3 != 0 || c4 != 0 || c5 != 0 || c6 != 0) {

			noImage.setVisibility(View.INVISIBLE);
			expListView.setVisibility(View.VISIBLE);

		} else {

			noImage.setVisibility(View.VISIBLE);
			expListView.setVisibility(View.INVISIBLE);

		}

		System.out.println("LIST LENGTH: " + c6);

		// Listview on child click listener
		expListView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

				//Toast.makeText(getActivity(), "child clk "  , //Toast.LENGTH_SHORT).show(); 

				TextView remId = (TextView) v.findViewById(R.id.remId);
				String valremId = remId.getText().toString();
				Intent objIndent = new Intent(getActivity(), EditReminder.class);
				objIndent.putExtra("remId", valremId);
				startActivity(objIndent);
				return true;
			}
		});

		backBtnSub = (ImageView) view.findViewById(R.id.iv_back);
		backBtnSub.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getActivity().finish();
				getActivity().overridePendingTransition(0, 0);
			}
		});

		view.findViewById(R.id.llMenu).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intObj = new Intent(getActivity(), MenuLay.class);
				startActivity(intObj);
				getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

				//finish();
			}
		});

		backBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (Util.isInternetAvailable(getActivity())) {
					Intent i = new Intent(getActivity(), HumDetailsView.class);
					startActivity(i);
					getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_leave_right);
				} else {
					Intent i = new Intent(getActivity(), NoInternetPage.class);
					startActivity(i);
					getActivity().overridePendingTransition(0, 0);
				}
			}
		});

		Button addImg = (Button) view.findViewById(R.id.addImg);
		addImg.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), NewReminder.class);
				startActivity(intent);
			}
		});

		

		return view;
	}

	public void getMailIdAndSendNotification() {
		int iList = 0;

		final String s_remType = "Mail";

		final Spinner spinExisMailId;

		int listSize;
		final List<String> remIdList = new ArrayList<String>();
		final List<String> remMailIdList = new ArrayList<String>();
		AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

		alert.setTitle("To get Reminder Mail @ 9 AM everyday");

		LayoutInflater inflater = getActivity().getLayoutInflater();

		View v = inflater.inflate(R.layout.custom_dialog_mail_reminder, null);
		int setStopOff = 0;
		alert.setView(v);

		editMailId = (EditText) v.findViewById(R.id.editMailId);

		spinExisMailId = (Spinner) v.findViewById(R.id.exisMailIds);

		ArrayList<HashMap<String, String>> dbMailList = db.getMailIdList();

		listSize = dbMailList.size();
		Log.i("dbMaillist size", Integer.toString(listSize));

		if (listSize != 0) {
			while (iList < listSize) {
				remIdList.add(dbMailList.get(iList).get("remId"));

				remMailIdList.add(dbMailList.get(iList).get("shortDesc"));
				Log.i("exis mail id", dbMailList.get(iList).get("shortDesc"));
				iList++;
			}

			dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, remMailIdList);
			dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinExisMailId.setAdapter(dataAdapter);

			dataAdapter.notifyDataSetChanged();

			spinExisMailId.setVisibility(View.VISIBLE);
			setStopOff = 0;
			// ad.getButton(DialogInterface.BUTTON_NEUTRAL).setEnabled(false);
		} else {
			setStopOff = 1;
			spinExisMailId.setVisibility(View.GONE);
		}

		alert.setPositiveButton("Start", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

				mailId = (editMailId.getText()).toString();

				if (mailId.length() != 0) {
					HashMap<String, String> chkMailList = db.checkForMailIdInDB(mailId);

					if (chkMailList.size() != 0) {
						Log.i("chkMaillist mailid", chkMailList.get("shortDesc"));
						showAlert("Mail ID already exists in Notification List", 1);
						//getMailIdAndSendNotification();
					} else {

						int validchk = sendSampleMail(mailId);
						if (validchk == 1) {
							updateLabel();
							HashMap<String, String> queryValues = new HashMap<String, String>();
							queryValues.put("shortDesc", mailId);
							queryValues.put("detailedDesc", "");
							queryValues.put("remDate", "");

							queryValues.put("remType", "Mail");

							db.insertReminder(queryValues);

							String remId = db.getLastInsertId();

							scheduleClient.setAlarmAndNotification(cal, remId, "Mail", mailId);

						} else
							showAlert("Enter Valid mail ID pls", 1);

					}
				} else
					showAlert("Enter New Id in text box", 1);

			}
		});

		alert.setNeutralButton("Stop", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// Canceled.

				if (spinExisMailId.getVisibility() == View.VISIBLE) {
					exisMailId = spinExisMailId.getSelectedItem().toString();
					spinnerPosition = dataAdapter.getPosition(exisMailId);
					//Toast.makeText(getActivity(), "rem spin pos " + Integer.toString(spinnerPosition), //Toast.LENGTH_SHORT).show(); 

				}

				remMailId = remIdList.get(spinnerPosition);
				//Toast.makeText(getActivity(), "rem MailId " + remMailId, //Toast.LENGTH_SHORT).show(); 
				db.deleteReminderInfo(remMailId);

				scheduleClient.cancelAlarmAndNotification(remMailId, "Mail");
				remMailIdList.remove(exisMailId);
				dataAdapter.notifyDataSetChanged();

			}
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// Canceled.

			}
		});

		AlertDialog ad = alert.create();
		ad.show();
		if (setStopOff == 1)
			ad.getButton(DialogInterface.BUTTON_NEUTRAL).setEnabled(false);
		else
			ad.getButton(DialogInterface.BUTTON_NEUTRAL).setEnabled(true);

	}

	/*
	 * Preparing the list data
	 */
	private void prepareListData() {

		listDataHeader = new ArrayList<String>();

		listDataChild = new HashMap<String, List<HashMap<String, String>>>();

		// Adding child data
		listDataHeader.add("Today");
		listDataHeader.add("Tomorrow");
		listDataHeader.add("This Week");
		listDataHeader.add("This Month");
		listDataHeader.add("This Year");

		listDataHeader.add("Completed Items");

		listDataChild.put(listDataHeader.get(0), todayRemList); // Header, Child data
		listDataChild.put(listDataHeader.get(1), tmrwRemList);
		listDataChild.put(listDataHeader.get(2), wklyRemList);
		listDataChild.put(listDataHeader.get(3), mnthlyRemList);
		listDataChild.put(listDataHeader.get(4), yrlyRemList);
		listDataChild.put(listDataHeader.get(5), completedList);

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}
	public void onHomeClick(View v) {
		startActivity(new Intent(getActivity(), DashboardActivity.class));
		getActivity().finish();
	}
}
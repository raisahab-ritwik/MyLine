package com.seva60plus.hum.pillreminder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

public class RepeatingAlarm extends BroadcastReceiver {
	String ns = Context.NOTIFICATION_SERVICE;
	NotificationManager mNotificationManager;
	private static final int HELLO_ID = 1;

	//BBDD
	private Long mRowId;
	private PillsDbAdapter mDbHelper;

	String getRowID = "";

	@Override
	public void onReceive(Context context, Intent intent) {

		mDbHelper = new PillsDbAdapter(context);
		mDbHelper.open();

		try {
			System.out.println("==========RECEIVED****");

			String weekday_name = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(System.currentTimeMillis());

			Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
			Date currentLocalTime = cal.getTime();
			DateFormat date = new SimpleDateFormat("HH:mm");
			String localTime = date.format(currentLocalTime);

			Cursor pillcursor = mDbHelper.fetchAllPillsHours();
			int totalRow = pillcursor.getCount();

			// looping through all rows and adding to list
			if (pillcursor.moveToFirst()) {
				do {

					String idRow = pillcursor.getString(0);
					String idRowHour = pillcursor.getString(1);
					String idRowDay = pillcursor.getString(2);
					System.out.println("@" + weekday_name + "#####" + idRow + ":" + idRowHour + ":" + idRowDay);

					if (idRowDay.contains(weekday_name)) {

						if (idRowHour.contains(localTime)) {

							getRowID = idRow;
							System.out.println("!!!!!----GET ID---" + getRowID);
						} else {
						}
					} else {
					}

				} while (pillcursor.moveToNext());
			}

			System.out.println("*********RECEIVED****" + localTime + " :" + totalRow + ":" + getRowID);

		} catch (Exception e) {
		}

		PillWakeReminderIntentService.acquireStaticLock(context);

		Intent i = new Intent(context, PillReminderService.class);
		i.putExtra("rowID", getRowID);
		context.startService(i);

	}
}

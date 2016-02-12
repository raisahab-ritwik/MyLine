package com.seva60plus.hum.network;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.format.Time;

import com.seva60plus.hum.R;
import com.seva60plus.hum.wellbeing.WellBeingActivityExercise;
import com.seva60plus.hum.wellbeing.WellBeingActivityMood;
import com.seva60plus.hum.wellbeing.WellBeingActivitySleep;
import com.seva60plus.hum.wellbeing.WellbeingExReminderAlert;
import com.seva60plus.hum.wellbeing.WellbeingMoodReminderAlert;
import com.seva60plus.hum.wellbeing.WellbeingWaReminderAlert;

public class PostDataStatices extends BroadcastReceiver {
	private final String SOMEACTION = "com.seva60Plus.alarm.ACTION";
	int YOURAPP_NOTIFICATION_ID = 1234567890;
	NotificationManager mNotificationManager;

	@Override
	public void onReceive(Context context, Intent intent) {

		mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		String action = intent.getAction();

		if (SOMEACTION.equals(action)) {

			Time time = new Time();
			time.setToNow();

			String tm = Integer.toString(time.hour) + Integer.toString(time.minute) + Integer.toString(time.second);

			System.out.println("Postime: " + tm);
			// MainActivity1.sendSms();
			// time 11:10:10
			if (tm.contains("11101")) {
				System.out.println("Postime:ALmo " + tm);
				showNotificationMood(context, R.drawable.icon, "short", false);
			}// time 17:10:10
			else if (tm.contains("17101")) {
				System.out.println("Postime:AL Ex" + tm);
				showNotificationExr(context, R.drawable.icon, "short", false);
			}// time 10:10:10
			else if (tm.contains("10101")) {
				System.out.println("Postime:ALWa " + tm);
				showNotificationSleep(context, R.drawable.icon, "short", false);
			} else {
				System.out.println("Postime:AL ===" + tm);
			}
		}

	}

	private void showNotificationExr(Context context, int statusBarIconID, String string, boolean showIconOnly) {
		// This is who should be launched if the user selects our notification.
		Intent contentIntent = new Intent(context, WellBeingActivityExercise.class);

		Intent targetIntent = new Intent(context, WellbeingExReminderAlert.class);
		targetIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		// choose the ticker text
		String tickerText = "Seva60Plus";

		Notification n = new Notification(R.drawable.icon, "Seva60Plus", System.currentTimeMillis());

		PendingIntent appIntent = PendingIntent.getActivity(context, 0, contentIntent, 0);

		n.setLatestEventInfo(context, "WELBING", "DID YOU EXERCISE TODAY ?", appIntent);

		// n.sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
		// "://com.seva60plus.seva60plus/raw/ex_sound");
		n.defaults |= Notification.DEFAULT_LIGHTS;
		n.flags |= Notification.FLAG_AUTO_CANCEL;
		n.flags |= Notification.FLAG_INSISTENT;

		long[] vibrate = { 0, 500, 500, 500 };
		n.vibrate = vibrate;

		// mNotificationManager.notify(YOURAPP_NOTIFICATION_ID, n);

		context.startActivity(targetIntent);

	}

	private void showNotificationMood(Context context, int statusBarIconID, String string, boolean showIconOnly) {
		// This is who should be launched if the user selects our notification.
		Intent contentIntent = new Intent(context, WellBeingActivityMood.class);

		Intent targetIntent = new Intent(context, WellbeingMoodReminderAlert.class);

		targetIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		// choose the ticker text
		String tickerText = "Seva60Plus";

		Notification n = new Notification(R.drawable.icon, "Seva60Plus", System.currentTimeMillis());

		PendingIntent appIntent = PendingIntent.getActivity(context, 0, contentIntent, 0);

		n.setLatestEventInfo(context, "WELBING", "HOW IS YOUR MOOD NOW ?", appIntent);

		// n.sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
		// "://com.seva60plus.seva60plus/raw/mood_sound");
		n.defaults |= Notification.DEFAULT_LIGHTS;
		n.flags |= Notification.FLAG_AUTO_CANCEL;
		n.flags |= Notification.FLAG_INSISTENT;

		long[] vibrate = { 0, 500, 500, 500 };
		n.vibrate = vibrate;

		// mNotificationManager.notify(YOURAPP_NOTIFICATION_ID, n);

		context.startActivity(targetIntent);

	}

	private void showNotificationSleep(Context context, int statusBarIconID, String string, boolean showIconOnly) {
		// This is who should be launched if the user selects our notification.
		Intent contentIntent = new Intent(context, WellBeingActivitySleep.class);

		Intent targetIntent = new Intent(context, WellbeingWaReminderAlert.class);

		targetIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		// choose the ticker text
		String tickerText = "Seva60Plus";

		Notification n = new Notification(R.drawable.icon, "Seva60Plus", System.currentTimeMillis());

		PendingIntent appIntent = PendingIntent.getActivity(context, 0, contentIntent, 0);

		n.setLatestEventInfo(context, "WELBING", "HOW MANY HOUR DID YOU SLEEP TONIGHT?", appIntent);

		// n.sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
		// "://com.seva60plus.seva60plus/raw/water_sound");
		n.defaults |= Notification.DEFAULT_LIGHTS;
		n.flags |= Notification.FLAG_AUTO_CANCEL;
		n.flags |= Notification.FLAG_INSISTENT;

		long[] vibrate = { 0, 500, 500, 500 };
		n.vibrate = vibrate;

		// mNotificationManager.notify(YOURAPP_NOTIFICATION_ID, n);
		context.startActivity(targetIntent);
	}

}
package com.seva60plus.hum.reminder.taskreminder;


import com.seva60plus.hum.R;
import com.seva60plus.hum.pillreminder.PillsDbAdapter;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

public class ReminderService extends WakeReminderIntentService {

	public ReminderService() {
		super("ReminderService");
			}

	@Override
	void doReminderWork(Intent intent) {
		Log.d("ReminderService", "Doing work.");
		Long rowId = intent.getExtras().getLong(PillsDbAdapter.RKEY_ROWID);
		 
		
		
		//NotificationManager mgr = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
			
		Intent targetIntent = new Intent(this, ReminderAlert.class);
		targetIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		targetIntent.putExtra(PillsDbAdapter.RKEY_ROWID, rowId); 
		
		Intent notificationIntent = new Intent(this, ReminderEditActivity.class); 
		
		PendingIntent pi = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_ONE_SHOT); 
		
		Notification note=new Notification(R.drawable.icon, getString(R.string.notify_new_task_message), System.currentTimeMillis());
		note.setLatestEventInfo(this, getString(R.string.notify_new_task_title), getString(R.string.notify_new_task_message), pi);
		note.defaults |= Notification.DEFAULT_SOUND; 
		note.flags |= Notification.FLAG_AUTO_CANCEL; 
		
		// An issue could occur if user ever enters over 2,147,483,647 tasks. (Max int value). 
		// I highly doubt this will ever happen. But is good to note. 
		int id = (int)((long)rowId);
		//mgr.notify(id, note);  
		
		this.startActivity(targetIntent);
		
	}
}

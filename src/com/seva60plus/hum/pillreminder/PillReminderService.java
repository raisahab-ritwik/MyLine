package com.seva60plus.hum.pillreminder;


import android.content.Intent;
import android.util.Log;

public class PillReminderService extends PillWakeReminderIntentService {

	public PillReminderService() {
		super("ReminderService");
			}

	@Override
	void doReminderWork(Intent intent) {
		Log.d("ReminderService", "Doing work.");
		
		String rowID = intent.getStringExtra("rowID");
		
		System.out.println("```"+rowID);
		
		Intent targetIntent = new Intent(this,PillReminderAlert.class);
		targetIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		targetIntent.putExtra("rowID", rowID);
		this.startActivity(targetIntent);
		
	}
}

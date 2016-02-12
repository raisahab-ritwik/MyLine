package com.seva60plus.hum.alarmnotification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StartService extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		Intent myInt = new Intent(context, NotificationAlarmService.class);
		context.stopService(myInt);
		context.startService(myInt);

	}

}

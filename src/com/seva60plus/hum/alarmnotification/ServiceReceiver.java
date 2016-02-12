package com.seva60plus.hum.alarmnotification;
/* close for changae SUMANGAL
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorEvent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

public class ServiceReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		SensorsMonitor sensorMonitor = new SensorsMonitor(context);
		MutePhoneStateListener phoneListener = new MutePhoneStateListener(context, sensorMonitor);
		TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
	    telephony.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
		
	/*	SensorsMonitor sensorMonitor = new SensorsMonitor(context);
		sensorMonitor.onSensorChanged(s);
		*/
/* close for changae SUMANGAL	}

} */

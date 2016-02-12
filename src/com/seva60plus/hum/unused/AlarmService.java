package com.seva60plus.hum.unused;


import com.seva60plus.hum.wellbeing.WellBeingActivityExercise;

import android.R;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.text.format.Time;
import android.util.Log;

public class AlarmService extends IntentService {
    private NotificationManager alarmNotificationManager;

    public AlarmService() {
        super("AlarmService");
    }

    @Override
    public void onHandleIntent(Intent intent) {
        sendNotification("Wake Up! Wake Up!");
    }

    private void sendNotification(String msg) {
        Log.d("AlarmService", "Preparing to send notification...: " + msg);
        alarmNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Time time = new Time(); 
		time.setToNow();
		
		String tm = Integer.toString(time.hour) + Integer.toString(time.minute);
		
		System.out.println("ServicePostime: " + tm);
        
        alarmNotificationEx();
       /* PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, WelbingActivityExercise.class), 0);

        NotificationCompat.Builder alamNotificationBuilder = new NotificationCompat.Builder(
                this).setContentTitle("Alarm").setSmallIcon(R.drawable.ic_lock_idle_alarm)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentText(msg);


        alamNotificationBuilder.setContentIntent(contentIntent);
        alarmNotificationManager.notify(1, alamNotificationBuilder.build());
        Log.d("AlarmService", "Notification sent.");*/
    }
    
    public void  alarmNotificationEx() {
		
    	 PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                 new Intent(this, WellBeingActivityExercise.class), 0);

         NotificationCompat.Builder alamNotificationBuilder = new NotificationCompat.Builder(
                 this).setContentTitle("Alarm").setSmallIcon(R.drawable.ic_lock_idle_alarm)
                 .setStyle(new NotificationCompat.BigTextStyle().bigText("Ex"))
                 .setContentText("Ex");


         alamNotificationBuilder.setContentIntent(contentIntent);
         alarmNotificationManager.notify(1, alamNotificationBuilder.build());
         Log.d("AlarmService", "Notification sent.");
         
	}
}
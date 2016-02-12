package com.seva60plus.hum.reminder.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import android.R;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.seva60plus.hum.reminder.DBController;
import com.seva60plus.hum.reminder.EditReminder;
import com.seva60plus.hum.reminder.MainActivity;


/**
 * This service is started when an Alarm has been raised
 * 
 * We pop a notification into the status bar for the user to click on
 * When the user clicks the notification a new activity is opened
 * 
 * @author paul.blundell
 */
public class NotifyService extends Service {

	DBController controller = new DBController(this);
	/**
	 * Class for clients to access
	 */
	public class ServiceBinder extends Binder {
		NotifyService getService() {
			return NotifyService.this;
		}
	}

	// Unique id to identify the notification.
	//private static final int NOTIFICATION = 123;
	private static int NOTIFICATION ;
	// Name of an intent extra we can use to identify if this service was started to create a notification	
	public static final String INTENT_NOTIFY = "com.example.expexamp.service.INTENT_NOTIFY";
	// The system notification manager
	private NotificationManager mNM;
	
	 ScheduleClient scheduleClient;

	@Override
	public void onCreate() {
		Log.i("NotifyService", "onCreate()");
		NOTIFICATION = ( int) System.currentTimeMillis();
		mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		  scheduleClient = new ScheduleClient(this);
		    scheduleClient.doBindService();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i("LocalService", "Received start id " + startId + ": " + intent);
		
		
		String remId = intent.getStringExtra("remId");
		
		String s_curDateTime;
		  SimpleDateFormat fmtDateAndTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		
		  Calendar cal = Calendar.getInstance();  
		  cal.set(Calendar.SECOND, 0);
		  
		  s_curDateTime = fmtDateAndTime.format(cal.getTime());
		
			Log.i("notify remID", remId);
		
	    String shortDesc = new String();
	    
	    HashMap<String , String> remList = controller.getReminderInfo(remId);
	  
	    if(remList.size()!=0) 
	    {
	      shortDesc = remList.get("shortDesc");
	      String remType = remList.get("remType");
	      String s_remDate = remList.get("remDate");
	      
	      if( remType.equals("Yearly") || remType.equals("Monthly"))
	      {
	    	  if(! s_remDate.equals(s_curDateTime))
	    	  {
	    		  remId = "-" + remId;
	    		  String s_remType = "Tally";
	    		  try {
	    	    	  cal.setTime(fmtDateAndTime.parse(s_remDate));
	    	    	  
	    	      }
	    	      catch(java.text.ParseException e){
	    	    	  e.printStackTrace();
	    	      }
	    		  scheduleClient.setAlarmAndNotification(cal,remId,s_remType);
	    		  
	    	  }
	    	  
	      }
	      else
	      {
	      
	      
	      
	   // If this service was started by out AlarmTask intent then we want to show our notification
			if(intent.getBooleanExtra(INTENT_NOTIFY, false))
				showNotification(remId,shortDesc);
			
	      }
	    }
	    else
	    {

	  	   AlertDialog.Builder alert = new AlertDialog.Builder(this);
	  	   	   
	  	   alert.setTitle("Alert!! ");
	  	   alert.setMessage("Oops !!! The reminder was deleted by u already");
	  	   alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	  	 	   public void onClick(DialogInterface dialog, int whichButton) {
	  	 		 Intent objIntent = new Intent(getApplicationContext(), MainActivity.class);
	  	 	    startActivity(objIntent);
	  	 	  
	  	 	   }
	  	 	   });
	  	 	
	  	  
	  	 	   
	        	 alert.show();
	        	
	    }
	      
		
		onStop();
		// We don't care if this service is stopped as we have already delivered our notification
		return START_NOT_STICKY;
	}

	  
	  public void onStop() {
	  	// When our activity is stopped ensure we also stop the connection to the service
	  	// this stops us leaking our activity into the system *bad*
	  	if(scheduleClient != null)
	  		scheduleClient.doUnbindService();
	  	
	 // Stop the service when we are finished
	 		stopSelf();
	   
	  }
	 
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	// This is the object that receives interactions from clients
	private final IBinder mBinder = new ServiceBinder();

	/**
	 * Creates a notification and shows it in the OS drag-down status bar
	 */
	private void showNotification(String remId, String shortDesc) {
		// This is the 'title' of the notification
		CharSequence title = "Alarm!!";
		// This is the icon to use on the notification
		int icon = R.drawable.ic_dialog_alert;
		// This is the scrolling text of the notification
		CharSequence text = shortDesc ;
		//CharSequence text = shortDesc;
		// What time to show on the notification
		long time = System.currentTimeMillis();
		
//		Notification notification = new Notification(icon, text, time);

		 Intent  objIndent = new Intent(getApplicationContext(),EditReminder.class);
		    objIndent.putExtra("remId", remId);
		    Log.i("notify to edit remID", remId);
		    //startActivity(objIndent); 
		
		// The PendingIntent to launch our activity if the user selects this notification
		PendingIntent contentIntent = PendingIntent.getActivity(this, NOTIFICATION ,objIndent, PendingIntent.FLAG_UPDATE_CURRENT);

		// Set the info for the views that show in the notification panel.
		//
		//notification.setLatestEventInfo(this, title, text, contentIntent);
	//	notification.setContentIntent(contentIntent);
		
		Notification notification = new Notification.Builder(this).setContentTitle(title)
				.setContentText(text)
				.setSmallIcon(icon)
				.setContentIntent(contentIntent)
				.build();
		
		// Clear the notification when it is pressed
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		
		// Send the notification to the system.
		mNM.notify(NOTIFICATION, notification);
		
		
	}
}
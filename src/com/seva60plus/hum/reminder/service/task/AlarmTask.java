package com.seva60plus.hum.reminder.service.task;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;



import com.seva60plus.hum.reminder.service.MailService;
import com.seva60plus.hum.reminder.service.NotifyService;

/**
 * Set an alarm for the date passed into the constructor
 * When the alarm is raised it will start the NotifyService
 * 
 * This uses the android build in alarm manager *NOTE* if the phone is turned off this alarm will be cancelled
 * 
 * This will run on it's own thread.
 * 
 * @author paul.blundell
 */
public class AlarmTask implements Runnable{
	// The date selected for the alarm
	private  Calendar date;
	// The android system alarm manager
	private final AlarmManager am;
	// Your context to retrieve the alarm manager from
	private final Context context;
	
	private  String remId;
	
	private  String remType;
	
	private  String mailId;

	public AlarmTask(Context context, Calendar date , String remId, String remType) {
		this.context = context;
		this.am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		this.date = date;
		this.remId = remId;
		this.remType = remType;
	}
	
	public AlarmTask(Context context, Calendar date , String remId, String remType,String mailId) {
		this.context = context;
		this.am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		this.date = date;
		this.remId = remId;
		this.remType = remType;
		this.mailId=mailId;
	}
	//cancel mail
	public AlarmTask(Context context, String remId,String remType) {
		this.context = context;
		this.am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		this.remType = remType;
		this.remId = remId;
	}
	//cancel non mail
	public AlarmTask(Context context, String remId ) {
		this.context = context;
		this.am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		 this.remType="";
		this.remId = remId;
	}
	
	
	
	
	
	@Override
	public void run() {
		
		if(remType.equals("Mail"))
		{
			// Request to start are service when the alarm date is upon us
			// We don't start an activity as we just want to pop up a notification into the system bar not a full activity
			Intent intent = new Intent(context, MailService.class);
			
			
		    intent.putExtra("toMailId", mailId); 
			Log.i("Alrm task", "MailId= " + mailId);
			  
	         
	         
			PendingIntent pendingIntent = PendingIntent.getService(context,Integer.parseInt(remId), intent, PendingIntent.FLAG_UPDATE_CURRENT);
			
			// Sets an alarm - note this alarm will be lost if the phone is turned off and on again
			am.setRepeating(AlarmManager.RTC_WAKEUP, date.getTimeInMillis(),24*60*60*1000, pendingIntent);
			//am.set(AlarmManager.RTC_WAKEUP, date.getTimeInMillis(), pendingIntent);

		}
		else
		{
			// Request to start are service when the alarm date is upon us
			// We don't start an activity as we just want to pop up a notification into the system bar not a full activity
			Intent intent = new Intent(context, NotifyService.class);
			intent.putExtra(NotifyService.INTENT_NOTIFY, true);
			
		    intent.putExtra("remId", remId); 
			Log.i("Alrm task", "remId= " + remId);
			  
	         
	         
			PendingIntent pendingIntent = PendingIntent.getService(context,Integer.parseInt(remId), intent, PendingIntent.FLAG_UPDATE_CURRENT);
			if(remType.equals("Tally"))
				am.set(AlarmManager.RTC_WAKEUP, date.getTimeInMillis(), pendingIntent); 
			else if(remType.equals("Monthly"))
				am.setRepeating(AlarmManager.RTC_WAKEUP, date.getTimeInMillis(),28*24*60*60*1000, pendingIntent);
			else if(remType.equals("Weekly"))
				am.setRepeating(AlarmManager.RTC_WAKEUP, date.getTimeInMillis(),7*24*60*60*1000, pendingIntent);
			else if(remType.equals("Yearly"))
				am.setRepeating(AlarmManager.RTC_WAKEUP, date.getTimeInMillis(),365*24*60*60*1000, pendingIntent);
			else
				am.setRepeating(AlarmManager.RTC_WAKEUP, date.getTimeInMillis(),24*60*60*1000, pendingIntent);
		}
	}
	
	public void runcancel() {
		// Request to start are service when the alarm date is upon us
		// We don't start an activity as we just want to pop up a notification into the system bar not a full activity
		if(remType.equals("Mail"))
		{
			
			Intent intent = new Intent(context, MailService.class);
			
			
		    intent.putExtra("toMailId", remId); 
			Log.i("Alrm task", "MailId= " + remId);
			     
			
			PendingIntent pendingIntent = PendingIntent.getService(context,Integer.parseInt(remId), intent, PendingIntent.FLAG_UPDATE_CURRENT);
			am.cancel(pendingIntent);
			
		}
		else
		{
			Intent intent = new Intent(context, NotifyService.class);
			intent.putExtra(NotifyService.INTENT_NOTIFY, true);
			
		    intent.putExtra("remId", remId); 
			Log.i("Alrm task", "remId= " + remId);
			  
	         
	         
			PendingIntent pendingIntent = PendingIntent.getService(context,Integer.parseInt(remId), intent, PendingIntent.FLAG_UPDATE_CURRENT);
			
			// Sets an alarm - note this alarm will be lost if the phone is turned off and on again
			am.cancel(pendingIntent);
			//am.setRepeating(AlarmManager.RTC, date.getTimeInMillis(),1000*5, pendingIntent);
			
		}
	}
	
	
	
}

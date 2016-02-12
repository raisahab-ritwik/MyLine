package com.seva60plus.hum.reminder.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
//import android.widget.//Toast;

import com.seva60plus.hum.reminder.DBController;
import com.seva60plus.hum.reminder.Mail;
import com.seva60plus.hum.reminder.service.task.AlarmTask;

public class MailService extends Service {

	/**
	 * Class for clients to access
	 */
	public class ServiceBinder extends Binder {
		MailService getService() {
			return MailService.this;
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i("MailService", "Received start id " + startId + ": " + intent);
		
	String toMailId = intent.getStringExtra("toMailId");
		
		Log.i("Mail Service MailID", toMailId);
		
		sendMailNotification(toMailId);
		
		// We want this service to continue running until it is explicitly stopped, so return sticky.
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	// This is the object that receives interactions from clients. See
	private final IBinder mBinder = new ServiceBinder();

	
	
	
	 public void sendMailNotification(String toMailId)
	   {
		
		   
		   Mail m = new Mail("suganya.rahulkanna@gmail.com", "nieo5822"); 
		   
		   String mailBody = m.getTodayRemInMailBody(this);
		   
		     if (mailBody != " ")
		     {
		         String[] toArr = {toMailId}; 
		         m.setTo(toArr); 
		         m.setFrom("suganya.rahulkanna@gmail.com"); 
		         m.setSubject("Reminders for today."); 
		         m.setBody(mailBody); 
		         
		        
		         
		       
		         
		    
		         try { 
		          // m.addAttachment("/sdcard/filelocation"); 
		    
		           if(m.send()) { 
		             ////Toast.makeText(getApplicationContext(), "Email was sent successfully.", ////Toast.LENGTH_LONG).show(); 
		           } else { 
		             ////Toast.makeText(getApplicationContext(), "Email was not sent.", ////Toast.LENGTH_LONG).show(); 
		           } 
		         } catch(Exception e) { 
		           //////Toast.makeText(MailApp.this, "There was a problem sending the email.", ////Toast.LENGTH_LONG).show(); 
		           Log.e("MailApp", "Could not send email", e); 
		         } 
		    	
		     }
		     else 
		     {
		    	Log.i("Reg Mail", "No reminder for today"); 
		     } 
		   
		     stopSelf();
	   }
	
	
}
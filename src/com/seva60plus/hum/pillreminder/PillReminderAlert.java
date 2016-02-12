
package com.seva60plus.hum.pillreminder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.seva60plus.hum.R;
import com.seva60plus.hum.R.id;
import com.seva60plus.hum.R.layout;
import com.seva60plus.hum.R.raw;
import com.seva60plus.hum.pillreminder.PillsDbAdapter;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class PillReminderAlert extends Activity{
    
	Long getRowID;
	 MediaPlayer mPill;
	private PillsDbAdapter mDbHelper;
	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd kk:mm:ss";
	
	TextView mTitleText;
	 String mainTime="";
	
	 String getPill = "", getHour = "";
	 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pill_reminder_alart);
        
        mDbHelper = new PillsDbAdapter(this);
		mDbHelper.open(); 
        
        mTitleText = (TextView)findViewById(R.id.textView2);
        
       
        mPill = MediaPlayer.create(this, R.raw.good_morning);
       
        mPill.setLooping(true);
        mPill.start();
        
        Intent intent = getIntent();
        String rowID = intent.getStringExtra("rowID");
        
        getRowID = Long.valueOf(rowID).longValue();
        System.out.println("!!!"+rowID);
        
        try {
			
        	Cursor pillreminder = mDbHelper.fetchPill(getRowID);
			startManagingCursor(pillreminder);
        	
			String getDay = pillreminder.getString(3);
			getPill = pillreminder.getString(2);
			getHour = pillreminder.getString(4);
			
        	
        	System.out.println("////*-*-*-=");
        	/*
			Cursor pillreminder2 = mDbHelper.fetchPillAlert("Tuesday");
			startManagingCursor(pillreminder2);
			
			String mGetPill2 = pillreminder2.getString(1);
			*/
			
			System.out.println("*-*-*-==ALERT DAY: "+" : "+getPill);
        	
		} catch (Exception e) {
			// TODO: handle exception
		}
        
        
        mTitleText.setText("Medicine: "+getPill+"\n"+"Time: "+getHour);
        
      /*  
        Intent intent = getIntent();
        rowId = intent.getExtras().getLong(PillsDbAdapter.RKEY_ROWID);
        
        mDbHelper = new PillsDbAdapter(this);
		mDbHelper.open(); 
       
		try {
			
		
			Cursor reminder = mDbHelper.fetchReminder(rowId);
			startManagingCursor(reminder);
			mTitleText.setText("Task : "+reminder.getString(
					reminder.getColumnIndexOrThrow(PillsDbAdapter.RKEY_TITLE))+"\n");
			
			// Get the date from the database and format it for our use. 
			SimpleDateFormat dateTimeFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
			Date date = null;
			try {
				String dateString = reminder.getString(reminder.getColumnIndexOrThrow(PillsDbAdapter.RKEY_DATE_TIME)); 
				date = dateTimeFormat.parse(dateString);
				//mTitleText.append("Date : "+date.toString()); 
				
				// Thu Jan 01 17:34:25 IST 1970//
				//----------------------------------------    
				//----------------------------------------    
                String getDate = date.toString();
                System.out.println("----MAinDate :"+getDate);
                
                String cutDate = getDate.substring(0, 10);
                System.out.println("----CUT DATE :"+cutDate);
                
                String cutTime = getDate.substring(11, getDate.indexOf("I"));
                System.out.println("----CUT TIME :"+cutTime);
                
                String cutYear = getDate.substring(getDate.indexOf("T")+1,getDate.length());
                System.out.println("----CUT Year :"+cutYear);
                
                String mainDate = cutDate+","+cutYear;
                System.out.println("----CUT Full Date :"+mainDate);
                
                //-------------------------------------
                //-------------------------------------
                
               
                try {
                     SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
                     Date dateObj = sdf.parse(cutTime);
                    System.out.println(dateObj);
                    System.out.println("$$$ AM-PM: "+new SimpleDateFormat("K:mm aa").format(dateObj));
                    mainTime = new SimpleDateFormat("K:mm aa").format(dateObj);
                    
                   
                } catch (final ParseException e) {
                    e.printStackTrace();
                }
                
               
                mTitleText.append("Date : "+mainDate);
                mTitleText.append("\nTime : "+mainTime);
                
				
			} catch (ParseException e) {
				Log.e("ReminderEditActivity", e.getMessage(), e); 
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
        */
        Button okBtn = (Button)findViewById(R.id.ok_btn);
        okBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mPill.stop();
				finish();
			}
		});
        
    }
    

}

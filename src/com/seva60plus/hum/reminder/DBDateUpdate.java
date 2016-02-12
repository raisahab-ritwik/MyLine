package com.seva60plus.hum.reminder;




import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.seva60plus.hum.reminder.DBController;

public class DBDateUpdate {

	DBController db ;
	 
	Date curDate, tblDate;
	
	String Weekly = "Weekly",Yearly = "Yearly",Monthly="Monthly"; 
	
	String scdate = new String();
SimpleDateFormat fmtDateAndTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 

SimpleDateFormat fmtDate = new SimpleDateFormat("yyyy-MM-dd"); 
Calendar myCalendar;

	 
	public DBDateUpdate(Context c)
	{
		 myCalendar = Calendar.getInstance();
	    scdate = fmtDate.format(myCalendar.getTime());
		
		
		try 
		{
			curDate = fmtDate.parse(scdate);
		 }
	    catch(java.text.ParseException e){
	  	  e.printStackTrace();
	    }
		 db = new DBController(c);
	}
	

	
	
	
	
	
	
		


	
	
	
	 public void doWklyDateUpdates()
	   {
		 ArrayList<HashMap<String, String>> weeklyRemList;
		 int iList=0;
		 weeklyRemList = db.getRemDatesList(Weekly);
		 String nwDate = new String();
		   int listSize = weeklyRemList.size();
			
			
		   if( listSize != 0 )
		   {
			   while(iList  < listSize )
			   {
				   String remDate = weeklyRemList.get(iList).get("remDate");
				   String remId = weeklyRemList.get(iList).get("remId");
				   Log.i("DB Upd Service", " remID_Date " + remId + ": " + remDate);
				   nwDate = calcNxtDate(remDate,Weekly);
				   if(nwDate.length() != 0)
					   updateDBDate(remId,nwDate);
				   iList++;
			   }
			   
		   }
		   
		     
	   }
	 
	
	 public void updateDBDate(String remId,String nWkDate)
	 {
		 
		 HashMap<String, String> queryValues =  new  HashMap<String, String>();
		  
		  
		    queryValues.put("remId", remId);
		   
		    
		    queryValues.put("remDate", nWkDate);
		    queryValues.put("remDone", "NO");
		    db.updateDate(queryValues);
		    
		    
          	

		    
		    
		 
	 }
	 
	 public void doMonthlyDateUpdates()
	   {
		 ArrayList<HashMap<String, String>> monthlyRemList;
		 int iList=0;
		 String nwDate = new String();
		 monthlyRemList = db.getRemDatesList(Monthly);
		 
		   int listSize = monthlyRemList.size();
			
			
		   if( listSize != 0 )
		   {
			   while(iList  < listSize )
			   {
				   String remDate = monthlyRemList.get(iList).get("remDate");
				   String remId = monthlyRemList.get(iList).get("remId");
				   Log.i("DB mon Upd Service", " remID_Date " + remId + ": " + remDate);
				   nwDate = calcNxtDate(remDate, Monthly);
				   if(nwDate.length() != 0)
					   updateDBDate(remId,nwDate);
				   iList++;
			   }
			   
		   }
		   
		     
	   }
	 
	
	 public void doYrlyDateUpdates()
	   {
		 ArrayList<HashMap<String, String>> monthlyRemList;
		 int iList=0;
		 String nwDate = new String();
		 monthlyRemList = db.getRemDatesList(Yearly);
		 
		   int listSize = monthlyRemList.size();
			
			
		   if( listSize != 0 )
		   {
			   while(iList  < listSize )
			   {
				   String remDate = monthlyRemList.get(iList).get("remDate");
				   String remId = monthlyRemList.get(iList).get("remId");
				   Log.i("DB mon Upd Service", " remID_Date " + remId + ": " + remDate);
				   nwDate = calcNxtDate(remDate,Yearly);
				   if(nwDate.length() != 0)
					   updateDBDate(remId,nwDate);
				   iList++;
			   }
			   
		   }
		   
		     
	   }
	 
	 public String calcNxtDate(String dbDate, String type)
	 {
		 String nwDate = new String();
	      try {
	    	  // db to cal
	    	  myCalendar.setTime(fmtDateAndTime.parse(dbDate));
	    	  
	    		// cal to str
	    		String sdbdate = fmtDate.format(myCalendar.getTime());
	    		
	    		//str to date
	    		try 
	    		{
	    			tblDate = fmtDate.parse(sdbdate);
	    		 }
	    	    catch(java.text.ParseException e){
	    	  	  e.printStackTrace();
	    	    }
	    		
	    		Log.i("cur date",scdate);
	    		Log.i("db date", sdbdate);
	    		
	    	  if(tblDate.before(curDate) )
	    	  { 
	    		  
	    		  if(type.equals(Yearly))
	       			  myCalendar.add(Calendar.YEAR, 1);
	    			   
	    		  else if(type.equals(Monthly))
	    			  myCalendar.add(Calendar.MONTH, 1);
	    			 
	    		  else if(type.equals(Weekly))
	    			  myCalendar.add(Calendar.DATE, 7);
	    		  
	    		  
	    		   
	    		  nwDate = fmtDateAndTime.format(myCalendar.getTime());
	    		  
	    	  }
	    	  
	    	  
	    	  
	      }
	      catch(java.text.ParseException e){
	    	  e.printStackTrace();
	      }
	      Log.i("nw date",nwDate);
	      return nwDate;
	 }
	
	
}
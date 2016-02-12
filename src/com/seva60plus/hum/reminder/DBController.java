package com.seva60plus.hum.reminder;



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
 
import android.util.Log;
 
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
 
public class DBController  extends SQLiteOpenHelper {
  private static final String LOGCAT = null;
 
  public DBController(Context applicationcontext) {
    super(applicationcontext, "doitbubloo.db", null, 1);
    Log.d(LOGCAT,"database Created");
  }
 
 
  
  @Override
  public void onCreate(SQLiteDatabase database) {
    String query;
    query = "CREATE TABLE doitbublootable ( remId INTEGER PRIMARY KEY AUTOINCREMENT, remType TEXT, remDate DATETIME, shortDesc TEXT, detailedDesc TEXT,remDone TEXT,doneDate DATETIME)";
    database.execSQL(query);
    Log.d(LOGCAT,"table Created");
  }
  @Override
  public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {
    String query;
    query = "DROP TABLE IF EXISTS doitbublootable";
    database.execSQL(query);
    onCreate(database);
  }
 
  public void insertReminder(HashMap<String, String> queryValues) {
	  
	  	String s_remDate;
    	  SimpleDateFormat fmtDateAndTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
    		
    		Calendar cal = Calendar.getInstance();
	  cal.add(Calendar.DATE, -1);
	  
	  s_remDate = fmtDateAndTime.format(cal.getTime());
	  
	  
    SQLiteDatabase database = this.getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put("remType", queryValues.get("remType"));
    values.put("remDate", queryValues.get("remDate"));
    values.put("shortDesc", queryValues.get("shortDesc"));
    values.put("detailedDesc", queryValues.get("detailedDesc"));
    values.put("remDone", "NO");
    values.put("doneDate", s_remDate);
    database.insert("doitbublootable", null, values);
    database.close();
  }
 
  
  public void updateReminderInfo(HashMap<String, String> queryValues) {
    SQLiteDatabase database = this.getWritableDatabase();  
    ContentValues values = new ContentValues();
    values.put("remType", queryValues.get("remType"));
    values.put("remDate", queryValues.get("remDate"));
    values.put("shortDesc", queryValues.get("shortDesc"));
    values.put("detailedDesc", queryValues.get("detailedDesc"));
    database.update("doitbublootable", values, "remId" + " = ?", new String[] { queryValues.get("remId") });
    database.close();
  }
  
  
  public void updateDoneFlds(HashMap<String, String> queryValues) {
	    SQLiteDatabase database = this.getWritableDatabase();  
	    ContentValues values = new ContentValues();
	    
	    values.put("remDone", queryValues.get("remDone"));
	    values.put("doneDate", queryValues.get("doneDate"));
	 
	    database.update("doitbublootable", values, "remId" + " = ?", new String[] { queryValues.get("remId") });
	    database.close();
	    //Log.i("dbcontrol","done flds updated");
	   printRec(queryValues.get("remId"));
	    
  }
  
  public void printRec(String id) {
	  Log.i("print remId",id);
	    HashMap<String, String> wordList = new HashMap<String, String>();
	    SQLiteDatabase database = this.getReadableDatabase();
	    String selectQuery = "SELECT * FROM doitbublootable where remId='"+id+"'";
	    Cursor cursor = database.rawQuery(selectQuery, null);
	    if (cursor.moveToFirst()) {
	      do {
	    	   
	         Log.i("remType =", cursor.getString(1));
	    	  Log.i("remDate=", cursor.getString(2));
	    	  Log.i("shortDesc=", cursor.getString(3));
	    	  Log.i("detailedDesc=", cursor.getString(4));
	    	  Log.i("remDone=", cursor.getString(5));
	    	  Log.i("doneDate=", cursor.getString(6));
	      } while (cursor.moveToNext());
	    }           
	     
	  } 
  
  public void updateDate(HashMap<String, String> queryValues) {
    SQLiteDatabase database = this.getWritableDatabase();  
    ContentValues values = new ContentValues();
    
    values.put("remDate", queryValues.get("remDate"));
    values.put("remDone", queryValues.get("remDone"));
   
    database.update("doitbublootable", values, "remId" + " = ?", new String[] { queryValues.get("remId") });
    database.close();
  }
 
  public void deleteReminderInfo(String id) {
    Log.d(LOGCAT,"delete");
    SQLiteDatabase database = this.getWritableDatabase();  
    String deleteQuery = "DELETE FROM  doitbublootable where remId='"+ id +"'";
    Log.d("query",deleteQuery);   
    database.execSQL(deleteQuery);
    database.close();
  }
 
  public ArrayList<HashMap<String, String>> getAllReminderList() {
    ArrayList<HashMap<String, String>> wordList;
    wordList = new ArrayList<HashMap<String, String>>();
  
    String selectQuery = "SELECT remId, shortDesc FROM doitbublootable " ;
    SQLiteDatabase database = this.getReadableDatabase();
    Cursor cursor = database.rawQuery(selectQuery, null);
    if (cursor.moveToFirst()) {
      do {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("remId", cursor.getString(0));
        map.put("shortDesc", cursor.getString(1));
        
        wordList.add(map);
      } while (cursor.moveToNext());
    }
    return wordList;
  }
  
  public ArrayList<HashMap<String, String>> getMailIdList() {
	    ArrayList<HashMap<String, String>> wordList;
	    wordList = new ArrayList<HashMap<String, String>>();
	  
	    String selectQuery = "SELECT remId, shortDesc FROM doitbublootable WHERE remType = 'Mail' " ;
	    SQLiteDatabase database = this.getReadableDatabase();
	    Cursor cursor = database.rawQuery(selectQuery, null);
	    if (cursor.moveToFirst()) {
	      do {
	        HashMap<String, String> map = new HashMap<String, String>();
	        map.put("remId", cursor.getString(0));
	        map.put("shortDesc", cursor.getString(1));
	        
	        wordList.add(map);
	      } while (cursor.moveToNext());
	    }
	    return wordList;
	  }
  
  public ArrayList<HashMap<String, String>> getDailyReminderList() {
	    ArrayList<HashMap<String, String>> wordList;
	    wordList = new ArrayList<HashMap<String, String>>();
	   
	    String selectQuery = "SELECT remId, shortDesc FROM doitbublootable where remType = 'Daily' " ;
	    SQLiteDatabase database = this.getReadableDatabase();
	    Cursor cursor = database.rawQuery(selectQuery, null);
	    if (cursor.moveToFirst()) {
	      do {
	        HashMap<String, String> map = new HashMap<String, String>();
	        map.put("remId", cursor.getString(0));
	        map.put("shortDesc", cursor.getString(1));
	        wordList.add(map);
	      } while (cursor.moveToNext());
	    }
	    return wordList;
	  }
 
  public ArrayList<HashMap<String, String>> getNoRepeatReminderList() {
	    ArrayList<HashMap<String, String>> wordList;
	    wordList = new ArrayList<HashMap<String, String>>();
	   
	    String selectQuery = "SELECT remId, shortDesc FROM doitbublootable where remType = 'No Repeat'  " ;
	    SQLiteDatabase database = this.getReadableDatabase();
	    Cursor cursor = database.rawQuery(selectQuery, null);
	    if (cursor.moveToFirst()) {
	      do {
	        HashMap<String, String> map = new HashMap<String, String>();
	        map.put("remId", cursor.getString(0));
	        map.put("shortDesc", cursor.getString(1));
	        wordList.add(map);
	      } while (cursor.moveToNext());
	    }
	    return wordList;
	  }
  
  public ArrayList<HashMap<String, String>> getWeeklyReminderList() {
	    ArrayList<HashMap<String, String>> wordList;
	    wordList = new ArrayList<HashMap<String, String>>();
	 
	    String selectQuery = "SELECT remId, shortDesc FROM doitbublootable where remType = 'Weekly' " ;
	    SQLiteDatabase database = this.getReadableDatabase();
	    Cursor cursor = database.rawQuery(selectQuery, null);
	    if (cursor.moveToFirst()) {
	      do {
	        HashMap<String, String> map = new HashMap<String, String>();
	        map.put("remId", cursor.getString(0));
	        map.put("shortDesc", cursor.getString(1));
	        wordList.add(map);
	      } while (cursor.moveToNext());
	    }
	    return wordList;
	  }
 
  public ArrayList<HashMap<String, String>> getRemDatesList(String remType) {
	    ArrayList<HashMap<String, String>> wordList;
	    wordList = new ArrayList<HashMap<String, String>>();
	   
	    String selectQuery = "SELECT remId,remDate FROM doitbublootable where remType = '" + remType +"'" ;
	    SQLiteDatabase database = this.getReadableDatabase();
	    Cursor cursor = database.rawQuery(selectQuery, null);
	    if (cursor.moveToFirst()) {
	      do {
	        HashMap<String, String> map = new HashMap<String, String>();
	        map.put("remId", cursor.getString(0));
	        map.put("remDate", cursor.getString(1));
	         wordList.add(map);
	      } while (cursor.moveToNext());
	    }
	    return wordList;
	  }
  
 

  
  public ArrayList<HashMap<String, String>> getMonthlyTypeReminderList() {
	    ArrayList<HashMap<String, String>> wordList;
	    wordList = new ArrayList<HashMap<String, String>>();
	
	    String selectQuery = "SELECT remId, shortDesc FROM doitbublootable where remType = 'Monthly' " ;
	    SQLiteDatabase database = this.getReadableDatabase();
	    Cursor cursor = database.rawQuery(selectQuery, null);
	    if (cursor.moveToFirst()) {
	      do {
	        HashMap<String, String> map = new HashMap<String, String>();
	        map.put("remId", cursor.getString(0));
	        map.put("shortDesc", cursor.getString(1));
	        wordList.add(map);
	      } while (cursor.moveToNext());
	    }
	    return wordList;
	  }
  
  public ArrayList<HashMap<String, String>> getYearlyReminderList() {
	    ArrayList<HashMap<String, String>> wordList;
	    wordList = new ArrayList<HashMap<String, String>>();
	 
	    String selectQuery = "SELECT remId, shortDesc FROM doitbublootable where remType = 'Yearly' " ;
	    SQLiteDatabase database = this.getReadableDatabase();
	    Cursor cursor = database.rawQuery(selectQuery, null);
	    if (cursor.moveToFirst()) {
	      do {
	        HashMap<String, String> map = new HashMap<String, String>();
	        map.put("remId", cursor.getString(0));
	        map.put("shortDesc", cursor.getString(1));
	        wordList.add(map);
	      } while (cursor.moveToNext());
	    }
	    return wordList;
	  }
  
  public ArrayList<HashMap<String, String>> getTodayReminderList() {
	    ArrayList<HashMap<String, String>> wordList;
	    wordList = new ArrayList<HashMap<String, String>>();
	  //  String selectQuery = "SELECT remId, shortDesc FROM doitbublootable where (( remType =  'No Repeat' or remType = 'Weekly' )and DATE(remDate, 'localtime' ) = DATE('now','localtime')) or ( remType = 'Daily' ) or ( remType = 'Monthly' and strftime('%d', remDate ) == strftime('%d', 'now'))  or ( remType = 'Yearly' and strftime('%d', remDate ) == strftime('%d', 'now') and strftime('%m', remDate ) == strftime('%m', 'now' ) ) " ;
	//    String selectQuery = "SELECT remId, shortDesc FROM doitbublootable where (( remType = 'Weekly' or remType = 'Monthly' or remType = 'Yearly' ) and  DATE(remDate, 'localtime' ) = DATE('now','localtime')) or ( ( remType =  'No Repeat' or remType = 'Daily' ) and ( DATE(remDate, 'localtime' ) <= DATE('now','localtime') ) )";
	    String selectQuery = "SELECT remId, shortDesc FROM doitbublootable where ( remType =  'No Repeat' and DATE(remDate, 'localtime' ) <= DATE('now','localtime') and remDone = 'NO' ) or ( remType =  'Daily' and DATE(remDate, 'localtime' ) <= DATE('now','localtime') and DATE(doneDate, 'localtime' ) < DATE('now','localtime')) or (( remType = 'Weekly' or remType = 'Monthly' or remType = 'Yearly' ) and  DATE(remDate, 'localtime' ) <= DATE('now','localtime') and remDone = 'NO') ";	     SQLiteDatabase database = this.getReadableDatabase();
	    Cursor cursor = database.rawQuery(selectQuery, null);
	    if (cursor.moveToFirst()) {
	      do {
	        HashMap<String, String> map = new HashMap<String, String>();
	        map.put("remId", cursor.getString(0));
	        map.put("shortDesc", cursor.getString(1));
	        wordList.add(map);
	      } while (cursor.moveToNext());
	    }
	    return wordList;
	  }
  
  public ArrayList<HashMap<String, String>> getCompletedReminderList() {
	    ArrayList<HashMap<String, String>> wordList;
	    wordList = new ArrayList<HashMap<String, String>>();
	  //  String selectQuery = "SELECT remId, shortDesc FROM doitbublootable where (( remType =  'No Repeat' or remType = 'Weekly' )and DATE(remDate, 'localtime' ) = DATE('now','localtime')) or ( remType = 'Daily' ) or ( remType = 'Monthly' and strftime('%d', remDate ) == strftime('%d', 'now'))  or ( remType = 'Yearly' and strftime('%d', remDate ) == strftime('%d', 'now') and strftime('%m', remDate ) == strftime('%m', 'now' ) ) " ;
	//    String selectQuery = "SELECT remId, shortDesc FROM doitbublootable where (( remType = 'Weekly' or remType = 'Monthly' or remType = 'Yearly' ) and  DATE(remDate, 'localtime' ) = DATE('now','localtime')) or ( ( remType =  'No Repeat' or remType = 'Daily' ) and ( DATE(remDate, 'localtime' ) <= DATE('now','localtime') ) )";
	   
	   // String selectQuery = "SELECT remId, shortDesc FROM doitbublootable where (( ( remType = 'Weekly' or remType = 'Monthly' or remType = 'Yearly' or 'No Repeat' ) or (remType = 'Daily' and DATE(doneDate, 'localtime') = DATE('now','localtime') ) ) and remDone = 'YES') "; 
	    Log.i("dbcontrol","comp list");
	   //String selectQuery = "SELECT remId, shortDesc FROM doitbublootable where remDone = 'YES' and ( ( remType = 'Weekly' or remType = 'Monthly' or remType = 'Yearly' or 'No Repeat' ) or (remType = 'Daily' and DATE(doneDate, 'localtime') = DATE('now','localtime')) )";
	    
	    String selectQuery = "SELECT remId, shortDesc FROM doitbublootable where remDone = 'YES' ";
	    SQLiteDatabase database = this.getReadableDatabase();
	    Cursor cursor = database.rawQuery(selectQuery, null);
	    if (cursor.moveToFirst()) {
	      do {
	        HashMap<String, String> map = new HashMap<String, String>();
	        map.put("remId", cursor.getString(0));
	        map.put("shortDesc", cursor.getString(1));
	        wordList.add(map);
	        
	        Log.i("comp remId =", cursor.getString(0));
	    	  
	    	  Log.i("comp shortDesc=", cursor.getString(1));
	    	  
	    	  
	      } while (cursor.moveToNext());
	    }
	    return wordList;
	  }

  
  public ArrayList<HashMap<String, String>> getTmrwReminderList() {
	    ArrayList<HashMap<String, String>> wordList;
	    wordList = new ArrayList<HashMap<String, String>>();
	    
	   //2 String selectQuery = "SELECT remId, shortDesc FROM doitbublootable where DATE(remDate, 'localtime' ) = DATE('now' , 'localtime','+1 day' ) " ;
	  //  String selectQuery = "SELECT remId, shortDesc FROM doitbublootable where ( ( remType =  'No Repeat' or remType = 'Weekly' ) and DATE(remDate, 'localtime' ) = DATE('now','localtime' , '+1 day')) or ( remType = 'Daily' ) or ( remType = 'Monthly' and strftime('%d', remDate ) == strftime('%d', 'now', '+1 day'))   or ( remType = 'Yearly' and strftime('%d', remDate ) == strftime('%d', 'now','+1 day') and strftime('%m', remDate ) == strftime('%m', 'now','+1 day' ) ) " ;
	  //3  String selectQuery = "SELECT remId, shortDesc FROM doitbublootable where DATE(remDate, 'localtime' ) = DATE('now' , 'localtime','+1 day' ) and (( remType = 'Daily' and DATE(doneDate, 'localtime' ) < DATE('now','localtime','+1 day') ) or ( ( remType = 'Weekly' or remType = 'Monthly' or remType = 'Yearly' or remType = 'No Repeat' ) and remDone = 'NO' ) )  " ;
	    
	    String selectQuery = "SELECT remId, shortDesc FROM doitbublootable where DATE(remDate, 'localtime' ) = DATE('now' , 'localtime','+1 day' ) and remDone = 'NO'   " ;	  
	    SQLiteDatabase database = this.getReadableDatabase();
	    Cursor cursor = database.rawQuery(selectQuery, null);
	    if (cursor.moveToFirst()) {
	      do {
	        HashMap<String, String> map = new HashMap<String, String>();
	        map.put("remId", cursor.getString(0));
	        map.put("shortDesc", cursor.getString(1));
	        wordList.add(map);
	      } while (cursor.moveToNext());
	    }
	    return wordList;
	  }
	 
  public ArrayList<HashMap<String, String>> getWklyReminderList() {
	    ArrayList<HashMap<String, String>> wordList;
	    wordList = new ArrayList<HashMap<String, String>>();
	    
	  // String selectQuery = "SELECT remId, shortDesc FROM doitbublootable where ( ( remType =  'No Repeat' or remType = 'Weekly' or remType = 'Monthly' or remType = 'Yearly') and ( strftime('%W', remDate ) == strftime('%W', 'now' ) ) or ( remType = 'Monthly' and strftime('%d%w', remDate ) == strftime('%d%w', 'now'))  or ( remType = 'Yearly' and strftime('%d', remDate ) == strftime('%d', 'now','+1 day') and strftime('%m', remDate ) == strftime('%m', 'now','+1 day' ) ) " ;
	    String selectQuery = "SELECT remId, shortDesc FROM doitbublootable where  ( strftime('%W', remDate ) == strftime('%W', 'now' ) ) and ( DATE(remDate, 'localtime' ) > DATE('now' , 'localtime','+1 day' )) and remDone = 'NO'";	    
	    
	    SQLiteDatabase database =this.getReadableDatabase();
	    Cursor cursor = database.rawQuery(selectQuery, null);
	    if (cursor.moveToFirst()) {
	      do {
	        HashMap<String, String> map = new HashMap<String, String>();
	        map.put("remId", cursor.getString(0));
	        map.put("shortDesc", cursor.getString(1));
	        wordList.add(map);
	      } while (cursor.moveToNext());
	    }
	    return wordList;
	  }
  public ArrayList<HashMap<String, String>> getMonthlyReminderList() {
	    ArrayList<HashMap<String, String>> wordList;
	    wordList = new ArrayList<HashMap<String, String>>();
	    //Integer i = Integer.parseInt("1");
	   //String selectQuery = "SELECT remId, shortDesc FROM doitbublootable where remDate= DATE('now') " ;
	    String selectQuery = "SELECT remId, shortDesc FROM doitbublootable where ( strftime('%m', remDate ) == strftime('%m', 'now' ) ) and ( strftime('%W', remDate ) > strftime('%W', 'now' ) ) and remDone = 'NO'  ";  
	    SQLiteDatabase database = this.getReadableDatabase();
	    Cursor cursor = database.rawQuery(selectQuery, null);
	    if (cursor.moveToFirst()) {
	      do {
	        HashMap<String, String> map = new HashMap<String, String>();
	        map.put("remId", cursor.getString(0));
	        map.put("shortDesc", cursor.getString(1));
	        wordList.add(map);
	      } while (cursor.moveToNext());
	    }
	    return wordList;
	  }
  
  public ArrayList<HashMap<String, String>> getYrlyReminderList() {
	    ArrayList<HashMap<String, String>> wordList;
	    wordList = new ArrayList<HashMap<String, String>>();
	    //Integer i = Integer.parseInt("1");
	   //String selectQuery = "SELECT remId, shortDesc FROM doitbublootable where remDate= DATE('now') " ;
	    String selectQuery = "SELECT remId, shortDesc FROM doitbublootable where ( strftime('%Y', remDate ) == strftime('%Y', 'now' ) ) and ( strftime('%m', remDate ) > strftime('%m', 'now' )  ) and remDone = 'NO'  ";  
	    SQLiteDatabase database = this.getReadableDatabase();
	    Cursor cursor = database.rawQuery(selectQuery, null);
	    if (cursor.moveToFirst()) {
	      do {
	        HashMap<String, String> map = new HashMap<String, String>();
	        map.put("remId", cursor.getString(0));
	        map.put("shortDesc", cursor.getString(1));
	        wordList.add(map);
	      } while (cursor.moveToNext());
	    }
	    return wordList;
	  }

  public HashMap<String, String> getReminderInfo(String id) {
    HashMap<String, String> wordList = new HashMap<String, String>();
    SQLiteDatabase database = this.getReadableDatabase();
    String selectQuery = "SELECT * FROM doitbublootable where remId='"+id+"'";
    Cursor cursor = database.rawQuery(selectQuery, null);
    if (cursor.moveToFirst()) {
      do {
        wordList.put("remType", cursor.getString(1));
        wordList.put("remDate", cursor.getString(2));
        wordList.put("shortDesc", cursor.getString(3));
        wordList.put("detailedDesc", cursor.getString(4));
        wordList.put("remDone", cursor.getString(5));
        wordList.put("doneDate", cursor.getString(6));
      } while (cursor.moveToNext());
    }           
    return wordList;
  } 
  
  public HashMap<String, String> checkForMailIdInDB(String mailId) {
	    HashMap<String, String> wordList = new HashMap<String, String>();
	    SQLiteDatabase database = this.getReadableDatabase();
	    String selectQuery = "SELECT remId FROM doitbublootable where shortDesc='"+mailId+"' and remType = 'Mail' " ;
	    Cursor cursor = database.rawQuery(selectQuery, null);
	    if (cursor.moveToFirst()) {
	      do {
	        wordList.put("remId", cursor.getString(0));
	        
	      } while (cursor.moveToNext());
	    }           
	    return wordList;
	  } 


  public ArrayList<HashMap<String, String>> getDetailedReminderForToday() {
	    ArrayList<HashMap<String, String>> wordList;
	    wordList = new ArrayList<HashMap<String, String>>();
	    String selectQuery = "SELECT shortDesc , detailedDesc FROM doitbublootable where (( remType =  'No Repeat' or remType = 'Weekly' or remType = 'Monthly' or remType = 'Yearly' ) and  DATE(remDate, 'localtime' ) = DATE('now','localtime')) or ( remType = 'Daily' )";
	    SQLiteDatabase database = this.getReadableDatabase();
	    Cursor cursor = database.rawQuery(selectQuery, null);
	    if (cursor.moveToFirst()) {
	      do {
	        HashMap<String, String> map = new HashMap<String, String>();
	        map.put("shortDesc", cursor.getString(0));
	        map.put("detailedDesc", cursor.getString(1));
	        wordList.add(map);
	      } while (cursor.moveToNext());
	    }
	    return wordList;
	  }

public String getLastInsertId() {

	int i_row_id = -1;
	String row_id = new String();
    String selectQuery = "SELECT MAX(remId) FROM doitbublootable " ;
    SQLiteDatabase database = this.getReadableDatabase();
    Cursor cursor = database.rawQuery(selectQuery, null);
    if (cursor.moveToFirst()) {
    
          i_row_id = cursor.getInt(0);
   
    }
    
    row_id=String.valueOf(i_row_id);
    Log.d(LOGCAT,"Max(remId) = "+row_id);
    return row_id;
  }


}


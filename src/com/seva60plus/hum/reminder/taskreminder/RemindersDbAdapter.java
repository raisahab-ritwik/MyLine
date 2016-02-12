
package com.seva60plus.hum.reminder.taskreminder;

import com.seva60plus.hum.pillreminder.PillsDbAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Simple reminder database access helper class. 
 * Defines the basic CRUD operations (Create, Read, Update, Delete)
 * for the example, and gives the ability to list all reminders as well as
 * retrieve or modify a specific reminder.
 * 
 */
public class RemindersDbAdapter {

	//
	// Databsae Related Constants
	//
	private static final String RDATABASE_NAME = "data";
    private static final String RDATABASE_TABLE = "reminders";
    private static final int RDATABASE_VERSION = 4;
    
	public static final String RKEY_TITLE = "title";
    public static final String RKEY_BODY = "body";
    public static final String RKEY_DATE_TIME = "reminder_date_time"; 
    public static final String RKEY_ROWID = "_id";
    
    
    private static final String RTAG = "ReminderDbAdapter";
    private DatabaseHelper RmDbHelper;
    private SQLiteDatabase RmDb;
    
    /**
     * Database creation SQL statement
     */
    private static final String RDATABASE_CREATE =
            "create table " + RDATABASE_TABLE + " ("
            		+ RKEY_ROWID + " integer primary key autoincrement, "
                    + RKEY_TITLE + " text not null, " 
                    + RKEY_BODY + " text not null, " 
                    + RKEY_DATE_TIME + " text not null);"; 

    

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, RDATABASE_NAME, null, RDATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(RDATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(RTAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + RDATABASE_TABLE);
            onCreate(db);
        }
    }

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     * 
     * @param ctx the Context within which to work
     */
    public RemindersDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Open the database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     * 
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public RemindersDbAdapter open() throws SQLException {
        RmDbHelper = new DatabaseHelper(mCtx);
        RmDb = RmDbHelper.getWritableDatabase();
        return this;
    }
    
    public void close() {
        RmDbHelper.close();
    }


    /**
     * Create a new reminder using the title, body and reminder date time provided. 
     * If the reminder is  successfully created return the new rowId
     * for that reminder, otherwise return a -1 to indicate failure.
     * 
     * @param title the title of the reminder
     * @param body the body of the reminder
     * @param reminderDateTime the date and time the reminder should remind the user
     * @return rowId or -1 if failed
     */
    public long createReminder(String title, String body, String reminderDateTime) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(RKEY_TITLE, title);
        initialValues.put(RKEY_BODY, body);
        initialValues.put(RKEY_DATE_TIME, reminderDateTime); 

        return RmDb.insert(RDATABASE_TABLE, null, initialValues);
    }

    /**
     * Delete the reminder with the given rowId
     * 
     * @param rowId id of reminder to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteReminder(long rowId) {

        return RmDb.delete(RDATABASE_TABLE, RKEY_ROWID + "=" + rowId, null) > 0;
    }

    /**
     * Return a Cursor over the list of all reminders in the database
     * 
     * @return Cursor over all reminders
     */
    public Cursor fetchAllReminders() {

        return RmDb.query(RDATABASE_TABLE, new String[] {RKEY_ROWID, RKEY_TITLE,
                RKEY_BODY, RKEY_DATE_TIME}, null, null, null, null, null);
    }

    /**
     * Return a Cursor positioned at the reminder that matches the given rowId
     * 
     * @param rowId id of reminder to retrieve
     * @return Cursor positioned to matching reminder, if found
     * @throws SQLException if reminder could not be found/retrieved
     */
    public Cursor fetchReminder(long rowId) throws SQLException {

        Cursor mCursor =

                RmDb.query(true, RDATABASE_TABLE, new String[] {RKEY_ROWID,
                        RKEY_TITLE, RKEY_BODY, RKEY_DATE_TIME}, RKEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    /**
     * Update the reminder using the details provided. The reminder to be updated is
     * specified using the rowId, and it is altered to use the title, body and reminder date time
     * values passed in
     * 
     * @param rowId id of reminder to update
     * @param title value to set reminder title to
     * @param body value to set reminder body to
     * @param reminderDateTime value to set the reminder time. 
     * @return true if the reminder was successfully updated, false otherwise
     */
    public boolean updateReminder(long rowId, String title, String body, String reminderDateTime) {
        ContentValues args = new ContentValues();
        args.put(RKEY_TITLE, title);
        args.put(RKEY_BODY, body);
        args.put(RKEY_DATE_TIME, reminderDateTime);

        return RmDb.update(RDATABASE_TABLE, args, RKEY_ROWID + "=" + rowId, null) > 0;
    }
}

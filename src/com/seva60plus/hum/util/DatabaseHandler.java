package com.seva60plus.hum.util;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	//private static final String DATABASE_NAME = "sevadata";
	private static final String DATABASE_NAME = Environment.getExternalStorageDirectory() + "/Seva60Plus/sevadata";
	// Contacts table name
	private static final String TABLE_DETAILS = "sevadetails";

	// Contacts Table Columns names

	private static final String KEY_ID = "id";
	private static final String KEY_DATE = "date";
	private static final String KEY_TIME = "time";
	private static final String KEY_RESULT = "result";
	private static final String KEY_STATUS = "status";
	private static final String KEY_MODE = "mode";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_DETAILS + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_DATE + " TEXT," + KEY_TIME + " TEXT,"
				+ KEY_RESULT + " TEXT," + KEY_STATUS + " TEXT," + KEY_MODE + " TEXT" + ")";
		db.execSQL(CREATE_CONTACTS_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DETAILS);

		// Create tables again
		onCreate(db);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	public// Adding new contact
	void addContact(Contact contact) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_DATE, contact.getDate());
		values.put(KEY_TIME, contact.getTime());

		values.put(KEY_RESULT, contact.getResult());
		values.put(KEY_STATUS, contact.getStaus());
		values.put(KEY_MODE, contact.getMode());

		// Inserting Row
		db.insert(TABLE_DETAILS, null, values);
		db.close(); // Closing database connection
	}

	// Getting All Contacts angainst Status **********************************
	public List<Contact> getAllContactsStatus(String key) {
		List<Contact> contactList = new ArrayList<Contact>();

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM sevadetails WHERE status = '" + key + "';", null);
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Contact contact = new Contact();
				contact.setID(Integer.parseInt(cursor.getString(0)));
				contact.setDate(cursor.getString(1));
				contact.setTime(cursor.getString(2));
				contact.setResult(cursor.getString(3));
				contact.setStatus(cursor.getString(4));
				contact.setMode(cursor.getString(5));
				// Adding contact to list
				contactList.add(contact);
			} while (cursor.moveToNext());
		}

		// return contact list
		return contactList;
	}

	// Getting All Contacts
	public List<Contact> getAllContacts() {
		List<Contact> contactList = new ArrayList<Contact>();
		// Select All Query
		String selectQuery = "SELECT  * FROM sevadetails";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Contact contact = new Contact();
				contact.setID(Integer.parseInt(cursor.getString(0)));
				contact.setDate(cursor.getString(1));
				contact.setTime(cursor.getString(2));
				contact.setResult(cursor.getString(3));
				contact.setStatus(cursor.getString(4));
				contact.setMode(cursor.getString(5));
				// Adding contact to list
				contactList.add(contact);
			} while (cursor.moveToNext());
		}

		// return contact list

		Log.d("TAG", contactList.toString());

		return contactList;
	}

	// Getting All Contacts angainst a value **********************************
	public List<Contact> getAllContactsNeed(String key) {
		List<Contact> contactList = new ArrayList<Contact>();

		// String key2 = "YES";

		// Select All Query
		// String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

		SQLiteDatabase db = this.getWritableDatabase();
		/*
		 * Cursor cursor = db.query(TABLE_DETAILS, new String[] { KEY_ID,
		 * KEY_DATE, KEY_TIME, KEY_RESULT, KEY_STATUS, KEY_MODE }, KEY_DATE +
		 * "=?", new String[] { String.valueOf(key) }, null, null, null, null);
		 */

		Cursor cursor = db.rawQuery("SELECT * FROM sevadetails WHERE mode = '" + key + "';", null);
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Contact contact = new Contact();
				contact.setID(Integer.parseInt(cursor.getString(0)));
				contact.setDate(cursor.getString(1));
				contact.setTime(cursor.getString(2));
				contact.setResult(cursor.getString(3));
				contact.setStatus(cursor.getString(4));
				contact.setMode(cursor.getString(5));
				// Adding contact to list
				contactList.add(contact);
			} while (cursor.moveToNext());
		}

		// return contact list
		return contactList;
	}
	/*
	 * // Updating single contact public int updateContact(Contact contact) {
	 * SQLiteDatabase db = this.getWritableDatabase();
	 * 
	 * ContentValues values = new ContentValues(); values.put(KEY_NAME,
	 * contact.getName()); values.put(KEY_PH_NO, contact.getPhoneNumber());
	 * 
	 * // updating row return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
	 * new String[] { String.valueOf(contact.getID()) }); }
	 */
	/*
	 * // Deleting single contact public void deleteContact(Contact contact) {
	 * SQLiteDatabase db = this.getWritableDatabase(); db.delete(TABLE_CONTACTS,
	 * KEY_ID + " = ?", new String[] { String.valueOf(contact.getID()) });
	 * db.close(); }
	 */
	/*
	 * // Getting contacts Count public int getContactsCount() { String
	 * countQuery = "SELECT  * FROM " + TABLE_CONTACTS; SQLiteDatabase db =
	 * this.getReadableDatabase(); Cursor cursor = db.rawQuery(countQuery,
	 * null); cursor.close();
	 * 
	 * // return count return cursor.getCount(); }
	 */

}

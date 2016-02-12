package com.seva60plus.hum.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Seva60plusHumDatabase extends SQLiteOpenHelper implements DBConstants {

	private static final String TAG = "Seva60plusHumDatabase";
	private static Seva60plusHumDatabase mDatabase;
	private SQLiteDatabase mDb;

	public Seva60plusHumDatabase(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	public static final Seva60plusHumDatabase getInstance(Context context) {
		if (mDatabase == null) {
			mDatabase = new Seva60plusHumDatabase(context);
			mDatabase.getWritableDatabase();
		}
		return mDatabase;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		Log.i(TAG, "oncreate tables");
		// create table
		String[] createStatements = getCreatetableStatements();
		int total = createStatements.length;
		for (int i = 0; i < total; i++) {
			Log.i(TAG, "executing create query " + createStatements[i]);
			Log.i("Database", "Database created");
			db.execSQL(createStatements[i]);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	private String[] getCreatetableStatements() {

		String[] create = new String[1];

		// Saathi table -> _id , formID, formName, formData , parent_cat_id
		String SAATHI_LIST_TABLE_ST = CREATE_TABLE_BASE + SAATHI_TABLE + START_COLUMN + _ID + INTEGER + PRIMARY_KEY + AUTO_ICNREMENT + COMMA + SAATHI_NAME
				+ TEXT + COMMA + SAATHI_COUNTRY_CODE + TEXT + COMMA + SAATHI_PHONE_NUMBER + TEXT 
				+ COMMA + SAATHI_EMAIL + TEXT + COMMA + UNIQUE + START_COLUMN
				+ SAATHI_PHONE_NUMBER + FINISH_COLUMN + ON_CONFLICT_REPLACE + FINISH_COLUMN;
		// FORMLIST table
		create[0] = SAATHI_LIST_TABLE_ST;

		return create;
	}

	@Override
	public synchronized SQLiteDatabase getWritableDatabase() {

		return mDb != null ? mDb : (mDb = super.getWritableDatabase());
	}

	@Override
	public synchronized SQLiteDatabase getReadableDatabase() {

		return mDb != null ? mDb : (mDb = super.getReadableDatabase());
	}

	public void startmanagingcursor() {
		mDatabase.startmanagingcursor();
	}

}

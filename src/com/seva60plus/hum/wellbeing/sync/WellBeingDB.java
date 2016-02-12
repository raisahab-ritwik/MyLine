package com.seva60plus.hum.wellbeing.sync;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

public class WellBeingDB extends SQLiteOpenHelper implements DBConstants {

	private static final String TAG = Environment.getExternalStorageDirectory() + "/Seva60Plus/WellBeingDB";
	private String CREATE_TABLE_STATEMENT;

	public WellBeingDB(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		Log.i(TAG, "oncreate tables");
		db.execSQL(getCreatetableStatements());
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	private String getCreatetableStatements() {

		CREATE_TABLE_STATEMENT = CREATE_TABLE_BASE + WELLBEING_TABLE + START_COLUMN + _ID + INTEGER + PRIMARY_KEY + AUTO_ICNREMENT + COMMA + TIME_STAMP + TEXT
				+ COMMA + TYPE + TEXT + COMMA + VALUE + TEXT + COMMA + UNIQUE + START_COLUMN + TIME_STAMP + FINISH_COLUMN + ON_CONFLICT_REPLACE + FINISH_COLUMN;

		return CREATE_TABLE_STATEMENT;

	}

	/**
	 * Deletes a particular row per se
	 */
	public boolean deleteRow(String timeStamp) {
		Log.v("DATABASE HEPER", "----------------\n\n  delete row  \n");
		SQLiteDatabase mdb = this.getWritableDatabase();
		return mdb.delete(WELLBEING_TABLE, TIME_STAMP + "= ?", new String[] { timeStamp }) > 0;
	}
}

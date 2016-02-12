package com.seva60plus.hum.db;

import java.util.ArrayList;

import com.seva60plus.hum.sathi.Saathi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SaathiDB implements DBConstants {

	private static SaathiDB obj = null;

	public synchronized static SaathiDB obj() {

		if (obj == null)
			obj = new SaathiDB();
		return obj;

	}

	public Boolean saveSaathi(Context context, ContentValues cv) {

		System.out.println(" ----------  SAVE SAATHI IN DB  --------- ");
		SQLiteDatabase mdb = Seva60plusHumDatabase.getInstance(context).getWritableDatabase();
		mdb.beginTransaction();
		try {
			mdb.insert(SAATHI_TABLE, null, cv);
			mdb.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			mdb.endTransaction();
			return true;
		}

	}

	public ArrayList<Saathi> getSaathiList(Context context) {

		ArrayList<Saathi> saathiList = new ArrayList<Saathi>();

		SQLiteDatabase mdb = Seva60plusHumDatabase.getInstance(context).getReadableDatabase();
		Cursor cur = mdb.query(SAATHI_TABLE, null, null, null, null, null, null);

		if (!isDatabaseEmpty(cur)) {
			try {
				if (cur.moveToFirst()) {
					do {
						Saathi saathi = new Saathi();
						saathi.set_id(String.valueOf(cur.getInt(cur.getColumnIndex(_ID))));
						saathi.setName(cur.getString(cur.getColumnIndex(SAATHI_NAME)));
						saathi.setCountryCode(cur.getString(cur.getColumnIndex(SAATHI_COUNTRY_CODE)));
						saathi.setPhoneNumber(cur.getString(cur.getColumnIndex(SAATHI_PHONE_NUMBER)));
						saathi.setEmailId(cur.getString(cur.getColumnIndex(SAATHI_EMAIL)));
						saathiList.add(saathi);
					} while (cur.moveToNext());
				}
				cur.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return saathiList;
	}

	/** Check if the Database is empty or not */
	private Boolean isDatabaseEmpty(Cursor mCursor) {

		if (mCursor.moveToFirst()) {
			// NOT EMPTY
			return false;

		} else {
			// IS EMPTY
			return true;
		}

	}

	/** Delete Saathi row */
	public boolean deleteSaathiRow(Context context, String phoneNumber) {
		SQLiteDatabase mdb = Seva60plusHumDatabase.getInstance(context).getWritableDatabase();
		return mdb.delete(SAATHI_TABLE, SAATHI_PHONE_NUMBER + "= ?", new String[] { phoneNumber }) > 0;
	}

	
	public void deleteSaathiTable(Context context){
		
	System.out.println("<<------------ Saathi Table deleted  <<  -----");
		SQLiteDatabase mdb = Seva60plusHumDatabase.getInstance(context).getWritableDatabase();
		mdb.delete(SAATHI_TABLE, null,null);
	}
}

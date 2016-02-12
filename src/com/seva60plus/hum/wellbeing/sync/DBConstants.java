package com.seva60plus.hum.wellbeing.sync;

import android.os.Environment;

public interface DBConstants {

	public static final int DB_VERSION = 1;

	//public static final String DB_NAME = "WellBeingDB.db";
	public static final String DB_NAME = Environment.getExternalStorageDirectory() + "/Seva60Plus/WellBeing";

	public static final String _ID = "_id";

	final String CREATE_TABLE_BASE = "CREATE TABLE IF NOT EXISTS ";

	final String ON = " ON ";

	final String PRIMARY_KEY = " PRIMARY KEY";

	final String INTEGER = " Integer";

	final String TEXT = " TEXT";

	final String DATE_TIME = " DATETIME";

	final String BLOB = " BLOB";

	final String AUTO_ICNREMENT = " AUTOINCREMENT";

	final String UNIQUE = "UNIQUE";

	final String START_COLUMN = " ( ";

	final String FINISH_COLUMN = " ) ";

	final String COMMA = ",";

	final String ON_CONFLICT_REPLACE = "ON CONFLICT REPLACE";

	// WellBeing Table
	public static final String WELLBEING_TABLE = " wellBeingTable";

	public static final String TIME_STAMP = "timeStamp";

	public static final String TYPE = "type";

	public static final String VALUE = "value";

}

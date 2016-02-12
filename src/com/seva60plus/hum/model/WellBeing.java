package com.seva60plus.hum.model;

import java.io.Serializable;

import com.seva60plus.hum.wellbeing.sync.Constant;
import com.seva60plus.hum.wellbeing.sync.WellBeingDB;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

/**
 * Serializable class of Well-being.
 * */
public class WellBeing implements Serializable {
	/**
	 * version u-id
	 */
	private static final long serialVersionUID = 1L;

	private String timeStamp;

	private String type;

	private String value;

	public static final Uri CONTENT_URI = Uri.parse("content://" + Constant.PROVIDER + "/wellbeing");

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static WellBeing fromCursor(Cursor cur) {

		String timestamp = cur.getString(cur.getColumnIndex(WellBeingDB.TIME_STAMP));
		String type = cur.getString(cur.getColumnIndex(WellBeingDB.TYPE));
		String value = cur.getString(cur.getColumnIndex(WellBeingDB.VALUE));
		WellBeing wellbeing = new WellBeing();
		wellbeing.setTimeStamp(timestamp);
		wellbeing.setType(type);
		wellbeing.setValue(value);
		return wellbeing;

	}

	public ContentValues getContentValues(String timeStamp, String type, String value) {
		ContentValues values = new ContentValues();
		values.put(WellBeingDB.TIME_STAMP, timeStamp);
		values.put(WellBeingDB.TYPE, type);
		values.put(WellBeingDB.VALUE, value);
		return values;
	}
}

package com.seva60plus.hum.util;

public class Contact {

	//private variables
	int _id;
	String _date;
	String _time;
	String _result;
	String _status;
	String _mode;

	// Empty constructor
	public Contact() {

	}

	// constructor
	public Contact(int id, String date, String time, String result, String status, String mode) {
		this._id = id;
		this._date = date;
		this._time = time;
		this._result = result;
		this._status = status;
		this._mode = mode;

	}

	// constructor
	public Contact(String date, String time, String result, String status, String mode) {
		this._date = date;
		this._time = time;
		this._result = result;
		this._status = status;
		this._mode = mode;
	}

	// getting ID
	public int getID() {
		return this._id;
	}

	// setting id
	public void setID(int id) {
		this._id = id;
	}

	// getting Date
	public String getDate() {
		return this._date;
	}

	// setting Date
	public void setDate(String date) {
		this._date = date;
	}

	// getting Time
	public String getTime() {
		return this._time;
	}

	// setting Time
	public void setTime(String time) {
		this._time = time;
	}

	// getting Result
	public String getResult() {
		return this._result;
	}

	// setting Result
	public void setResult(String result) {
		this._result = result;
	}

	// getting Status
	public String getStaus() {
		return this._status;
	}

	// setting Status
	public void setStatus(String status) {
		this._status = status;
	}

	// getting Mode
	public String getMode() {
		return this._mode;
	}

	// setting Mode
	public void setMode(String mode) {
		this._mode = mode;
	}
}

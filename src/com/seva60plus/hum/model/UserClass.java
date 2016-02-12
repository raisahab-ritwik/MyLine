package com.seva60plus.hum.model;

import java.io.Serializable;

import android.util.Log;

public class UserClass implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	private Hum userHum = new Hum();

	public static volatile UserClass userClass = null;

	public static UserClass getInstance() {

		System.out.println("1 : " + userClass);
		if (userClass == null) {
			synchronized (UserClass.class) {
				userClass = new UserClass();
				Log.v("getInstance...", "Called");
			}
		}

		Log.v("2 : ", userClass.toString());
		return userClass;
	}

	public Hum getUserHum() {
		return userHum;
	}

	public void setUserHum(Hum userHum) {
		this.userHum = userHum;
	}
}

package com.seva60plus.hum.util;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.seva60plus.hum.model.Hum;

public class PreferenceUtil {
	private static String USERCLASS = "USERCLASS";

	// Saving DataClass details
	public static void saveUserClass(final Activity mContext, Hum userClass) {
		SharedPreferences tfdsPrefs = mContext.getSharedPreferences("seva60plusHumPrefs", Context.MODE_PRIVATE);
		SharedPreferences.Editor prefsEditor = tfdsPrefs.edit();
		try {
			prefsEditor.putString(USERCLASS, ObjectSerializer.serialize(userClass));
		} catch (IOException e) {
			e.printStackTrace();
		}
		prefsEditor.commit();
	}

	// Fetching DataClass details
	public static Hum fetchUserClass(final Context mContext) {
		SharedPreferences tfdsPrefs = mContext.getSharedPreferences("seva60plusHumPrefs", Context.MODE_PRIVATE);
		Hum userClass = null;
		String serializeOrg = tfdsPrefs.getString(USERCLASS, null);
		try {
			if (serializeOrg != null) {
				userClass = (Hum) ObjectSerializer.deserialize(serializeOrg);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return userClass;
	}

}

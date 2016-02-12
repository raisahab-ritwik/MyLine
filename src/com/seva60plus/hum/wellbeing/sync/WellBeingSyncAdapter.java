package com.seva60plus.hum.wellbeing.sync;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SyncResult;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import com.seva60plus.hum.model.WellBeing;
import com.seva60plus.hum.staticconstants.ConstantVO;
import com.seva60plus.hum.util.JsonParser;
import com.seva60plus.hum.util.Util;

public class WellBeingSyncAdapter extends AbstractThreadedSyncAdapter {

	private String TAG = getClass().getSimpleName().toString().trim();
	private JsonParser jsonParser;

	public WellBeingSyncAdapter(Context context, boolean autoInitialize) {
		super(context, autoInitialize);
		Log.v("WellBeingSyncAdapter", "Constructor");
	}

	@Override
	public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {

		try {
			String humPhone = Util.getSharedPreference(getContext()).getString("humPhone", "");
			if (!(humPhone.trim().length() > 0)) {
				SharedPreferences prefs = getContext().getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE);
				humPhone = prefs.getString("UserPhoneNumber", "");
			}
			Log.d(TAG, "--->> on Perform Sync -->>> Phone:: " + humPhone);
			ArrayList<WellBeing> localUserList = new ArrayList<WellBeing>();
			Cursor cur = provider.query(WellBeing.CONTENT_URI, null, null, null, null);
			if (cur != null) {
				while (cur.moveToNext()) {
					localUserList.add(WellBeing.fromCursor(cur));
				}
				cur.close();
			}

			jsonParser = new JsonParser();
			for (int i = 0; i < localUserList.size(); i++) {

				System.out.println("Time stamp " + localUserList.get(i).getTimeStamp());
				System.out.println("Type " + localUserList.get(i).getType());
				System.out.println("Vaue " + localUserList.get(i).getValue());
				String[] timestamp = localUserList.get(i).getTimeStamp().split("\\ ");
				String date = timestamp[0].trim();
				String time = timestamp[1].trim();
				sendStatisticsData(localUserList.get(i).getType(), localUserList.get(i).getValue(), humPhone, date, time, localUserList.get(i).getTimeStamp());

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Boolean sendStatisticsData(String type, String answer, String phoneNo, String iDate, String iTime, String timeStamp) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("notification_type", type));
		params.add(new BasicNameValuePair("phone_no", phoneNo));
		params.add(new BasicNameValuePair("ans", answer));
		params.add(new BasicNameValuePair("added_date", iDate));
		params.add(new BasicNameValuePair("added_time", iTime));

		JSONObject json = jsonParser.getJSONFromUrl(ConstantVO.SEND_STATISTICS_DETAILS, params);
		System.out.println("JSON Response Sync ADapter : " + json);
		boolean postStatus = parseResponse(json.toString());
		if (postStatus) {
			deleteFromDatabase(timeStamp);
		}
		return false;
	}

	private void deleteFromDatabase(String timeStamp) {
		System.out.println("---------------------------------------------\n");
		System.out.println("deletefrom database");
		System.out.println("---------------------------------------------\n\n");
		WellBeingDB mdb = new WellBeingDB(getContext());
		boolean status = mdb.deleteRow(timeStamp);
		if (status) {
			System.out.println("DEeted!!");
		} else
			System.out.println("NOT DONE DELETE");
	}

	private boolean parseResponse(String string) {
		String status = "";
		try {
			JSONObject jobj = new JSONObject(string);
			status = jobj.optString("code");
			Log.e("parseResponse", "parseResponse code :" + status);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (status.trim().equalsIgnoreCase("100"))
			return true;
		else
			return false;
	}
}

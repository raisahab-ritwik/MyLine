package com.seva60plus.hum.asynctask;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.seva60plus.hum.Listeners.OnCheckUserListener;
import com.seva60plus.hum.custom.ProgressHUD;
import com.seva60plus.hum.model.Hum;
import com.seva60plus.hum.util.PreferenceUtil;
import com.seva60plus.hum.util.Util;

public class CheckUser extends AsyncTask<Void, Void, String> implements OnCancelListener {

	private Context mContext;
	ProgressHUD mProgressHUD;
	private String subscriberId;
	public OnCheckUserListener mListener;
	String strResponse = "";
	private String TAG="CheckUser";

	public CheckUser(Context mContext, String subscriberId) {

		this.mContext = mContext;
		this.subscriberId = subscriberId;

	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		mProgressHUD = ProgressHUD.show(mContext, "Please wait....", true, true, this);
	}

	@Override
	protected String doInBackground(Void... params) {
		checkUser();
		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		mProgressHUD.dismiss();
		mListener.onCheckUser(strResponse);
	}

	private String checkUser() {

		try {

			JSONObject json2 = null;

			try {

				strResponse = Util.postMethodWayNoRequestData("http://seva60plus.co.in/seva60PlusAndroidAPI/register/checkUserRegistered/" + subscriberId);

			} catch (Exception e) {
				e.printStackTrace();
			}

			System.out.println("Response:  \n" + strResponse);
			try {
				JSONArray jArray2 = new JSONArray(strResponse);
				json2 = jArray2.getJSONObject(0);
				strResponse= json2.getString("code");
				String userPhoneNumber = json2.optString("humPhone");
				Hum hum = new Hum();
				hum.setPhone(userPhoneNumber.trim());
				PreferenceUtil.saveUserClass((Activity) mContext, hum);

				SharedPreferences.Editor mEditor = Util.getSharedEditor(mContext);
				mEditor.putString("humPhone", userPhoneNumber).commit();
			} catch (Exception e) {
				Log.d(TAG, "Exception Occured");
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return strResponse;
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		mProgressHUD.dismiss();

	}
}

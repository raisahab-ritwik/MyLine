package com.seva60plus.hum.asynctask;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.util.Log;

import com.seva60plus.hum.Listeners.AddSaathiListener;
import com.seva60plus.hum.custom.ProgressHUD;
import com.seva60plus.hum.sathi.Saathi;
import com.seva60plus.hum.staticconstants.ConstantVO;
import com.seva60plus.hum.util.Util;

public class AddSaathi extends AsyncTask<Void, Void, String> implements OnCancelListener {

	public AddSaathiListener mListener;
	private Saathi mSaathi;
	private Context mContext;
	public static final String RESPONSE_SUCCESS = "200";
	public static final String RESPONSE_FAILED = "000";
	private String response = RESPONSE_FAILED;
	ProgressHUD mProgressHUD;

	public AddSaathi(Context mContext, Saathi mSaathi) {
		this.mSaathi = mSaathi;
		this.mContext = mContext;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mProgressHUD = ProgressHUD.show(mContext, "Please wait...", true, true, this);
	}

	@Override
	protected String doInBackground(Void... params) {

		try {
			List<NameValuePair> requestParams = compileRequest();
			String json = Util.getJSONFromUrl(ConstantVO.ADD_SAATHI_URL, requestParams);
			Log.v("Register User", "Response:: \n" + json);
			if (parseResponse(json).trim().equalsIgnoreCase(RESPONSE_SUCCESS))
				response = RESPONSE_SUCCESS;

		} catch (Exception e) {

			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		mProgressHUD.dismiss();
		mListener.onSaathiAdd(response);
	}

	private List<NameValuePair> compileRequest() throws Exception {

		String humPhone = Util.getSharedPreference(mContext).getString("humPhone", "");
		List<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("humPhone", humPhone));
		params.add(new BasicNameValuePair("saathiPhone", mSaathi.getPhoneNumber()));
		params.add(new BasicNameValuePair("saathiCountryCode", mSaathi.getCountryCode()));
		params.add(new BasicNameValuePair("saathiName", mSaathi.getName()));
		params.add(new BasicNameValuePair("saathiEmail", mSaathi.getEmailId()));

		return params;
	}

	private String parseResponse(String json) {

		String parsedResponse = RESPONSE_FAILED;
		try {

			parsedResponse = new JSONObject(json).optString("code");
		} catch (Exception e) {

			e.printStackTrace();
		}
		return parsedResponse;
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		mProgressHUD.dismiss();
	}
}

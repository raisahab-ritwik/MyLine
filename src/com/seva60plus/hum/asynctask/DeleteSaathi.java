package com.seva60plus.hum.asynctask;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.seva60plus.hum.Listeners.DeleteSaathiListener;
import com.seva60plus.hum.custom.ProgressHUD;
import com.seva60plus.hum.sathi.Saathi;
import com.seva60plus.hum.staticconstants.ConstantVO;
import com.seva60plus.hum.util.CustomAsynkLoader;
import com.seva60plus.hum.util.Util;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.util.Log;

public class DeleteSaathi extends AsyncTask<Void, Void, String> implements OnCancelListener {

	private Context mContext;
	public DeleteSaathiListener mListener;
	private Saathi mSaathi;
	public static final String RESPONSE_SUCCESS = "1";
	public static final String RESPONSE_FAILED = "000";
	private String response = RESPONSE_FAILED;
	private int position;
	ProgressHUD mProgressHUD;

	public DeleteSaathi(Context mContext, Saathi mSaathi, int position) {

		this.mSaathi = mSaathi;
		this.mContext = mContext;
		this.position = position;
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
			String json = Util.getJSONFromUrl(ConstantVO.DELETE_SAATHI_URL, requestParams);
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
		mListener.onDeleteSaathi(response, this.position);
		mProgressHUD.dismiss();
	}

	private List<NameValuePair> compileRequest() throws Exception {

		String humPhone = Util.getSharedPreference(mContext).getString("humPhone", "");
		List<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("humPhone", humPhone));
		params.add(new BasicNameValuePair("saathiPhone", mSaathi.getPhoneNumber()));

		return params;
	}

	private String parseResponse(String json) {

		String parsedResponse = RESPONSE_FAILED;
		try {

			parsedResponse = new JSONObject(json).optString("status");
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

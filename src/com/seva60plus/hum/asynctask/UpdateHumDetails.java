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

import com.seva60plus.hum.Listeners.UpdateHumDetailsAsyncListener;
import com.seva60plus.hum.custom.ProgressHUD;
import com.seva60plus.hum.model.Hum;
import com.seva60plus.hum.network.HttpPostHelper;
import com.seva60plus.hum.staticconstants.ConstantVO;

public class UpdateHumDetails extends AsyncTask<Void, Void, String> implements OnCancelListener {

	private Hum mHum;
	public UpdateHumDetailsAsyncListener mListener;
	private String responseCode = RESPONSE_FAILED;
	private String isGeo = "1";
	public static final String RESPONSE_SUCCESS = "200";
	public static final String RESPONSE_FAILED = "000";
	ProgressHUD mProgressHUD;
	private Context mContext;

	public UpdateHumDetails(Context mContext, Hum mHum) {
		this.mHum = mHum;
		this.mContext = mContext;
	}

	@Override
	protected void onPreExecute() {

		super.onPreExecute();
		mProgressHUD = ProgressHUD.show(mContext, "Please wait...", true, true, this);
	}

	@Override
	protected String doInBackground(Void... params) {
		String response = postData();
		try {
			if (parseResponse(response).equalsIgnoreCase(RESPONSE_SUCCESS))

				responseCode = RESPONSE_SUCCESS;
			else

				responseCode = RESPONSE_FAILED;

		} catch (Exception e) {

			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(String result) {

		super.onPostExecute(result);
		mProgressHUD.dismiss();

		mListener.getUpdateStatus(responseCode, isGeo);
	}

	private String postData() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("submit", "submit"));
		params.add(new BasicNameValuePair("sub_id", mHum.getSubscriberId()));
		params.add(new BasicNameValuePair("imei", mHum.getImeiNumber()));
		params.add(new BasicNameValuePair("email", mHum.getEmail()));

		params.add(new BasicNameValuePair("name", mHum.getName()));
		params.add(new BasicNameValuePair("phone", mHum.getPhone()));
		params.add(new BasicNameValuePair("country_code", mHum.getCountryCode()));

		Log.v("Date of birth", "DOB: " + mHum.getDateOfBirth());
		params.add(new BasicNameValuePair("dob", mHum.getDateOfBirth()));
		params.add(new BasicNameValuePair("gender", mHum.getGender()));

		params.add(new BasicNameValuePair("is_geo_enabled", mHum.getIsGeoEnabled()));
		JSONObject responseObject = HttpPostHelper.getJSONFromUrl(ConstantVO.HUM_UPDATE_URL, params);
		return responseObject.toString();

	}

	private String parseResponse(String response) throws Exception {

		JSONObject jObj = new JSONObject(response);
		responseCode = jObj.optString("code");
		isGeo = jObj.optString("is_geo_enabled");
		return responseCode;

	}

	@Override
	public void onCancel(DialogInterface dialog) {

		mProgressHUD.dismiss();
	}
}

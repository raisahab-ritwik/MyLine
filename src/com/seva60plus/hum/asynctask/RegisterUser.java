package com.seva60plus.hum.asynctask;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.seva60plus.hum.Listeners.RegisterUserListener;
import com.seva60plus.hum.custom.ProgressHUD;
import com.seva60plus.hum.model.HumRegister;
import com.seva60plus.hum.staticconstants.ConstantVO;
import com.seva60plus.hum.util.Util;

public class RegisterUser extends AsyncTask<Void, Void, String> implements OnCancelListener {

	private Context mContext;
	private HumRegister humRegisterObj;
	public RegisterUserListener mListener;
	public static final String RESPONSE_SUCCESS = "200";
	public static final String RESPONSE_FAILED = "000";
	private String response = RESPONSE_FAILED;
	private String saathiPhone = "";
	ProgressHUD mProgressHUD;

	public RegisterUser(Context mContext, HumRegister humRegisterObj) {

		this.mContext = mContext;
		this.humRegisterObj = humRegisterObj;
	}

	@Override
	protected void onPreExecute() {

		super.onPreExecute();

		mProgressHUD = ProgressHUD.show(mContext, "Please wait...", true, true, this);
	}

	@Override
	protected String doInBackground(Void... params) {

		List<NameValuePair> requestParams = compileRequest();
		String json = Util.getJSONFromUrl(ConstantVO.REGISTER_URL, requestParams);
		Log.v("Register User", "Response:: \n" + json);
		if (parseResponse(json).equalsIgnoreCase(RESPONSE_SUCCESS))
			response = RESPONSE_SUCCESS;
		return null;
	}

	@Override
	protected void onPostExecute(String result) {

		super.onPostExecute(result);
		mProgressHUD.dismiss();
		mListener.onRegisterUser(this.response, saathiPhone);
	}

	private List<NameValuePair> compileRequest() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		Log.v("Register User", "" + humRegisterObj.getSathi_email1());
		params.add(new BasicNameValuePair("submit", humRegisterObj.getSubmit()));
		params.add(new BasicNameValuePair("sub_id", humRegisterObj.getSub_id()));
		params.add(new BasicNameValuePair("imei", humRegisterObj.getImei()));
		params.add(new BasicNameValuePair("email", humRegisterObj.getEmail()));
		params.add(new BasicNameValuePair("name", humRegisterObj.getName()));
		params.add(new BasicNameValuePair("country_code", humRegisterObj.getCountry_code()));
		params.add(new BasicNameValuePair("phone", humRegisterObj.getPhone()));
		params.add(new BasicNameValuePair("dob", humRegisterObj.getDob()));
		params.add(new BasicNameValuePair("gender", humRegisterObj.getGender()));
		params.add(new BasicNameValuePair("address1", humRegisterObj.getAddress1()));
		params.add(new BasicNameValuePair("city", humRegisterObj.getCity()));
		params.add(new BasicNameValuePair("state", humRegisterObj.getState()));
		params.add(new BasicNameValuePair("pincode", humRegisterObj.getPincode()));
		params.add(new BasicNameValuePair("sathi_name1", humRegisterObj.getSathi_name1()));
		params.add(new BasicNameValuePair("sathi_email1", humRegisterObj.getSathi_email1()));
		params.add(new BasicNameValuePair("sathi_name2", humRegisterObj.getSathi_name2()));
		params.add(new BasicNameValuePair("sathi_email2", humRegisterObj.getSathi_email2()));
		params.add(new BasicNameValuePair("sathi1_country_code", humRegisterObj.getSathi1_country_code()));
		params.add(new BasicNameValuePair("sathi_contact_no1", humRegisterObj.getSathi_contact_no1()));
		params.add(new BasicNameValuePair("sathi2_country_code", humRegisterObj.getSathi2_country_code()));
		params.add(new BasicNameValuePair("sathi_contact_no2", humRegisterObj.getSathi_contact_no2()));
		params.add(new BasicNameValuePair("pharmacy_no", humRegisterObj.getPharmacy_no()));
		params.add(new BasicNameValuePair("status", humRegisterObj.getStatus()));
		params.add(new BasicNameValuePair("is_geo_enabled", humRegisterObj.getIs_geo_enabled()));

		return params;
	}

	private String parseResponse(String json) {

		String parsedResponse = RESPONSE_FAILED;
		try {
			JSONArray jArray = new JSONArray(json);
			parsedResponse = jArray.getJSONObject(0).optString("code");
			saathiPhone = jArray.getJSONObject(0).optString("sathi1_contact_no");

			if (parsedResponse.equalsIgnoreCase(RESPONSE_SUCCESS)) {
				SharedPreferences.Editor mEditor = Util.getSharedEditor(mContext);
				mEditor.putString("humPhone", humRegisterObj.getPhone());
				mEditor.putString("geoLocation", humRegisterObj.getIs_geo_enabled());
				mEditor.commit();
			}
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

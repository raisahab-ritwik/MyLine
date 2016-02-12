package com.seva60plus.hum.asynctask;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.util.Log;

import com.seva60plus.hum.Listeners.HumDetailsAsyncListener;
import com.seva60plus.hum.custom.ProgressHUD;
import com.seva60plus.hum.model.Hum;
import com.seva60plus.hum.network.HttpPostHelper;
import com.seva60plus.hum.staticconstants.ConstantVO;
import com.seva60plus.hum.util.CustomAsynkLoader;

@SuppressWarnings("unused")
public class HumDetailsAsync extends AsyncTask<Void, Void, String> implements OnCancelListener {

	// private CustomAsynkLoader mDialog;

	private Context mContext;
	public HumDetailsAsyncListener mListener;
	private String subscriberId;
	Hum mHum = new Hum();
	ProgressHUD mProgressHUD;

	public HumDetailsAsync(Context mContext, String subscriberId) {
		this.mContext = mContext;
		// mDialog = new CustomAsynkLoader(mContext);
		this.subscriberId = subscriberId;
		// ConstantVO.HUM_UPDATE_URL;
	}

	@Override
	protected void onPreExecute() {

		super.onPreExecute();
		// if (!mDialog.isDialogShowing()) {
		// mDialog.ShowDialog();
		// }
		mProgressHUD = ProgressHUD.show(mContext, "Please wait...", true, true, this);
	}

	@Override
	protected String doInBackground(Void... params) {
		String response = null;
		response = HttpPostHelper.postMethodWay(ConstantVO.GET_USER_DETAILS + subscriberId);
		parseResponse(response);
		return response;
	}

	@Override
	protected void onPostExecute(String result) {

		super.onPostExecute(result);
		// if (mDialog.isDialogShowing())
		// mDialog.DismissDialog();
		mProgressHUD.dismiss();
		mListener.getHumDetailsJson(mHum);

	}

	private Hum parseResponse(String response) {
		Log.v("HumDetailsAsync", "Response:\n" + response);

		try {
			JSONObject responseJsonObject = new JSONObject(response);
			JSONArray detailsJsonArray = new JSONArray(responseJsonObject.optString("details"));
			for (int i = 0; i < detailsJsonArray.length(); i++) {
				JSONObject detailsJsonObject = detailsJsonArray.getJSONObject(i);
				mHum.setId(Integer.parseInt(detailsJsonObject.optString("id")));
				mHum.setSubscriberId(detailsJsonObject.optString("sub_id"));
				mHum.setImeiNumber(detailsJsonObject.optString("imei"));
				mHum.setEmail(detailsJsonObject.optString("email"));
				mHum.setName(detailsJsonObject.optString("name"));
				mHum.setCountryCode(detailsJsonObject.optString("country_code"));
				mHum.setPhone(detailsJsonObject.optString("phone"));
				mHum.setDateOfBirth(detailsJsonObject.optString("dob"));
				mHum.setGender(detailsJsonObject.optString("gender"));
				mHum.setIsGeoEnabled(detailsJsonObject.optString("is_geo_enabled"));

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return mHum;

	}

	@Override
	public void onCancel(DialogInterface dialog) {

		mProgressHUD.dismiss();

	}

}

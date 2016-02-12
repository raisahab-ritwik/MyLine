package com.seva60plus.hum.sathi;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.util.Log;

import com.seva60plus.hum.custom.ProgressHUD;
import com.seva60plus.hum.util.Util;

public class FetchSaathiList extends AsyncTask<String, Void, String> implements OnCancelListener {

	// private CustomAsynkLoader mDialog;
	// ProgressDialog progress;
	private String subscriberId;
	private ArrayList<Saathi> saathiList = new ArrayList<Saathi>();
	public FetchSaathiListListener mListener;
	ProgressHUD mProgressHUD;
	private Context mContext;

	public FetchSaathiList(Context mContext, String subscriberId) {
		// mDialog = new CustomAsynkLoader(mContext);
		this.subscriberId = subscriberId;
		this.mContext = mContext;
		// progress = new ProgressDialog(mContext);
		// progress.setMessage("Please wait..");
		// progress.setCancelable(true);
		// progress.getWindow().setBackgroundDrawable(new
		// ColorDrawable(Color.TRANSPARENT));
		// progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		// if (!mDialog.isDialogShowing())
		// mDialog.ShowDialog();
		// if (!progress.isShowing())
		// progress.show();
		mProgressHUD = ProgressHUD.show(mContext, "Please wait...", true, true, this);

	}

	@Override
	protected String doInBackground(String... arg0) {

		String str_response = Util.postMethodWay(arg0[0], subscriberId);
		try {
			parseResponse(str_response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		// if (mDialog.isDialogShowing())
		// mDialog.DismissDialog();
		// if (progress != null & progress.isShowing())
		// progress.cancel();
		mProgressHUD.dismiss();
		mListener.getSaathiList(saathiList);
	}

	private void parseResponse(String response) throws Exception {

		Log.v("FetchSaathi", "Response:-  " + response);
		JSONObject responseObj = new JSONObject(response);
		JSONArray detailsJsonArray = new JSONArray(responseObj.optString("details"));
		for (int i = 0; i < detailsJsonArray.length(); i++) {
			JSONObject detailsJsonObject = detailsJsonArray.getJSONObject(i);
			Saathi saathi = new Saathi();
			saathi.setName(detailsJsonObject.optString("sathiName"));
			saathi.setPhoneNumber(detailsJsonObject.optString("sathiPhone"));
			saathi.setEmailId(detailsJsonObject.optString("sathiEmail"));
			saathiList.add(saathi);
		}
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		mProgressHUD.dismiss();

	}
}

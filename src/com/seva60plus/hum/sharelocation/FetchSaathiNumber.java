package com.seva60plus.hum.sharelocation;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.util.Log;

import com.seva60plus.hum.sathi.FetchSaathiListListener;
import com.seva60plus.hum.sathi.Saathi;
import com.seva60plus.hum.util.Util;

public class FetchSaathiNumber extends AsyncTask<String, Void, String> {

	// private CustomAsynkLoader mDialog;
	ProgressDialog progress;
	private String subscriberId;
	private ArrayList<Saathi> saathiList = new ArrayList<Saathi>();
	public FetchSaathiListListener mListener;

	public FetchSaathiNumber(Context mContext, String subscriberId) {
		// mDialog = new CustomAsynkLoader(mContext);
		this.subscriberId = subscriberId;
		progress = new ProgressDialog(mContext);
		progress.setMessage("Please wait..");
		progress.setCancelable(true);
		progress.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		// if (!mDialog.isDialogShowing())
		// mDialog.ShowDialog();
		if (!progress.isShowing())
			progress.show();

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
		if (progress != null & progress.isShowing())
			progress.cancel();
		mListener.getSaathiList(saathiList);
	}

	private void parseResponse(String response) throws Exception {

		Log.v("Fetch Saathi Name", "Response:-  " + response);
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
}

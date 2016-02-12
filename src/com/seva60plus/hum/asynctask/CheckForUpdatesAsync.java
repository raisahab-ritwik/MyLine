package com.seva60plus.hum.asynctask;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;

import com.seva60plus.hum.Listeners.CheckUpdateListener;
import com.seva60plus.hum.custom.ProgressHUD;

public class CheckForUpdatesAsync extends AsyncTask<Void, Void, String> implements OnCancelListener {

	ProgressHUD mProgressHUD;
	// private CustomAsynkLoader mDialog;
	public CheckUpdateListener mListener;
	private String updateAppVersion = "";
	private Context mContext;
	private int CONNECTIONTIMEOUT = 10 * 1000;

	public CheckForUpdatesAsync(Context mContext) {
		// mDialog = new CustomAsynkLoader(mContext);
		this.mContext = mContext;

	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		// if (!mDialog.isDialogShowing())
		// mDialog.ShowDialog();
		mProgressHUD = ProgressHUD.show(mContext, "Please wait...", true, true, this);
	}

	@Override
	protected String doInBackground(Void... arg0) {
		String version = "";
		JSONObject json2 = null;
		String str2 = "";
		HttpResponse response2;
		HttpClient client = new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(client.getParams(), CONNECTIONTIMEOUT);
		HttpConnectionParams.setSoTimeout(client.getParams(), CONNECTIONTIMEOUT);
		HttpPost myConnection2 = new HttpPost("http://seva60plus.co.in/seva60PlusAndroidAPI/dbversion");

		try {
			response2 = client.execute(myConnection2);
			str2 = EntityUtils.toString(response2.getEntity(), "UTF-8");

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Json Sarted.... " + "\n" + str2);
		try {
			json2 = new JSONObject(str2);
			version = json2.getString("version");
			System.out.println("Code: " + version);

		} catch (Exception e) {
			e.printStackTrace();
		}
		updateAppVersion = version;
		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		// if (mDialog.isDialogShowing())
		// mDialog.DismissDialog();
		mProgressHUD.dismiss();
		mListener.getUpdateVersion(updateAppVersion);
	}

	@Override
	public void onCancel(DialogInterface dialog) {

		mProgressHUD.dismiss();

	}
}

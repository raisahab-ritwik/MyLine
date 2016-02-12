package com.seva60plus.hum.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.seva60plus.hum.R;
import com.seva60plus.hum.Listeners.AlertDialogCallBack;
import com.seva60plus.hum.model.WellBeing;

/**
 * Utility Class
 * 
 * @author raisahab.ritwik
 */
public class Util {

	private static int CONNECTIONTIMEOUT = 10000;
	private static StringBuilder sb = null;
	private static String WELLBEINGCLASS = "WELLBEING";
	private static String PREF_FILE_NAME = "HUM_PREF_FILE";

	/**
	 * Check if Internet Connection is available or not
	 * 
	 * @param context
	 *            - {@link Context}
	 **/
	public static boolean isInternetAvailable(Context context) {

		ConnectivityManager conManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo i = conManager.getActiveNetworkInfo();
		if ((i == null) || (!i.isConnected()) || (!i.isAvailable())) {

			return false;
		}
		return true;
	}

	/** Hide Soft Keyboard **/
	public static void hideSoftKeyboard(Context context, View view) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
	}

	/**
	 * Check the authenticity of an email ID
	 * 
	 * @param email
	 *            - {@link String}
	 **/
	public static boolean isEmailValid(String email) {
		Pattern pattern;
		Matcher matcher;
		String regExpn = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@" + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
				+ "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\." + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?" + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
				+ "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

		CharSequence inputStr = email;

		pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
		matcher = pattern.matcher(inputStr);

		if (matcher.matches())
			return true;
		else
			return false;
	}

	public static String postMethodWay_json(String hostName, String data) throws ClientProtocolException, IOException {

		sb = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(client.getParams(), CONNECTIONTIMEOUT);
		HttpConnectionParams.setSoTimeout(client.getParams(), CONNECTIONTIMEOUT);
		HttpResponse response;

		try {
			HttpPost post = new HttpPost(hostName);

			StringEntity se = new StringEntity(data);
			se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
			post.setEntity(se);
			response = client.execute(post);
			int statuscode = response.getStatusLine().getStatusCode();

			/* Checking response */
			if (statuscode == 200) {

				if (response != null) {
					InputStream in = response.getEntity().getContent();
					// InputStream in = /* your InputStream */;
					InputStreamReader is = new InputStreamReader(in);

					BufferedReader br = new BufferedReader(is);
					String read = br.readLine();

					while (read != null) {
						// System.out.println(read);
						sb.append(read);
						read = br.readLine();

					}
					// the entity
					Log.i("tAG", "" + sb.toString());
					return sb.toString();
				}
			} else {
				// the entity
				System.out.println("Response code is:: " + response.getStatusLine().getStatusCode());
				sb.append(statuscode);
				return sb.toString();
			}

		} catch (Exception e) {
			e.printStackTrace();
			sb.append(e.getLocalizedMessage());
			Log.i("connection", "Cannot Estabilish Connection! " + e.getLocalizedMessage());
		}

		return sb.toString();
	}

	public static String postMethodWayNoRequestData(String hostName) throws ClientProtocolException, IOException {

		sb = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(client.getParams(), CONNECTIONTIMEOUT);
		HttpConnectionParams.setSoTimeout(client.getParams(), CONNECTIONTIMEOUT);
		HttpResponse response;

		try {
			HttpPost post = new HttpPost(hostName);

			response = client.execute(post);
			int statuscode = response.getStatusLine().getStatusCode();

			/* Checking response */
			if (statuscode == 200) {

				if (response != null) {
					InputStream in = response.getEntity().getContent();
					// InputStream in = /* your InputStream */;
					InputStreamReader is = new InputStreamReader(in);

					BufferedReader br = new BufferedReader(is);
					String read = br.readLine();

					while (read != null) {
						// System.out.println(read);
						sb.append(read);
						read = br.readLine();

					}
					// the entity
					Log.i("tAG", "" + sb.toString());
					return sb.toString();
				}
			} else {
				// the entity
				System.out.println("Response code is:: " + response.getStatusLine().getStatusCode());
				sb.append(statuscode);
				return sb.toString();
			}

		} catch (Exception e) {
			e.printStackTrace();
			sb.append(e.getLocalizedMessage());
			Log.i("connection", "Cannot Estabilish Connection! " + e.getLocalizedMessage());
		}

		return sb.toString();
	}

	/** @return --- str_response -- {@link String} */
	public static String postMethodWay(String hostName, String param) {
		String str_response = null;
		HttpResponse response = null;
		HttpClient myClient = new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(myClient.getParams(), CONNECTIONTIMEOUT);
		HttpConnectionParams.setSoTimeout(myClient.getParams(), CONNECTIONTIMEOUT);
		HttpPost myConnection = new HttpPost(hostName + param);

		try {
			response = myClient.execute(myConnection);
			int statuscode = response.getStatusLine().getStatusCode();
			if (statuscode == 200) {
				str_response = EntityUtils.toString(response.getEntity(), "UTF-8");
			} else {
				str_response = String.valueOf(statuscode);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return str_response;
	}

	public static String getJSONFromUrl(String url, List<NameValuePair> params) {
		InputStream is = null;
		JSONObject jObj = null;
		String jsonResponse = "";
		System.out.println("URL :" + url);
		Log.i("PARAM", "Size: " + params.size());
		for (int i = 0; i < params.size(); i++) {
			System.out.println("Param \n " + params.get(i).getName() + "  Vaue: " + params.get(i).getValue());
		}

		// Making HTTP request
		try {
			// defaultHttpClient
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(new UrlEncodedFormEntity(params));
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();

		} catch (UnsupportedEncodingException e) {
			System.out.println("Error is" + e);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);

			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			jsonResponse = sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}

		return jsonResponse;

	}

	public static String getCurrentDateTime() {
		String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		return date;
	}

	// Saving DataClass details
	public static void saveWellBeingClass(final Activity mContext, WellBeing wellBeingClass) {
		SharedPreferences tfdsPrefs = mContext.getSharedPreferences("wellBeingPrefs", Context.MODE_PRIVATE);
		SharedPreferences.Editor prefsEditor = tfdsPrefs.edit();
		try {
			prefsEditor.putString(WELLBEINGCLASS, ObjectSerializer.serialize(wellBeingClass));
		} catch (IOException e) {
			e.printStackTrace();
		}
		prefsEditor.commit();
	}

	// Fetching DataClass details
	public static WellBeing fetchWellBeingClass(final Context mContext) {
		SharedPreferences tfdsPrefs = mContext.getSharedPreferences("wellBeingPrefs", Context.MODE_PRIVATE);
		WellBeing wellBeingClass = null;
		String serializeOrg = tfdsPrefs.getString(WELLBEINGCLASS, null);
		try {
			if (serializeOrg != null) {
				wellBeingClass = (WellBeing) ObjectSerializer.deserialize(serializeOrg);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return wellBeingClass;
	}

	public static void showCallBackMessageWithOkCancel(final Context mContext, final String message, final AlertDialogCallBack callBack) {
		((Activity) mContext).runOnUiThread(new Runnable() {

			public void run() {
				final AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
				alert.setTitle(R.string.app_name);
				alert.setCancelable(false);
				alert.setMessage(message);
				alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						callBack.onSubmit();
					}
				});
				alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int whichButton) {
						callBack.onCancel();
					}
				});
				alert.show();
			}
		});
	}

	// FMS Shared Preference--------------------------------------
	public static SharedPreferences.Editor getSharedEditor(Context context) throws Exception {
		if (context == null) {
			throw new Exception("Context null Exception");
		}
		return getSharedPreference(context).edit();
	}

	public static SharedPreferences getSharedPreference(Context context) throws Exception {
		if (context == null) {
			throw new Exception("Context null Exception");
		}
		return context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
	}

}

package com.seva60plus.hum.nearby;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.seva60plus.hum.R;
import com.seva60plus.hum.activity.DashboardActivity;
import com.seva60plus.hum.activity.HumDetailsView;
import com.seva60plus.hum.activity.MenuLay;
import com.seva60plus.hum.custom.NoInternetPage;
import com.seva60plus.hum.util.ConnectionDetector;
import com.seva60plus.hum.util.Util;

public class NearByMapListInfo extends Activity {

	String mapURL;
	LocationManager lm;
	GoogleMap map;
	Button whatsapp, fb, twitter;
	public static final String MY_PREFS_NAME = "MyPrefsFile";

	Button mapView, listView;
	RelativeLayout backBtn;

	//---by Dibyendu
	// flag for Internet connection status
	Boolean isInternetPresent = false;

	// Connection detector class
	ConnectionDetector cd;

	// ProgressDialog  progress;

	//ProgressDialog progress;

	Dialog progress;
	private AnimationDrawable progressAnimation;

	String eMsg = "";
	String iDestLate = "", iPlaceID = "", iDestLang = "", iDistance = "", iChoice = "";
	String iMyLate = "", iMyLang = "";
	Double myLate, myLang;

	JSONObject json1;

	String destName = "", destAdd = "", destPhno = "", website = "";
	String distance = "";

	TextView nameText, addressText, phoneText, distance_text, web_text;

	RelativeLayout phone_lay, website_lay, getdirect_lay;
	Button call_btn, getdirect_btn, vist_btn;

	View view1, view1copy, view2, view2copy, view3, view3copy, view4, view4copy, view5, view5copy, view6;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nearby_map_info);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		cd = new ConnectionDetector(getApplicationContext());// by Dibyendu

		//------START------------For Progress Spinner--------------

		progress = new Dialog(NearByMapListInfo.this);
		progress.getWindow().setBackgroundDrawableResource(R.drawable.spinner_dialog_backround);

		//Remove the Title
		progress.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

		//progress.setTitle("");

		//Set the View of the Dialog - Custom
		progress.setContentView(R.layout.custom_progress_dialog);

		//Set the title of the Dialog
		//dialog.setTitle("Title...");

		ImageView progressSpinner = (ImageView) progress.findViewById(R.id.progressSpinner);

		//Set the background of the image - In this case an animation (/res/anim folder)
		progressSpinner.setBackgroundResource(R.anim.spinner_progress_animation);

		//Get the image background and attach the AnimationDrawable to it.
		progressAnimation = (AnimationDrawable) progressSpinner.getBackground();

		//Start the animation after the dialog is displayed.
		progress.setOnShowListener(new OnShowListener() {
			@Override
			public void onShow(DialogInterface dialog) {
				progressAnimation.start();
			}

		});

		progress.setCanceledOnTouchOutside(false);
		//-------END-----------For Progress Spinner--------------    ​

		whatsapp = (Button) findViewById(R.id.whatsapp_btn);
		fb = (Button) findViewById(R.id.facebook_btn);
		twitter = (Button) findViewById(R.id.twitter_btn);

		whatsapp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				shareAppLinkViaWhatsApp();
			}
		});

		fb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				shareAppLinkViaFacebook();
			}
		});

		twitter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				shareAppLinkViaTwitter();
			}
		});

		Intent intObj = getIntent();

		iMyLate = intObj.getStringExtra("iMyLate");
		iMyLang = intObj.getStringExtra("iMyLang");
		iDestLate = intObj.getStringExtra("iDestLate");
		iDestLang = intObj.getStringExtra("iDestLang");
		iPlaceID = intObj.getStringExtra("iPlaceID");
		iChoice = intObj.getStringExtra("iChoice");

		System.out.println("CHOICEInfo: " + iChoice);

		nameText = (TextView) findViewById(R.id.name_text);
		addressText = (TextView) findViewById(R.id.address_text);
		phoneText = (TextView) findViewById(R.id.phone_text);
		distance_text = (TextView) findViewById(R.id.distance_text);
		web_text = (TextView) findViewById(R.id.web_text);

		phone_lay = (RelativeLayout) findViewById(R.id.phone_text_lay);
		phone_lay.setVisibility(View.GONE);
		website_lay = (RelativeLayout) findViewById(R.id.website_text_lay);
		website_lay.setVisibility(View.GONE);
		getdirect_lay = (RelativeLayout) findViewById(R.id.getdirect_text_lay);

		call_btn = (Button) findViewById(R.id.call_icon2);
		getdirect_btn = (Button) findViewById(R.id.get_direc_icon2);
		vist_btn = (Button) findViewById(R.id.web_text_icon2);

		view1 = (View) findViewById(R.id.view1);
		view1copy = (View) findViewById(R.id.view1copy);

		view2 = (View) findViewById(R.id.view2);
		view2copy = (View) findViewById(R.id.view2copy);

		view3 = (View) findViewById(R.id.view3);
		view3copy = (View) findViewById(R.id.view3copy);

		view4 = (View) findViewById(R.id.view4);
		view4copy = (View) findViewById(R.id.view4copy);

		view5 = (View) findViewById(R.id.view5);
		view5copy = (View) findViewById(R.id.view5copy);

		view6 = (View) findViewById(R.id.view6);

		view2copy.setVisibility(View.GONE);
		view3.setVisibility(View.GONE);

		view5copy.setVisibility(View.GONE);
		view6.setVisibility(View.GONE);

		System.out.println("NearbyMAplistIntent Value are : " + iDestLate + " : " + iDestLang + " : " + iPlaceID + " : " + iMyLate + " : " + iMyLang);

		findViewById(R.id.menu_icon).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intObj = new Intent(NearByMapListInfo.this, MenuLay.class);
				startActivity(intObj);
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

				//finish();
			}
		});

		listView = (Button) findViewById(R.id.list_btn);
		mapView = (Button) findViewById(R.id.map_btn);

		//----mapView change to listView
		listView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

			}
		});

		//----listView change to mapView
		mapView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				Intent intObj = new Intent(NearByMapListInfo.this, NearByMapListDist.class);
				intObj.putExtra("iMyLate", iMyLate);
				intObj.putExtra("iMyLang", iMyLang);
				intObj.putExtra("iDestLate", iDestLate);
				intObj.putExtra("iDestLang", iDestLang);
				intObj.putExtra("iPlaceID", iPlaceID);
				intObj.putExtra("iChoice", iChoice);
				startActivity(intObj);
				finish();
			}
		});

		LinearLayout banner = (LinearLayout) findViewById(R.id.footerLay);

		banner.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// Take action.
				/*
				 * Intent myIntent = new Intent(MainActivityMap.this,
				 * AdBanner.class); myIntent.putExtra("banner_value", "2");
				 * startActivity(myIntent);
				 */
				/*
				 * Intent i = new Intent(Intent.ACTION_VIEW,
				 * Uri.parse("http://www.homers.in")); startActivity(i);
				 */
			}
		});

		backBtn = (RelativeLayout) findViewById(R.id.back_settings);

		backBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (Util.isInternetAvailable(NearByMapListInfo.this)) {
					Intent i = new Intent(NearByMapListInfo.this, HumDetailsView.class);
					startActivity(i);
					overridePendingTransition(R.anim.slide_in_right, R.anim.slide_leave_right);
				} else {
					Intent i = new Intent(NearByMapListInfo.this, NoInternetPage.class);
					startActivity(i);
					overridePendingTransition(0, 0);
				}

			}
		});

		progress.show();
		new GetLocationListInfo().execute();

	}

	//-----------Get Saathi List
	private class GetLocationListInfo extends AsyncTask<Void, Integer, String> {

		@Override
		protected void onPreExecute() {
			// setting progress bar to zero
			//progressBar.setProgress(0);
			//Caption = txtCaption.getText().toString();
			progress.show();
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			return getLocationListInfo();
		}

		@SuppressWarnings("deprecation")
		private String getLocationListInfo() {

			try {

				//https://maps.googleapis.com/maps/api/place/details/json?placeid=ChIJXSAtv9B2AjoRUSEr7YS-ZJM&sensor=false&key=AIzaSyDLb2iB_8R_hA96ORtnmM77nkQYPvrcBO4
				// city = new LatLng(myLate, myLang);
				//AIzaSyDLb2iB_8R_hA96ORtnmM77nkQYPvrcBO4

				String keyString = "AIzaSyDLb2iB_8R_hA96ORtnmM77nkQYPvrcBO4";
				String JSON_BASE = "https://maps.googleapis.com/maps/api/place/details/json?placeid=";
				String placeid = iPlaceID;

				//mapURL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=22.572003,88.4356902&radius=5000&key=AIzaSyDLb2iB_8R_hA96ORtnmM77nkQYPvrcBO4";
				try {

					//https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=22.572003,88.4356902&radius=5000&key=AIzaSyDLb2iB_8R_hA96ORtnmM77nkQYPvrcBO4

					mapURL = JSON_BASE + URLEncoder.encode(placeid, "UTF-8") + "&sensor=false&key=" + keyString;

					System.out.println("URL : " + mapURL);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}

				String str = "";
				HttpResponse response;
				HttpClient myClient = new DefaultHttpClient();
				HttpPost myConnection = new HttpPost(mapURL);
				System.out.println("URL " + mapURL);

				System.out.println("JSON");

				try {
					response = myClient.execute(myConnection);
					str = EntityUtils.toString(response.getEntity(), "UTF-8");

				} catch (ClientProtocolException e) {

					e.printStackTrace();

				} catch (IOException e) {

					e.printStackTrace();
				}

				try {

					System.out.println("TRY");
					JSONObject c = new JSONObject(str);
					// jA1 = c.getJSONArray("results");
					json1 = c.getJSONObject("result");

					System.out.println("json :" + json1);
				} catch (JSONException e) {
					e.printStackTrace();
					eMsg = e.toString();
				}

			} catch (Exception e) {
				// TODO: handle exception
				eMsg = e.toString();
			}

			return eMsg;
		}

		@Override
		protected void onPostExecute(String result) {
			//Log.e(TAG, "Response from server: " + result);

			try {
				destName = json1.getString("name");
				destAdd = json1.getString("formatted_address");
				distance = getDistance(iMyLate, iMyLang, iDestLate, iDestLang);
				destPhno = json1.getString("international_phone_number");
				website = json1.getString("website");

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			nameText.setText(destName);
			addressText.setText(destAdd + "\n");
			if (destPhno.equals("") || destPhno == null) {
				view2copy.setVisibility(View.GONE);
				view3.setVisibility(View.GONE);
				phone_lay.setVisibility(View.GONE);
			} else {
				phone_lay.setVisibility(View.VISIBLE);
				view2copy.setVisibility(View.VISIBLE);
				view3.setVisibility(View.VISIBLE);
				phoneText.setText(destPhno);
			}

			if (website.equals("") || website == null) {
				view5copy.setVisibility(View.GONE);
				view6.setVisibility(View.GONE);
				website_lay.setVisibility(View.GONE);
			} else {
				website_lay.setVisibility(View.VISIBLE);
				view5copy.setVisibility(View.VISIBLE);
				view6.setVisibility(View.VISIBLE);
				web_text.setText(website);
				website_lay.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(website));
						startActivity(i);
					}
				});

				vist_btn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(website));
						startActivity(i);
					}
				});

			}

			distance_text.setText("Distance: " + distance + " K.M");

			phone_lay.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					try {
						Intent my_callIntent = new Intent(Intent.ACTION_CALL);
						my_callIntent.setData(Uri.parse("tel:" + destPhno.replace(" ", "")));
						//here the word 'tel' is important for making a call...
						startActivity(my_callIntent);
					} catch (ActivityNotFoundException e) {
						Toast.makeText(getApplicationContext(), "Error in your phone call" + e.getMessage(), Toast.LENGTH_LONG).show();
					}
				}
			});

			call_btn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					try {
						Intent my_callIntent = new Intent(Intent.ACTION_CALL);
						my_callIntent.setData(Uri.parse("tel:" + destPhno.replace(" ", "")));
						//here the word 'tel' is important for making a call...
						startActivity(my_callIntent);
					} catch (ActivityNotFoundException e) {
						Toast.makeText(getApplicationContext(), "Error in your phone call" + e.getMessage(), Toast.LENGTH_LONG).show();
					}
				}
			});

			progress.dismiss();

			TextView getDirection = (TextView) findViewById(R.id.get_direction_text);
			getDirection.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intObj = new Intent(NearByMapListInfo.this, NearByMapListDist.class);
					intObj.putExtra("iMyLate", iMyLate);
					intObj.putExtra("iMyLang", iMyLang);
					intObj.putExtra("iDestLate", iDestLate);
					intObj.putExtra("iDestLang", iDestLang);
					intObj.putExtra("iPlaceID", iPlaceID);
					intObj.putExtra("iChoice", iChoice);
					startActivity(intObj);
					finish();
				}
			});

			getdirect_btn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intObj = new Intent(NearByMapListInfo.this, NearByMapListDist.class);
					intObj.putExtra("iMyLate", iMyLate);
					intObj.putExtra("iMyLang", iMyLang);
					intObj.putExtra("iDestLate", iDestLate);
					intObj.putExtra("iDestLang", iDestLang);
					intObj.putExtra("iPlaceID", iPlaceID);
					intObj.putExtra("iChoice", iChoice);
					startActivity(intObj);
					finish();
				}
			});

			super.onPostExecute(result);
		}

	}

	//-------------Calculate Distance
	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */

	public String getDistance(String mlat1, String mlon1, String dlat2, String dlon2) {

		double lat1 = Double.parseDouble(mlat1);
		double lon1 = Double.parseDouble(mlon1);
		double lat2 = Double.parseDouble(dlat2);
		double lon2 = Double.parseDouble(dlon2);
		;

		double dist;

		double theta = lon1 - lon2;
		dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;

		dist = dist * 1.609344;//----Distance in Killometers
		System.out.println("Dest Dis = " + dist);
		return String.valueOf(new DecimalFormat("##.##").format(dist));

	}

	/* :: This function converts decimal degrees to radians : */
	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
	private double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
	/* :: This function converts radians to decimal degrees : */
	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
	private double rad2deg(double rad) {
		return (rad * 180 / Math.PI);
	}

	private void shareAppLinkViaFacebook() {
		/*
		 * String urlToShare = "seva60plus.co.in";
		 * 
		 * try { Intent intent1 = new Intent();
		 * intent1.setClassName("com.facebook.katana",
		 * "com.facebook.katana.activity.composer.ImplicitShareIntentHandler");
		 * intent1.setAction("android.intent.action.SEND");
		 * intent1.setType("text/plain");
		 * intent1.putExtra("android.intent.extra.TEXT", urlToShare);
		 * startActivity(intent1); } catch (Exception e) { // If we failed (not
		 * native FB app installed), try share through SEND Intent intent = new
		 * Intent(Intent.ACTION_SEND); intent.setType("text/plain"); String
		 * sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=" +
		 * urlToShare; //String sharerUrl =
		 * "https://www.facebook.com/sharer/sharer.php?u=http://wrctechnologies.com/"
		 * ; intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
		 * startActivity(intent); }
		 */

		Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/Seva60Plus"));
		startActivity(i);
	}

	private void shareAppLinkViaTwitter() {
		String urlToShare = "Please spread the word : Seva60Plus HUM Download Link : https://play.google.com/store/apps/details?id=com.seva60plus.hum";

		try {
			Intent intent1 = new Intent();
			intent1.setClassName("com.twitter.android", "com.twitter.android.PostActivity");
			intent1.setAction("android.intent.action.SEND");
			intent1.setType("text/plain");
			intent1.putExtra("android.intent.extra.TEXT", urlToShare);
			startActivity(intent1);
		} catch (Exception e) {
			// If we failed (not native FB app installed), try share through SEND
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("text/plain");
			String sharerUrl = "https://twitter.com/intent/tweet?text=Please spread the word : Seva60Plus HUM";
			//String sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=http://wrctechnologies.com/";
			intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
			startActivity(intent);
		}

	}

	private void shareAppLinkViaWhatsApp() {
		Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
		whatsappIntent.setType("text/plain");
		whatsappIntent.setPackage("com.whatsapp");
		whatsappIntent.putExtra(Intent.EXTRA_TEXT,
				"Please spread the word : Seva60Plus HUM Download Link : https://play.google.com/store/apps/details?id=com.seva60plus.hum");
		try {
			startActivity(whatsappIntent);
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(getApplicationContext(), "Whatsapp have not been installed.", Toast.LENGTH_SHORT).show();
			//ToastHelper.MakeShortText("Whatsapp have not been installed.");
		}
	}
	public void onHomeClick(View v) {
		startActivity(new Intent(NearByMapListInfo.this, DashboardActivity.class));
		finish();
	}
}

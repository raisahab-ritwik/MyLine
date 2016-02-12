package com.seva60plus.hum.nearby;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.seva60plus.hum.R;
import com.seva60plus.hum.activity.DashboardActivity;
import com.seva60plus.hum.activity.HumDetailsView;
import com.seva60plus.hum.activity.MenuLay;
import com.seva60plus.hum.custom.NoInternetPage;
import com.seva60plus.hum.util.ConnectionDetector;
import com.seva60plus.hum.util.Util;

public class NearByMapListDist extends Activity {

	LocationManager lm;
	GoogleMap map;
	Button whatsapp, fb, twitter;
	public static final String MY_PREFS_NAME = "MyPrefsFile";

	String iMyLate = "", iMyLang = "";
	String iDestLate = "", iDestLang = "", iPlaceID = "", iChoice = "";
	Double myLate, myLang;
	Double destLat, destLong;

	Button mapView, listView;
	RelativeLayout backBtn;

	LatLng origin, dest;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nearby_map_dist_info);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		cd = new ConnectionDetector(getApplicationContext());// by Dibyendu

		//------START------------For Progress Spinner--------------

		progress = new Dialog(NearByMapListDist.this);
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

		System.out.println("Intent Value are : " + iMyLate + " : " + iMyLang + " : " + iDestLate);

		//-----------Convert String to Double	 
		try {
			myLate = Double.parseDouble(iMyLate);
			myLang = Double.parseDouble(iMyLang);
			destLat = Double.parseDouble(iDestLate);
			destLong = Double.parseDouble(iDestLang);

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
		}
		//---------------end Convert=======	

		origin = new LatLng(myLate, myLang);
		dest = new LatLng(destLat, destLong);

		findViewById(R.id.llMenu).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intObj = new Intent(NearByMapListDist.this, MenuLay.class);
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

				Intent intObj = new Intent(NearByMapListDist.this, NearByMapListInfo.class);
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

		//----listView change to mapView
		mapView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

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
				if (Util.isInternetAvailable(NearByMapListDist.this)) {
					Intent i = new Intent(NearByMapListDist.this, HumDetailsView.class);
					startActivity(i);
					overridePendingTransition(R.anim.slide_in_right, R.anim.slide_leave_right);
				} else {
					Intent i = new Intent(NearByMapListDist.this, NoInternetPage.class);
					startActivity(i);
					overridePendingTransition(0, 0);
				}

			}
		});

		progress.show();
		// Get a handle to the Map Fragment
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

		map.moveCamera(CameraUpdateFactory.newLatLngZoom(origin, 11));

		Marker m = map.addMarker(new MarkerOptions().position(origin).icon(BitmapDescriptorFactory.fromResource(R.drawable.userpin)));

		System.out.println("CHOICE: " + iChoice);

		if (iChoice.contains("atm")) {

			Marker m2 = map.addMarker(new MarkerOptions().position(dest).icon(BitmapDescriptorFactory.fromResource(R.drawable.atm)));
		} else if (iChoice.contains("bank")) {

			Marker m2 = map.addMarker(new MarkerOptions().position(dest).icon(BitmapDescriptorFactory.fromResource(R.drawable.bank)));
		} else if (iChoice.contains("hospital")) {

			Marker m2 = map.addMarker(new MarkerOptions().position(dest).icon(BitmapDescriptorFactory.fromResource(R.drawable.hospital)));
		} else if (iChoice.contains("airport")) {

			Marker m2 = map.addMarker(new MarkerOptions().position(dest).icon(BitmapDescriptorFactory.fromResource(R.drawable.airport)));
		} else if (iChoice.contains("food")) {

			Marker m2 = map.addMarker(new MarkerOptions().position(dest).icon(BitmapDescriptorFactory.fromResource(R.drawable.resturant)));
		} else if (iChoice.contains("lodging")) {

			Marker m2 = map.addMarker(new MarkerOptions().position(dest).icon(BitmapDescriptorFactory.fromResource(R.drawable.hotel)));
		} else if (iChoice.contains("taxi")) {

			Marker m2 = map.addMarker(new MarkerOptions().position(dest).icon(BitmapDescriptorFactory.fromResource(R.drawable.taxi)));
		} else if (iChoice.contains("bus")) {

			Marker m2 = map.addMarker(new MarkerOptions().position(dest).icon(BitmapDescriptorFactory.fromResource(R.drawable.bus)));
		} else if (iChoice.contains("movie")) {

			Marker m2 = map.addMarker(new MarkerOptions().position(dest).icon(BitmapDescriptorFactory.fromResource(R.drawable.cinema)));
		} else if (iChoice.contains("shopping")) {

			Marker m2 = map.addMarker(new MarkerOptions().position(dest).icon(BitmapDescriptorFactory.fromResource(R.drawable.shop)));
		} else if (iChoice.contains("police")) {

			Marker m2 = map.addMarker(new MarkerOptions().position(dest).icon(BitmapDescriptorFactory.fromResource(R.drawable.police)));
		} else if (iChoice.contains("park")) {

			Marker m2 = map.addMarker(new MarkerOptions().position(dest).icon(BitmapDescriptorFactory.fromResource(R.drawable.park)));
		} else if (iChoice.contains("gas")) {

			Marker m2 = map.addMarker(new MarkerOptions().position(dest).icon(BitmapDescriptorFactory.fromResource(R.drawable.gas)));
		} else {

		}

		/*
		 * Marker m2 = map.addMarker(new MarkerOptions() .position(dest)
		 * .icon(BitmapDescriptorFactory
		 * .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
		 */
		// Getting URL to the Google Directions API
		String url = getDirectionsUrl(origin, dest);

		DownloadTask downloadTask = new DownloadTask();

		// Start downloading json data from Google Directions API
		downloadTask.execute(url);

	}

	private String getDirectionsUrl(LatLng origin, LatLng dest) {

		System.out.println("----" + origin);
		// Origin of route
		String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

		// Destination of route
		String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

		// Sensor enabled
		String sensor = "sensor=false";

		// Building the parameters to the web service
		String parameters = str_origin + "&" + str_dest + "&" + sensor;

		// Output format
		String output = "json";

		// Building the url to the web service
		String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

		return url;
	}

	/** A method to download json data from url */
	private String downloadUrl(String strUrl) throws IOException {
		String data = "";
		InputStream iStream = null;
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(strUrl);

			// Creating an http connection to communicate with url 
			urlConnection = (HttpURLConnection) url.openConnection();

			// Connecting to url 
			urlConnection.connect();

			// Reading data from url 
			iStream = urlConnection.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

			StringBuffer sb = new StringBuffer();

			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			data = sb.toString();

			br.close();

		} catch (Exception e) {
			Log.d("Exception while downloading url", e.toString());
		} finally {
			iStream.close();
			urlConnection.disconnect();
		}
		return data;
	}

	// Fetches data from url passed
	private class DownloadTask extends AsyncTask<String, Void, String> {

		// Downloading data in non-ui thread
		@Override
		protected String doInBackground(String... url) {

			// For storing data from web service
			String data = "";

			try {
				// Fetching the data from web service
				data = downloadUrl(url[0]);
			} catch (Exception e) {
				Log.d("Background Task", e.toString());
			}
			return data;
		}

		// Executes in UI thread, after the execution of
		// doInBackground()
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			ParserTask parserTask = new ParserTask();

			// Invokes the thread for parsing the JSON data
			parserTask.execute(result);

		}
	}

	/** A class to parse the Google Places in JSON format */
	private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

		// Parsing the data in non-ui thread    	
		@Override
		protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

			JSONObject jObject;
			List<List<HashMap<String, String>>> routes = null;

			try {
				jObject = new JSONObject(jsonData[0]);
				DirectionsJSONParser parser = new DirectionsJSONParser();

				// Starts parsing data
				routes = parser.parse(jObject);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return routes;
		}

		// Executes in UI thread, after the parsing process
		@Override
		protected void onPostExecute(List<List<HashMap<String, String>>> result) {
			ArrayList<LatLng> points = null;
			PolylineOptions lineOptions = null;
			MarkerOptions markerOptions = new MarkerOptions();

			// Traversing through all the routes
			for (int i = 0; i < result.size(); i++) {
				points = new ArrayList<LatLng>();
				lineOptions = new PolylineOptions();

				// Fetching i-th route
				List<HashMap<String, String>> path = result.get(i);

				// Fetching all the points in i-th route
				for (int j = 0; j < path.size(); j++) {
					HashMap<String, String> point = path.get(j);

					double lat = Double.parseDouble(point.get("lat"));
					double lng = Double.parseDouble(point.get("lng"));
					LatLng position = new LatLng(lat, lng);

					points.add(position);
				}

				// Adding all the points in the route to LineOptions
				lineOptions.addAll(points);
				lineOptions.width(10);
				lineOptions.color(Color.BLUE);

			}

			// Drawing polyline in the Google Map for the i-th route
			map.addPolyline(lineOptions);

			progress.dismiss();
		}
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
		startActivity(new Intent(NearByMapListDist.this, DashboardActivity.class));
		finish();
	}
}

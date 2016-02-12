package com.seva60plus.hum.nearby;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.seva60plus.hum.R;
import com.seva60plus.hum.activity.DashboardActivity;
import com.seva60plus.hum.activity.HumDetailsView;
import com.seva60plus.hum.activity.MenuLay;
import com.seva60plus.hum.custom.NoInternetPage;
import com.seva60plus.hum.custom.SlowInternetPage;
import com.seva60plus.hum.sharelocation.MapListData;
import com.seva60plus.hum.sharelocation.MapListLazyAdapter;
import com.seva60plus.hum.util.ConnectionDetector;
import com.seva60plus.hum.util.Util;

public class MainActivityMap extends Activity {

	String mapURL;
	Button whatsapp, fb, twitter;

	LocationManager lm;
	GoogleMap map;
	private Object mMelbourne;
	String u_id;
	private View myContentsView;
	double lat;
	double lon;
	Circle circle;
	private Context mContext;
	public static final String MY_PREFS_NAME = "MyPrefsFile";
	String ShareUserID, ShareCustID, CheckUser;
	String cityName = "0", custID;
	Button user;
	Button bikeBtn;
	Button rental;
	String CustomerIDFROM;

	String iMyLate = "", iPlace = "", iMyLang = "";
	Double myLate, myLang;

	String jType;

	ArrayList<MapListData> fDatas = new ArrayList<MapListData>();

	ListView list;
	Button mapView, listView;
	RelativeLayout backBtn;
	RelativeLayout listlay, maplay;
	LinearLayout spinnerLay;
	Spinner type, area;

	String[] values = new String[] { "ATM", "HOSPITAL", "POLICE", "BANK", "FOOD", "RESTAURANT", "PARK" };

	String[] km = new String[] { "1km", "2km", "3km", "4km", "5km" };

	String selectedType, selectedArea;

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

	JSONArray jA1;
	JSONObject json1 = null;

	String jlat, jlang, jName, jLocatiion;
	LatLng city;

	String jPlaceID;

	String mapRadius = "5000";

	RelativeLayout helpLay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_map);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		cd = new ConnectionDetector(getApplicationContext());// by Dibyendu

		//------START------------For Progress Spinner--------------

		progress = new Dialog(MainActivityMap.this);
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
		iMyLate = intObj.getStringExtra("myLate");
		iMyLang = intObj.getStringExtra("myLang");
		iPlace = intObj.getStringExtra("iChoice");

		System.out.println("Intent Value are : " + iMyLate + " : " + iMyLang + " : " + iPlace);

		//-----------Convert String to Double	 
		try {
			myLate = Double.parseDouble(iMyLate);
			myLang = Double.parseDouble(iMyLang);

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
		}
		//---------------end Convert=======	

		spinnerLay = (LinearLayout) findViewById(R.id.subheaderLay1);
		/*
		 * if(iSpinner.equals("0")){
		 * spinnerLay.setVisibility(View.VISIBLE);//---Here Was Gone--22.06.15 }
		 * else{ spinnerLay.setVisibility(View.VISIBLE); }
		 */

		listlay = (RelativeLayout) findViewById(R.id.sv_items);
		maplay = (RelativeLayout) findViewById(R.id.map_lay);

		list = (ListView) findViewById(R.id.map_list);

		findViewById(R.id.llMenu).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intObj = new Intent(MainActivityMap.this, MenuLay.class);
				startActivity(intObj);
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

				//finish();
			}
		});

		helpLay = (RelativeLayout) findViewById(R.id.helpLay);
		helpLay.setVisibility(View.VISIBLE);
		helpLay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intObj = new Intent(MainActivityMap.this, HelpNearbyMapPage.class);
				startActivity(intObj);
			}
		});

		listView = (Button) findViewById(R.id.list_btn);
		mapView = (Button) findViewById(R.id.map_btn);

		//----mapView change to listView
		listView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				listlay.setVisibility(View.VISIBLE);
				maplay.setVisibility(View.GONE);
				//mapView.setTextColor(Color.MAGENTA);
				listView.setTextColor(Color.parseColor("#ffffff"));
				listView.setBackgroundColor(Color.parseColor("#404041"));
				mapView.setTextColor(Color.parseColor("#404041"));
				mapView.setBackgroundColor(Color.parseColor("#ffffff"));
			}
		});

		//----listView change to mapView
		mapView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				maplay.setVisibility(View.VISIBLE);
				listlay.setVisibility(View.GONE);
				//listView.setTextColor(Color.RED);
				mapView.setTextColor(Color.parseColor("#ffffff"));
				mapView.setBackgroundColor(Color.parseColor("#404041"));
				listView.setTextColor(Color.parseColor("#404041"));
				listView.setBackgroundColor(Color.parseColor("#ffffff"));

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
				if (Util.isInternetAvailable(mContext)) {
					Intent i = new Intent(MainActivityMap.this, HumDetailsView.class);
					startActivity(i);
					overridePendingTransition(R.anim.slide_in_right, R.anim.slide_leave_right);
				} else {
					Intent i = new Intent(mContext, NoInternetPage.class);
					startActivity(i);
					overridePendingTransition(0, 0);
				}

			}
		});

		progress.show();
		// Get a handle to the Map Fragment
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapView)).getMap();

		//  map.setInfoWindowAdapter(new InfoWindowAdapterMarker(mContext));
		//AIzaSyASEqZG8-zGBpxF5eZ_eBlgzO1-G629iCc

		//     map.setMyLocationEnabled(true);

		new GetLocationList().execute();

		//----------------Loder timing Check-------26.08.15

		if (progress.isShowing()) {
			//is running
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {

					// not getting-----!
					if (progress.isShowing()) {
						System.out.println("Pro=====1");
						progress.dismiss();
						//Toast.makeText(getApplicationContext(), "Please try again", Toast.LENGTH_SHORT).show();

						Intent intObj = new Intent(MainActivityMap.this, SlowInternetPage.class);
						startActivity(intObj);
						overridePendingTransition(0, 0);
					} else {

					}

				}
			}, 20000);
		} else {
			//----Getit Done
			System.out.println("Pro=====2");
		}

		//=============== -------end Loader timing ====26.08.15

	}

	//-----------Get Saathi List
	private class GetLocationList extends AsyncTask<Void, Integer, String> {

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
			return getLocationList();
		}

		@SuppressWarnings("deprecation")
		private String getLocationList() {

			try {
				city = new LatLng(myLate, myLang);

				String keyString = "AIzaSyDLb2iB_8R_hA96ORtnmM77nkQYPvrcBO4";
				String JSON_BASE = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=";
				String types = iPlace;

				//mapURL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=22.572003,88.4356902&radius=5000&key=AIzaSyDLb2iB_8R_hA96ORtnmM77nkQYPvrcBO4";
				try {

					//https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=22.572003,88.4356902&radius=5000&key=AIzaSyDLb2iB_8R_hA96ORtnmM77nkQYPvrcBO4

					mapURL = JSON_BASE + myLate + "," + myLang + "&radius=" + mapRadius + "&sensor=false&types=" + URLEncoder.encode(types, "UTF-8") + "&key="
							+ keyString;

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

				} catch (Exception e) {
					e.printStackTrace();
				}

				try {

					System.out.println("TRY");
					JSONObject c = new JSONObject(str);
					jA1 = c.getJSONArray("results");

					if (jA1.length() == 0) {

						if (mapRadius == "5000") {
							mapRadius = "10000";
							new GetLocationList().execute();
						} else if (mapRadius == "10000") {
							mapRadius = "20000";
							new GetLocationList().execute();
						} else {

							System.out.println("jA1 :" + jA1.length());
							Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_SHORT).show();

						}

					} else {
						System.out.println("jA1 :" + jA1.length());

					}

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

		@SuppressWarnings("unchecked")
		@Override
		protected void onPostExecute(String result) {

			fDatas.clear();

			map.moveCamera(CameraUpdateFactory.newLatLngZoom(city, 13));

			Marker j = map.addMarker(new MarkerOptions().position(city).title("My Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.userpin)));

			try {

			} catch (Exception e) {
				// TODO: handle exception
			}

			int countNum = jA1.length();

			if (countNum == 0) {

			} else {

				for (int i = 0; i <= countNum; i++) {
					try {

						json1 = jA1.getJSONObject(i);

						JSONObject json2 = json1.getJSONObject("geometry");
						JSONObject json3 = json2.getJSONObject("location");

						jlat = json3.getString("lat");
						jlang = json3.getString("lng");

						jPlaceID = json1.getString("place_id");

						jName = json1.getString("name");
						jLocatiion = json1.getString("vicinity");
						jType = json1.getString("types");

						System.out.println("Type " + jType);

					} catch (Exception e) {
						e.printStackTrace();
					}
					System.out.println("Name : " + jName + " Location : " + jLocatiion);
					lat = Double.parseDouble(jlat);
					lon = Double.parseDouble(jlang);

					String segments = jType.replaceAll("[^a-zA-Z]", " ");
					System.out.println(" NAME TYPR :" + segments);

					String s = "I want to walk my dog";

					String[] arr = segments.split(" ");

					for (String ss : arr) {

						//if(ss.contains("atm") || ss.contains("bank")){

						if (iPlace.equals("atm") && ss.contains("atm")) {

							MapListData fData = new MapListData();
							fData.FimageUrl = "http://www.belindaclark.co.uk/images/location_icon.fw.png";
							fData.Fname = jName;
							fData.FuID = jLocatiion;
							fData.image = R.drawable.big_atm;
							fData.destLate = Double.toString(lat);
							fData.destLang = Double.toString(lon);

							fData.place_id = jPlaceID;
							fData.distance_km = getDistance(iMyLate, iMyLang, Double.toString(lat), Double.toString(lon));
							fData.place_type = iPlace;

							fDatas.add(fData);

							Marker m = map.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title(jName).snippet("Address : " + jLocatiion)

							.icon(BitmapDescriptorFactory.fromResource(R.drawable.atm)));
						} else if (iPlace.equals("bank") && ss.contains("bank")) {

							MapListData fData = new MapListData();
							fData.FimageUrl = "http://www.belindaclark.co.uk/images/location_icon.fw.png";
							fData.Fname = jName;
							fData.FuID = jLocatiion;
							fData.image = R.drawable.big_bank;
							fData.destLate = Double.toString(lat);
							fData.destLang = Double.toString(lon);

							fData.place_id = jPlaceID;
							fData.distance_km = getDistance(iMyLate, iMyLang, Double.toString(lat), Double.toString(lon));

							fData.place_type = iPlace;
							fDatas.add(fData);

							Marker m = map.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title(jName).snippet("Address : " + jLocatiion)

							.icon(BitmapDescriptorFactory.fromResource(R.drawable.bank)));
						}

						//}
						/*
						 * else if(ss.contains("bank")){
						 * 
						 * MapListData fData = new MapListData();
						 * fData.FimageUrl =
						 * "http://www.belindaclark.co.uk/images/location_icon.fw.png"
						 * ; fData.Fname = jName; fData.FuID = jLocatiion;
						 * fData.image = R.drawable.big_bank; fDatas.add(fData);
						 * 
						 * Marker m = map.addMarker(new MarkerOptions()
						 * .position(new LatLng(lat, lon)) .title(jName)
						 * .snippet("Address : "+jLocatiion)
						 * 
						 * .icon(BitmapDescriptorFactory.fromResource(R.drawable.
						 * bank))); }
						 */
						else if (ss.equals("hospital")) {

							MapListData fData = new MapListData();
							fData.FimageUrl = "http://www.belindaclark.co.uk/images/location_icon.fw.png";
							fData.Fname = jName;
							fData.FuID = jLocatiion;
							fData.image = R.drawable.big_hospital;
							fData.destLate = Double.toString(lat);
							fData.destLang = Double.toString(lon);

							fData.place_id = jPlaceID;
							fData.distance_km = getDistance(iMyLate, iMyLang, Double.toString(lat), Double.toString(lon));

							fData.place_type = iPlace;
							fDatas.add(fData);

							Marker m = map.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title(jName).snippet("Address : " + jLocatiion)

							.icon(BitmapDescriptorFactory.fromResource(R.drawable.hospital)));
						}

						else if (ss.equals("police")) {

							MapListData fData = new MapListData();
							fData.FimageUrl = "http://www.belindaclark.co.uk/images/location_icon.fw.png";
							fData.Fname = jName;
							fData.FuID = jLocatiion;
							fData.image = R.drawable.big_policestation;
							fData.destLate = Double.toString(lat);
							fData.destLang = Double.toString(lon);

							fData.place_id = jPlaceID;
							fData.distance_km = getDistance(iMyLate, iMyLang, Double.toString(lat), Double.toString(lon));

							fData.place_type = iPlace;
							fDatas.add(fData);

							Marker m = map.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title(jName).snippet("Address : " + jLocatiion)

							.icon(BitmapDescriptorFactory.fromResource(R.drawable.police)));
						}

						else if (ss.equals("airport")) {

							MapListData fData = new MapListData();
							fData.FimageUrl = "http://www.belindaclark.co.uk/images/location_icon.fw.png";
							fData.Fname = jName;
							fData.FuID = jLocatiion;
							fData.image = R.drawable.big_airport;
							fData.destLate = Double.toString(lat);
							fData.destLang = Double.toString(lon);

							fData.place_id = jPlaceID;
							fData.distance_km = getDistance(iMyLate, iMyLang, Double.toString(lat), Double.toString(lon));

							fData.place_type = iPlace;
							fDatas.add(fData);

							Marker m = map.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title(jName).snippet("Address : " + jLocatiion)

							.icon(BitmapDescriptorFactory.fromResource(R.drawable.airport)));
						}

						//else if(ss.equals("food") || ss.equals("lodging")){

						else if (iPlace.equals("food") && ss.equals("food")) {

							MapListData fData = new MapListData();
							fData.FimageUrl = "http://www.belindaclark.co.uk/images/location_icon.fw.png";
							fData.Fname = jName;
							fData.FuID = jLocatiion;
							fData.image = R.drawable.big_resturant;
							fData.destLate = Double.toString(lat);
							fData.destLang = Double.toString(lon);

							fData.place_id = jPlaceID;
							fData.distance_km = getDistance(iMyLate, iMyLang, Double.toString(lat), Double.toString(lon));

							fData.place_type = iPlace;
							fDatas.add(fData);

							Marker m = map.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title(jName).snippet("Address : " + jLocatiion)

							.icon(BitmapDescriptorFactory.fromResource(R.drawable.resturant)));
						} else if (iPlace.equals("lodging") && ss.equals("lodging")) {

							MapListData fData = new MapListData();
							fData.FimageUrl = "http://www.belindaclark.co.uk/images/location_icon.fw.png";
							fData.Fname = jName;
							fData.FuID = jLocatiion;
							fData.image = R.drawable.big_hotel;
							fData.destLate = Double.toString(lat);
							fData.destLang = Double.toString(lon);

							fData.place_id = jPlaceID;
							fData.distance_km = getDistance(iMyLate, iMyLang, Double.toString(lat), Double.toString(lon));

							fData.place_type = iPlace;
							fDatas.add(fData);

							Marker m = map.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title(jName).snippet("Address : " + jLocatiion)

							.icon(BitmapDescriptorFactory.fromResource(R.drawable.hotel)));

						}

						//}
						else if (ss.equals("taxi")) {

							MapListData fData = new MapListData();
							fData.FimageUrl = "http://www.belindaclark.co.uk/images/location_icon.fw.png";
							fData.Fname = jName;
							fData.FuID = jLocatiion;
							fData.image = R.drawable.big_taxi;
							fData.destLate = Double.toString(lat);
							fData.destLang = Double.toString(lon);

							fData.place_id = jPlaceID;
							fData.distance_km = getDistance(iMyLate, iMyLang, Double.toString(lat), Double.toString(lon));

							fData.place_type = iPlace;
							fDatas.add(fData);

							Marker m = map.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title(jName).snippet("Address : " + jLocatiion)

							.icon(BitmapDescriptorFactory.fromResource(R.drawable.taxi)));
						}

						else if (ss.equals("bus")) {

							MapListData fData = new MapListData();
							fData.FimageUrl = "http://www.belindaclark.co.uk/images/location_icon.fw.png";
							fData.Fname = jName;
							fData.FuID = jLocatiion;
							fData.image = R.drawable.big_bus;
							fData.destLate = Double.toString(lat);
							fData.destLang = Double.toString(lon);

							fData.place_id = jPlaceID;
							fData.distance_km = getDistance(iMyLate, iMyLang, Double.toString(lat), Double.toString(lon));

							fData.place_type = iPlace;
							fDatas.add(fData);

							Marker m = map.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title(jName).snippet("Address : " + jLocatiion)

							.icon(BitmapDescriptorFactory.fromResource(R.drawable.bus)));
						}

						/*
						 * else if(ss.equals("lodging")){
						 * 
						 * MapListData fData = new MapListData();
						 * fData.FimageUrl =
						 * "http://www.belindaclark.co.uk/images/location_icon.fw.png"
						 * ; fData.Fname = jName; fData.FuID = jLocatiion;
						 * fData.image = R.drawable.big_hotel;
						 * fDatas.add(fData);
						 * 
						 * Marker m = map.addMarker(new MarkerOptions()
						 * .position(new LatLng(lat, lon)) .title(jName)
						 * .snippet("Address : "+jLocatiion)
						 * 
						 * .icon(BitmapDescriptorFactory.fromResource(R.drawable.
						 * hotel))); }
						 */
						else if (ss.equals("movie")) {

							MapListData fData = new MapListData();
							fData.FimageUrl = "http://www.belindaclark.co.uk/images/location_icon.fw.png";
							fData.Fname = jName;
							fData.FuID = jLocatiion;
							fData.image = R.drawable.big_cinema;
							fData.destLate = Double.toString(lat);
							fData.destLang = Double.toString(lon);

							fData.place_id = jPlaceID;
							fData.distance_km = getDistance(iMyLate, iMyLang, Double.toString(lat), Double.toString(lon));

							fData.place_type = iPlace;
							fDatas.add(fData);

							Marker m = map.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title(jName).snippet("Address : " + jLocatiion)

							.icon(BitmapDescriptorFactory.fromResource(R.drawable.cinema)));
						}

						else if (ss.equals("shopping")) {

							MapListData fData = new MapListData();
							fData.FimageUrl = "http://www.belindaclark.co.uk/images/location_icon.fw.png";
							fData.Fname = jName;
							fData.FuID = jLocatiion;
							fData.image = R.drawable.big_shopping;
							fData.destLate = Double.toString(lat);
							fData.destLang = Double.toString(lon);

							fData.place_id = jPlaceID;
							fData.distance_km = getDistance(iMyLate, iMyLang, Double.toString(lat), Double.toString(lon));

							fData.place_type = iPlace;
							fDatas.add(fData);

							Marker m = map.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title(jName).snippet("Address : " + jLocatiion)

							.icon(BitmapDescriptorFactory.fromResource(R.drawable.shop)));
						}

						else if (ss.equals("park") || ss.equals("amusement_park") || ss.equals("zoo")) {

							MapListData fData = new MapListData();
							fData.FimageUrl = "http://www.belindaclark.co.uk/images/location_icon.fw.png";
							fData.Fname = jName;
							fData.FuID = jLocatiion;
							fData.image = R.drawable.big_park;
							fData.destLate = Double.toString(lat);
							fData.destLang = Double.toString(lon);

							fData.place_id = jPlaceID;
							fData.distance_km = getDistance(iMyLate, iMyLang, Double.toString(lat), Double.toString(lon));

							fData.place_type = iPlace;
							fDatas.add(fData);

							Marker m = map.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title(jName).snippet("Address : " + jLocatiion)

							.icon(BitmapDescriptorFactory.fromResource(R.drawable.park)));
						}

						else if (ss.equals("gas")) {

							MapListData fData = new MapListData();
							fData.FimageUrl = "http://www.belindaclark.co.uk/images/location_icon.fw.png";
							fData.Fname = jName;
							fData.FuID = jLocatiion;
							fData.image = R.drawable.big_gas;
							fData.destLate = Double.toString(lat);
							fData.destLang = Double.toString(lon);

							fData.place_id = jPlaceID;
							fData.distance_km = getDistance(iMyLate, iMyLang, Double.toString(lat), Double.toString(lon));

							fData.place_type = iPlace;
							fDatas.add(fData);

							Marker m = map.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title(jName).snippet("Address : " + jLocatiion)

							.icon(BitmapDescriptorFactory.fromResource(R.drawable.gas)));
						}

						else {
							System.out.println("TTE :" + ss);
						}
					}

				}

				//Collections.sort(fDatas);
				System.out.println("\n\nMAP List---" + fDatas);
				//-----Dibyendu Change 22.06.15

				//Collections.sort(fDatas, comparator)

				Collections.sort(fDatas, new Comparator() {

					public int compare(Object o1, Object o2) {
						MapListData p1 = (MapListData) o1;
						MapListData p2 = (MapListData) o2;
						return p1.getDistanceKM().compareToIgnoreCase(p2.getDistanceKM());
					}

				});

				//---------------

				MapListLazyAdapter adapter = new MapListLazyAdapter(getApplicationContext(), fDatas);
				System.out.println("LIST ADAPTER: ");
				list.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				list.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						// TODO Auto-generated method stub
						MapListData mData = (MapListData) list.getItemAtPosition(position);

						//String distance = getDistance(iMyLate, iMyLang, mData.getDestLate(), mData.getDestLang());

						System.out.println("CHOICE:Main " + iPlace);

						Intent intObj = new Intent(MainActivityMap.this, NearByMapListInfo.class);
						intObj.putExtra("iMyLate", iMyLate);
						intObj.putExtra("iMyLang", iMyLang);
						intObj.putExtra("iDestLate", mData.getDestLate());
						intObj.putExtra("iDestLang", mData.getDestLang());
						intObj.putExtra("iPlaceID", mData.getPlaceID());
						intObj.putExtra("iChoice", iPlace);
						startActivity(intObj);

					}
				});

				//----End
			}

			progress.dismiss();

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
		startActivity(new Intent(mContext, DashboardActivity.class));
		finish();
	}
}

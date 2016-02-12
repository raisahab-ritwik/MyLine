package com.seva60plus.hum.sharelocation;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.seva60plus.hum.R;
import com.seva60plus.hum.activity.DashboardActivity;
import com.seva60plus.hum.activity.HumDetailsView;
import com.seva60plus.hum.activity.MenuLay;
import com.seva60plus.hum.custom.NoInternetPage;
import com.seva60plus.hum.util.ConnectionDetector;
import com.seva60plus.hum.util.Util;

public class MapActivity extends FragmentActivity {
	
	GoogleMap mMap;
	Marker mMarker;
	LocationManager lm;
	double lat, lng;
	double latitude, longitude;
	ImageView homeBtn;
	RelativeLayout backBtn;
	TextView headerTitleText;
	Button shareLocation;
	AppLocationService appLocationService;
	TextView tvAddress;
	String locationAddressMessage = "";
	String Late = "", Lang = "";

	// ---by Dibyendu
	// flag for Internet connection status
	Boolean isInternetPresent = false;

	// Connection detector class
	ConnectionDetector cd;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		appLocationService = new AppLocationService(MapActivity.this);

		cd = new ConnectionDetector(getApplicationContext());// by Dibyendu
		backBtn = (RelativeLayout) findViewById(R.id.back_settings);

		backBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (Util.isInternetAvailable(MapActivity.this)) {
					Intent myIntent = new Intent(MapActivity.this, HumDetailsView.class);
					startActivity(myIntent);
				} else {
					Intent i = new Intent(MapActivity.this, NoInternetPage.class);
					startActivity(i);
					overridePendingTransition(0, 0);
				}
			}
		});

		findViewById(R.id.llMenu).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// Take action.
				Intent myIntent = new Intent(MapActivity.this, MenuLay.class);
				startActivity(myIntent);
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

			}
		});

		shareLocation = (Button) findViewById(R.id.shareLocationBtn);

		shareLocation.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Toast.makeText(MapActivity.this, "Share Location", Toast.LENGTH_SHORT).show();
				Location location = appLocationService.getLocation(LocationManager.GPS_PROVIDER);

				if (location != null) {
					latitude = location.getLatitude();
					longitude = location.getLongitude();
					LocationAddress locationAddress = new LocationAddress();
					locationAddress.getAddressFromLocation(latitude, longitude, getApplicationContext(), new GeocoderHandler());

				} else {
					showSettingsAlert();
				}
			}

		});

		mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
	}

	LocationListener listener = new LocationListener() {
		public void onLocationChanged(Location loc) {
			LatLng coordinate = new LatLng(loc.getLatitude(), loc.getLongitude());
			lat = loc.getLatitude();
			lng = loc.getLongitude();

			// Toast.makeText(getApplicationContext(),
			// "Lat :: "+lat+"\nLong :: "+lng, Toast.LENGTH_LONG).show();

			/*
			 * LocationAddress locationAddress = new LocationAddress();
			 * locationAddress.getAddressFromLocation(lat, lng,
			 * getApplicationContext(), new GeocoderHandler());
			 * locationAddressMessage=locationAddress.toString();
			 */

			if (mMarker != null)
				mMarker.remove();

			mMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title("Lat :: " + lat + "\nLong :: " + lng));
			// .snippet(locationAddressMessage));

			mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 16));
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onProviderDisabled(String provider) {
		}
	};

	public void onResume() {
		super.onResume();

		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		boolean isNetwork = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		boolean isGPS = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

		if (isNetwork) {
			lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 10, listener);
			Location loc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			if (loc != null) {
				lat = loc.getLatitude();
				lng = loc.getLongitude();

				// Toast.makeText(getApplicationContext(),
				// " NETWrok Your Location is - \nLat: " + lat + "\nLong: " +
				// lng, Toast.LENGTH_LONG).show();
				/*
				 * Late = Double.toString(lat); Lang = Double.toString(lng);
				 */
			}

		}

		if (isGPS) {
			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, listener);
			Location loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if (loc != null) {
				lat = loc.getLatitude();
				lng = loc.getLongitude();

				// Toast.makeText(getApplicationContext(),
				// " GPS Your Location is - \nLat: " + lat + "\nLong: " + lng,
				// Toast.LENGTH_LONG).show();
				/*
				 * Late = Double.toString(lat); Lang = Double.toString(lng);
				 */
			}

		}
	}

	public void onPause() {
		super.onPause();
		lm.removeUpdates(listener);
	}

	public void showSettingsAlert() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(MapActivity.this);
		alertDialog.setTitle("SETTINGS");
		alertDialog.setMessage("Enable Location Provider! Go to settings menu?");
		alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				MapActivity.this.startActivity(intent);
			}
		});
		//
		alertDialog.setNegativeButton("With Out Internet", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// dialog.cancel();
				Intent intent = new Intent(MapActivity.this, SmsLocation.class);
				// intent.putExtra("msg",locationAddressMessage);

				Late = Double.toString(lat);
				Lang = Double.toString(lng);

				intent.putExtra("lat", Late);
				intent.putExtra("lng", Lang);
				startActivity(intent);

			}
		});
		alertDialog.show();
	}

	private class GeocoderHandler extends Handler {
		@Override
		public void handleMessage(Message message) {
			String locationAddress;
			switch (message.what) {
			case 1:
				Bundle bundle = message.getData();
				locationAddress = bundle.getString("address");
				break;
			default:
				locationAddress = null;
			}
			// tvAddress.setText(locationAddress);
			Late = Double.toString(lat);
			Lang = Double.toString(lng);

			locationAddressMessage = locationAddress.toString();
			Toast.makeText(getApplicationContext(), "LocMsg: " + locationAddressMessage, Toast.LENGTH_LONG).show();

			Intent intent = new Intent(MapActivity.this, SmsLocation.class);
			intent.putExtra("msg", locationAddressMessage);
			intent.putExtra("lat", Late);
			intent.putExtra("lng", Lang);
			startActivity(intent);

		}
	}
	public void onHomeClick(View v) {
		startActivity(new Intent(MapActivity.this, DashboardActivity.class));
		finish();
	}
}

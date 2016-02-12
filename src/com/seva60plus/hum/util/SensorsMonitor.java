package com.seva60plus.hum.util;

/*
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
//import com.wrc.seva60plus.Map.GeocoderHandler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import android.app.Activity;

public class SensorsMonitor extends BroadcastReceiver implements SensorEventListener {
	
	private static final String TAG = SensorsMonitor.class.getSimpleName();
	
	private SensorManager mSensorManager;
	//private SensorEventListener
	private final Sensor mAccelerometer;
	private Context myContext;
	private AudioManager audioMan;
	private Boolean isMutted = false;
	
	private long lastUpdate = -1;
	private float x, y, z;
	private float last_x, last_y, last_z;
	
	AppLocationService appLocationService;
	LocationManager lm;
	double lat, lng;
	double latitude,longitude;
	
	String call1,call2;
	String name1,name2;
	String LocationMsg = "";
	String Lat,Lng;
	//String url = "https://www.google.co.in/maps/@";
	String msgUrl = "";
	
	
	
	public SensorsMonitor(Context context){
		
		
		
		this.myContext = context;
		mSensorManager = (SensorManager) this.myContext.getSystemService(Context.SENSOR_SERVICE);
		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		audioMan = (AudioManager) this.myContext.getSystemService(Context.AUDIO_SERVICE);
	//------------
	//	mSensonListener = new ShakeEventListener();
	
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		//nothing
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (Settings.getOnOffStatus(this.myContext)){
			synchronized (this) {
		        switch (event.sensor.getType()){
		            case Sensor.TYPE_ACCELEROMETER:
		            	long curTime = System.currentTimeMillis();
		            	if ((curTime - lastUpdate) > 100) {
		            		long diffTime = (curTime - lastUpdate);
		            		lastUpdate = curTime;
		            		x = event.values[SensorManager.DATA_X];
							y = event.values[SensorManager.DATA_Y];
							z = event.values[SensorManager.DATA_Z];
							float speed = Math.abs(x + y + z - last_x - last_y - last_z)/ diffTime * 10000;
							if (speed > Settings.getShakeThreshold(this.myContext)) {
					    		this.muteVolume();
							}
							last_x = x;
							last_y = y;
							last_z = z;
						}
		            	break;
		        }
			}
		}
	}
	
	private void sendSms() {
		
	//---------------------Location---------------	
	
		 Location location = appLocationService
                 .getLocation(LocationManager.GPS_PROVIDER);

         //you can hard-code the lat & long if you have issues with getting it
         //remove the below if-condition and use the following couple of lines
         //double latitude = 37.422005;
         //double longitude = -122.084095

         if (location != null) {
              latitude = location.getLatitude();
              longitude = location.getLongitude();           
            	                    
         } else {
            // showSettingsAlert();
         }
         
         LocationListener listener = new LocationListener() {
     	    public void onLocationChanged(Location loc) {
     	    	LatLng coordinate = new LatLng(loc.getLatitude(), loc.getLongitude());
     	    	lat = loc.getLatitude();
     	    	lng = loc.getLongitude();
     	    	
     	    	//Toast.makeText(getApplicationContext(), "Lat :: "+lat+"\nLong :: "+lng, Toast.LENGTH_LONG).show();
     	    	
                /* LocationAddress locationAddress = new LocationAddress();
                 locationAddress.getAddressFromLocation(lat, lng,
                         getApplicationContext(), new GeocoderHandler());
     	    	locationAddressMessage=locationAddress.toString(); */
     	    	
     	    /*	if(mMarker != null)
     	    		mMarker.remove();
     	    	
     	    	mMarker = mMap.addMarker(new MarkerOptions()
     	    	.position(new LatLng(lat, lng))
     	    	.title("Lat :: "+lat+"\nLong :: "+lng));
     	    	//.snippet(locationAddressMessage));
     	    	
     			mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 16));*/
   /* close for changae SUMANGAL  	    }

     	    public void onStatusChanged(String provider, int status, Bundle extras) {}
     	    public void onProviderEnabled(String provider) {}
     	    public void onProviderDisabled(String provider) {}
     	};
     	
     	
     	//	lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

     		boolean isNetwork = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
     		boolean isGPS = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

     		if(isNetwork) {
     			lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 10, listener);
     			Location loc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
     			if(loc != null) {
     				lat = loc.getLatitude();
     			    lng = loc.getLongitude();
     			    
     			  //  Toast.makeText(getApplicationContext(), " NETWrok Your Location is - \nLat: " + lat + "\nLong: " + lng, Toast.LENGTH_LONG).show();
     			  /*  Late = Double.toString(lat);
     			    Lang = Double.toString(lng);*/
 /* close for changae SUMANGAL    			}
     			 
     			
     		}
     		
     		if(isGPS) {
     			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, listener);
     			Location loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
     			if(loc != null) {
     				lat = loc.getLatitude();
     			    lng = loc.getLongitude();
     			    
     			   // Toast.makeText(getApplicationContext(), " GPS Your Location is - \nLat: " + lat + "\nLong: " + lng, Toast.LENGTH_LONG).show();
     			  /*  Late = Double.toString(lat);
     			    Lang = Double.toString(lng);*/
 /* close for changae SUMANGAL    			}
     			
     		}
     		
   //----------------Send SMS------------
     	/*	 SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE); 
             call1 = prefs.getString("saathi1Call", null);
             call2 = prefs.getString("saathi2Call", null);
             name1 = prefs.getString("saathi1CallName", null);
             name2 = prefs.getString("saathi2CallName", null);*/
     		
 /* close for changae SUMANGAL    		Lat = Double.toString(lat);
			Lng = Double.toString(lng);
     		
     		msgUrl = "https://www.google.co.in/maps/@" + Lat + "," + Lng + ",15z?hl=en";
     		LocationMsg = "Help! Help!...Your Friend Mobile Drop \n Your Friend Location is:\n " + msgUrl;
     		
     		Intent i = new Intent(android.content.Intent.ACTION_VIEW);
            i.putExtra("address", call1+"; "+call2);
            // here i can send message to emulator 5556,5558,5560
            // you can change in real device
            i.putExtra("sms_body", LocationMsg);
            i.setType("vnd.android-dir/mms-sms");
          //  startActivity(i);
     		
     		
     	}
      
	private void muteVolume(){
		
		System.out.println("......DONE...");
	}        
	
/*
	private void muteVolume(){
		if (!isMutted && audioMan.getRingerMode() == AudioManager.RINGER_MODE_NORMAL){
			isMutted = true;
			if (Settings.getVirbation(this.myContext)){
				audioMan.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
			} else {
				audioMan.setRingerMode(AudioManager.RINGER_MODE_SILENT);
			}
		}
	}
	*/
/* close for changae SUMANGAL	private void unmuteVolume(){
		if (isMutted){
			isMutted = false;
			audioMan.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
		}
	}
	
	public void resumeSensors(){
		mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	public void pauseSensors(){
		mSensorManager.unregisterListener(this);
		this.unmuteVolume();
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		 SharedPreferences prefs =PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext()); 
	        call1 = prefs.getString("saathi1Call", null);
	        call2 = prefs.getString("saathi2Call", null);
	        name1 = prefs.getString("saathi1CallName", null);
	        name2 = prefs.getString("saathi2CallName", null);
	        
	        System.out.println("No1: "+call1);
		
	}

} */

package com.seva60plus.hum.nearby;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.seva60plus.hum.R;
import com.seva60plus.hum.activity.DashboardActivity;
import com.seva60plus.hum.activity.HumDetailsView;
import com.seva60plus.hum.activity.MenuLay;
import com.seva60plus.hum.custom.NoInternetPage;
import com.seva60plus.hum.custom.SlowInternetPage;
import com.seva60plus.hum.util.ConnectionDetector;
import com.seva60plus.hum.util.GPSTracker;
import com.seva60plus.hum.util.Util;

public class NearByDashboard extends Activity {

	RelativeLayout atm_btn, hospital_btn, bank_btn, police_btn, airport_btn, restaurant_btn, taxi_btn, bus_btn, hotel_btn, cinema_btn, shopping_btn, park_btn,
			gas_btn;;

	RelativeLayout backBtn;
	Button whatsapp, fb, twitter;
	//---by Dibyendu
	// flag for Internet connection status
	Boolean isInternetPresent = false;

	// Connection detector class
	ConnectionDetector cd;
	LocationManager lm;

	GPSTracker gpsTracker;

	//------End

	//ProgressDialog progress;

	Dialog progress;
	private AnimationDrawable progressAnimation;

	Double myLate, myLang;

	public static final String MY_PREFS_NAME = "MyPrefsFile";

	RelativeLayout helpLay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.near_by_dashboard);

		cd = new ConnectionDetector(getApplicationContext());// by Dibyendu
		lm = (LocationManager) getSystemService(LOCATION_SERVICE);// by Dibyendu

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

		//------START------------For Progress Spinner--------------

		progress = new Dialog(NearByDashboard.this);
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
		//-------END-----------For Progress Spinner--------------   

		progress.show();

		gpsTracker = new GPSTracker(this);

		if (gpsTracker.getLatitude() == 0.0 || gpsTracker.getLongitude() == 0.0) {

		} else {

			myLate = gpsTracker.getLatitude();
			myLang = gpsTracker.getLongitude();

			progress.dismiss();
			System.out.println("My Location:" + myLate + " : " + myLang);

		}

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

						Intent intObj = new Intent(NearByDashboard.this, SlowInternetPage.class);
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

		findViewById(R.id.llMenu).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intObj = new Intent(NearByDashboard.this, MenuLay.class);
				startActivity(intObj);
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

				//finish();
			}
		});

		backBtn = (RelativeLayout) findViewById(R.id.back_settings);

		backBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (Util.isInternetAvailable(NearByDashboard.this)) {
					Intent i = new Intent(NearByDashboard.this, HumDetailsView.class);
					startActivity(i);
					overridePendingTransition(R.anim.slide_in_right, R.anim.slide_leave_right);
				} else {
					Intent i = new Intent(NearByDashboard.this, NoInternetPage.class);
					startActivity(i);
					overridePendingTransition(0, 0);
				}

			}
		});

		LinearLayout banner = (LinearLayout) findViewById(R.id.footerLay);

		banner.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// Take action.
				/*
				 * Intent myIntent = new
				 * Intent(OffersListElderCareActivity.this, AdBanner.class);
				 * myIntent.putExtra("banner_value", "2");
				 * startActivity(myIntent);
				 */
				/*
				 * Intent i = new Intent(Intent.ACTION_VIEW,
				 * Uri.parse("http://www.homers.in")); startActivity(i);
				 */
			}
		});

		ImageView backBtnSub = (ImageView) findViewById(R.id.iv_back);
		backBtnSub.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(0, 0);
			}
		});

		helpLay = (RelativeLayout) findViewById(R.id.helpLay);
		helpLay.setVisibility(View.VISIBLE);
		helpLay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intObj = new Intent(NearByDashboard.this, HelpNearbyPage.class);
				startActivity(intObj);
			}
		});

		SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
		editor.putString("prefMyLate", String.valueOf(myLate));
		editor.putString("prefMyLang", String.valueOf(myLang));
		editor.commit();

		//-------------------------------------------------------------------------------
		atm_btn = (RelativeLayout) findViewById(R.id.atm_btn);
		atm_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				System.out.println("My Location:" + myLate + " : " + myLang);

				Intent intObj = new Intent(NearByDashboard.this, MainActivityMap.class);
				intObj.putExtra("myLate", String.valueOf(myLate));
				intObj.putExtra("myLang", String.valueOf(myLang));
				intObj.putExtra("iChoice", "atm");
				startActivity(intObj);
				progress.dismiss();

			}
		});

		bank_btn = (RelativeLayout) findViewById(R.id.bank_btn);
		bank_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				System.out.println("My Location:" + myLate + " : " + myLang);

				Intent intObj = new Intent(NearByDashboard.this, MainActivityMap.class);
				intObj.putExtra("myLate", String.valueOf(myLate));
				intObj.putExtra("myLang", String.valueOf(myLang));
				intObj.putExtra("iChoice", "bank");
				startActivity(intObj);
				progress.dismiss();

			}
		});

		hospital_btn = (RelativeLayout) findViewById(R.id.hospital_btn);
		hospital_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				System.out.println("My Location:" + myLate + " : " + myLang);

				Intent intObj = new Intent(NearByDashboard.this, MainActivityMap.class);
				intObj.putExtra("myLate", String.valueOf(myLate));
				intObj.putExtra("myLang", String.valueOf(myLang));
				intObj.putExtra("iChoice", "hospital");
				startActivity(intObj);
				progress.dismiss();

			}
		});

		police_btn = (RelativeLayout) findViewById(R.id.police_btn);
		police_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				System.out.println("My Location:" + myLate + " : " + myLang);

				Intent intObj = new Intent(NearByDashboard.this, MainActivityMap.class);
				intObj.putExtra("myLate", String.valueOf(myLate));
				intObj.putExtra("myLang", String.valueOf(myLang));
				intObj.putExtra("iChoice", "police");
				startActivity(intObj);
				progress.dismiss();

			}
		});

		/*
		 * airport_btn = (RelativeLayout)findViewById(R.id.airport_btn);
		 * airport_btn.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub
		 * 
		 * System.out.println("My Location:"+myLate+" : "+myLang);
		 * 
		 * Intent intObj = new
		 * Intent(NearByDashboard.this,MainActivityMap.class);
		 * intObj.putExtra("myLate", String.valueOf(myLate));
		 * intObj.putExtra("myLang", String.valueOf(myLang));
		 * intObj.putExtra("iChoice", "airport"); startActivity(intObj);
		 * progress.dismiss();
		 * 
		 * } });
		 */

		restaurant_btn = (RelativeLayout) findViewById(R.id.resturant_btn);
		restaurant_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				System.out.println("My Location:" + myLate + " : " + myLang);

				Intent intObj = new Intent(NearByDashboard.this, MainActivityMap.class);
				intObj.putExtra("myLate", String.valueOf(myLate));
				intObj.putExtra("myLang", String.valueOf(myLang));
				intObj.putExtra("iChoice", "food");
				startActivity(intObj);
				progress.dismiss();

			}
		});

		/*
		 * bus_btn= (RelativeLayout)findViewById(R.id.bus_btn);
		 * bus_btn.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub
		 * 
		 * System.out.println("My Location:"+myLate+" : "+myLang);
		 * 
		 * Intent intObj = new
		 * Intent(NearByDashboard.this,MainActivityMap.class);
		 * intObj.putExtra("myLate", String.valueOf(myLate));
		 * intObj.putExtra("myLang", String.valueOf(myLang));
		 * intObj.putExtra("iChoice", "bus_station"); startActivity(intObj);
		 * progress.dismiss();
		 * 
		 * } });
		 * 
		 * taxi_btn= (RelativeLayout)findViewById(R.id.taxi_btn);
		 * taxi_btn.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub
		 * 
		 * System.out.println("My Location:"+myLate+" : "+myLang);
		 * 
		 * Intent intObj = new
		 * Intent(NearByDashboard.this,MainActivityMap.class);
		 * intObj.putExtra("myLate", String.valueOf(myLate));
		 * intObj.putExtra("myLang", String.valueOf(myLang));
		 * intObj.putExtra("iChoice", "taxi_stand"); startActivity(intObj);
		 * progress.dismiss();
		 * 
		 * } });
		 */

		/*
		 * hotel_btn= (RelativeLayout)findViewById(R.id.hotel_btn2);
		 * hotel_btn.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub
		 * 
		 * System.out.println("My Location:"+myLate+" : "+myLang);
		 * 
		 * Intent intObj = new
		 * Intent(NearByDashboard.this,MainActivityMap.class);
		 * intObj.putExtra("myLate", String.valueOf(myLate));
		 * intObj.putExtra("myLang", String.valueOf(myLang));
		 * intObj.putExtra("iChoice", "lodging"); startActivity(intObj);
		 * progress.dismiss();
		 * 
		 * } });
		 */
		/*
		 * cinema_btn= (RelativeLayout)findViewById(R.id.cinema_btn);
		 * cinema_btn.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub
		 * 
		 * System.out.println("My Location:"+myLate+" : "+myLang);
		 * 
		 * Intent intObj = new
		 * Intent(NearByDashboard.this,MainActivityMap.class);
		 * intObj.putExtra("myLate", String.valueOf(myLate));
		 * intObj.putExtra("myLang", String.valueOf(myLang));
		 * intObj.putExtra("iChoice", "movie_theater"); startActivity(intObj);
		 * progress.dismiss();
		 * 
		 * } });
		 */

		shopping_btn = (RelativeLayout) findViewById(R.id.shopping_btn);
		shopping_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				System.out.println("My Location:" + myLate + " : " + myLang);

				Intent intObj = new Intent(NearByDashboard.this, MainActivityMap.class);
				intObj.putExtra("myLate", String.valueOf(myLate));
				intObj.putExtra("myLang", String.valueOf(myLang));
				intObj.putExtra("iChoice", "shopping_mall");
				startActivity(intObj);
				progress.dismiss();

			}
		});

		/*
		 * park_btn= (RelativeLayout)findViewById(R.id.park_btn2);
		 * park_btn.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub
		 * 
		 * System.out.println("My Location:"+myLate+" : "+myLang);
		 * 
		 * Intent intObj = new
		 * Intent(NearByDashboard.this,MainActivityMap.class);
		 * intObj.putExtra("myLate", String.valueOf(myLate));
		 * intObj.putExtra("myLang", String.valueOf(myLang));
		 * intObj.putExtra("iChoice", "park"); startActivity(intObj);
		 * progress.dismiss();
		 * 
		 * } });
		 */

		/*
		 * gas_btn= (RelativeLayout)findViewById(R.id.gas_btn);
		 * gas_btn.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub
		 * 
		 * System.out.println("My Location:"+myLate+" : "+myLang);
		 * 
		 * Intent intObj = new
		 * Intent(NearByDashboard.this,MainActivityMap.class);
		 * intObj.putExtra("myLate", String.valueOf(myLate));
		 * intObj.putExtra("myLang", String.valueOf(myLang));
		 * intObj.putExtra("iChoice", "gas_station"); startActivity(intObj);
		 * progress.dismiss();
		 * 
		 * } });
		 */

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
			String sharerUrl = "https://twitter.com/intent/tweet?text=" + urlToShare;
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
		startActivity(new Intent(NearByDashboard.this, DashboardActivity.class));
		finish();
	}
}

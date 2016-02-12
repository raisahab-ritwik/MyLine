package com.seva60plus.hum.specialoffers;

import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.seva60plus.hum.R;
import com.seva60plus.hum.activity.DashboardActivity;
import com.seva60plus.hum.activity.HumDetailsView;
import com.seva60plus.hum.activity.MenuLay;
import com.seva60plus.hum.custom.NoInternetPage;
import com.seva60plus.hum.util.ConnectionDetector;
import com.seva60plus.hum.util.Util;

public class OfferMedicalActivity extends FragmentActivity {
	ViewPager Tab;
	OfferMedicalTabPagerAdapter TabAdapter;
	//ActionBar actionBar;

	
	RelativeLayout backBtn;
	ImageView menuIcon;
	ImageView backBtnSub;
	
	//---by Dibyendu
		 // flag for Internet connection status
	    Boolean isInternetPresent = false;
	     
	    // Connection detector class
	    ConnectionDetector cd;
	    LocationManager lm;
	    
	    //------End
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.offer_medical_activity);


		cd = new ConnectionDetector(getApplicationContext());// by Dibyendu
		lm = (LocationManager) getSystemService(LOCATION_SERVICE);// by Dibyendu
		
		backBtnSub = (ImageView)findViewById(R.id.iv_back);
		backBtnSub.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(0, 0);
			}
		});
		
		findViewById(R.id.llMenu).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intObj = new Intent(OfferMedicalActivity.this,MenuLay.class);
				startActivity(intObj);
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
				
				//finish();
			}
		});
		
		backBtn=(RelativeLayout)findViewById(R.id.back_settings);
        
		backBtn.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View view) {
	           
	                if (Util.isInternetAvailable(OfferMedicalActivity.this)) {
	    	        	Intent i = new Intent(OfferMedicalActivity.this,HumDetailsView.class);
	    				startActivity(i);
	    				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_leave_right);
	    				
	           }
	                else {
	                	Intent i = new Intent(OfferMedicalActivity.this,NoInternetPage.class);
	                	startActivity(i);
	    				overridePendingTransition(0, 0);
	                	
	                }
	        	
	        }
	    });

		final Button nextBtn = (Button)findViewById(R.id.button1);
		nextBtn.setVisibility(View.VISIBLE);
		final Button preBtn = (Button)findViewById(R.id.button2);
		preBtn.setVisibility(View.VISIBLE);
		
		TabAdapter = new OfferMedicalTabPagerAdapter(getSupportFragmentManager());

		Tab = (ViewPager)findViewById(R.id.pager);
		Tab.setAdapter(TabAdapter);
		 Tab.setOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                    	
                    	System.out.println("MAIN: "+Tab.getCurrentItem()); 
                    	if(Tab.getCurrentItem() == 0){
        					nextBtn.setBackgroundColor(Color.parseColor("#404041"));
        					preBtn.setBackgroundColor(Color.parseColor("#A0A0A0"));
        				}
        				else if(Tab.getCurrentItem() == 1){
        					nextBtn.setBackgroundColor(Color.parseColor("#404041"));
        					preBtn.setBackgroundColor(Color.parseColor("#404041"));
        				}
        				else if(Tab.getCurrentItem() == 2){
        					nextBtn.setBackgroundColor(Color.parseColor("#A0A0A0"));
        					preBtn.setBackgroundColor(Color.parseColor("#404041"));
        				}
        				else{
        					nextBtn.setBackgroundColor(Color.parseColor("#A0A0A0"));
        					preBtn.setBackgroundColor(Color.parseColor("#A0A0A0"));
        				}
                    	}
                });
		
		


		nextBtn.setBackgroundColor(Color.parseColor("#404041"));
		preBtn.setBackgroundColor(Color.parseColor("#A0A0A0"));
		nextBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Tab.setCurrentItem(getItem(+1), true);
				System.out.println("NEXT : "+Tab.getCurrentItem());

				if(Tab.getCurrentItem() == 0){
					nextBtn.setBackgroundColor(Color.parseColor("#404041"));
					preBtn.setBackgroundColor(Color.parseColor("#A0A0A0"));
				}
				else if(Tab.getCurrentItem() == 1){
					nextBtn.setBackgroundColor(Color.parseColor("#404041"));
					preBtn.setBackgroundColor(Color.parseColor("#404041"));
				}
				else if(Tab.getCurrentItem() == 2){
					nextBtn.setBackgroundColor(Color.parseColor("#A0A0A0"));
					preBtn.setBackgroundColor(Color.parseColor("#404041"));
				}
				else{
					nextBtn.setBackgroundColor(Color.parseColor("#A0A0A0"));
					preBtn.setBackgroundColor(Color.parseColor("#A0A0A0"));
				}
			}
		});



		preBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Tab.setCurrentItem(getItem(-1), true);
				System.out.println("PRE : "+Tab.getCurrentItem());

				if(Tab.getCurrentItem() == 2){
					nextBtn.setBackgroundColor(Color.parseColor("#A0A0A0"));
					preBtn.setBackgroundColor(Color.parseColor("#404041"));
				}
				else if(Tab.getCurrentItem() == 1){
					nextBtn.setBackgroundColor(Color.parseColor("#404041"));
					preBtn.setBackgroundColor(Color.parseColor("#404041"));
				}
				else if(Tab.getCurrentItem() == 0){
					nextBtn.setBackgroundColor(Color.parseColor("#404041"));
					preBtn.setBackgroundColor(Color.parseColor("#A0A0A0"));
				}
				else{
					nextBtn.setBackgroundColor(Color.parseColor("#A0A0A0"));
					preBtn.setBackgroundColor(Color.parseColor("#A0A0A0"));
				}
			}
		});
	}


	private int getItem(int i) {
		return Tab.getCurrentItem() + i;
	}
	public void onHomeClick(View v) {
		startActivity(new Intent(OfferMedicalActivity.this, DashboardActivity.class));
		finish();
	}
}

package com.seva60plus.hum.util;

import com.seva60plus.hum.R;
import com.seva60plus.hum.R.anim;
import com.seva60plus.hum.R.id;
import com.seva60plus.hum.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class CheckUpdates extends Activity {
	
	//---by Dibyendu
		 // flag for Internet connection status
	    Boolean isInternetPresent = false;
	     
	    // Connection detector class
	    ConnectionDetector cd;
	
	    public static final String MY_PREFS_NAME = "MyPrefsFile";
	   @Override
	   protected void onCreate(Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);
	      setContentView(R.layout.check_updates);
	      overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
	      
	      cd = new ConnectionDetector(getApplicationContext());// by Dibyendu
	      
	      Button checkBtn = (Button)findViewById(R.id.check_internet);
	      checkBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
				editor.putString("checkUpdate", "1");
				editor.commit();
				*/
				Intent i = new Intent(Intent.ACTION_VIEW, 
						Uri.parse("https://play.google.com/store/apps/details?id=com.seva60plus.hum&hl=en"));
				startActivity(i);
				
				finish();
	             
				
			}
		});
	    
	      Button checkBtnCancel = (Button)findViewById(R.id.check_internet_cancel);  
	      checkBtnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
				editor.putString("checkUpdate", "1");
				editor.commit();
				
            	finish();
				overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
				
			}
		});
	      
	   }
}

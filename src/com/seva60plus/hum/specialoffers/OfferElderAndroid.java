package com.seva60plus.hum.specialoffers;

import com.seva60plus.hum.R;
import com.seva60plus.hum.R.id;
import com.seva60plus.hum.R.layout;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class OfferElderAndroid extends Fragment {

	View android;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		android = inflater.inflate(R.layout.offer_elder_android_frag, container, false);
		((TextView)android.findViewById(R.id.textView)).setText("Hindustan Wellness Pvt Ltd");

		TextView call_txt = (TextView)android.findViewById(R.id.phone_text);
		Button call_img = (Button)android.findViewById(R.id.call_icon2);

		call_img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
				    Intent my_callIntent = new Intent(Intent.ACTION_CALL);
				    my_callIntent.setData(Uri.parse("tel:"+"+919810981073"));
				    //here the word 'tel' is important for making a call...
				    startActivity(my_callIntent);
				} catch (ActivityNotFoundException e) {
				    Toast.makeText(getActivity(), "Error in your phone call"+e.getMessage(), Toast.LENGTH_LONG).show();
				}
			}
		});
		call_txt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
				    Intent my_callIntent = new Intent(Intent.ACTION_CALL);
				    my_callIntent.setData(Uri.parse("tel:"+"+919810981073"));
				    //here the word 'tel' is important for making a call...
				    startActivity(my_callIntent);
				} catch (ActivityNotFoundException e) {
				    Toast.makeText(getActivity(), "Error in your phone call"+e.getMessage(), Toast.LENGTH_LONG).show();
				}
			}
		});

		TextView web_txt = (TextView)android.findViewById(R.id.web_text);
		Button web_img = (Button)android.findViewById(R.id.web_text_icon2);

		web_txt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					Intent i = new Intent(Intent.ACTION_VIEW, 
							Uri.parse("http://www.hindustanwellness.com"));
					startActivity(i);
				} catch (ActivityNotFoundException e) {
				    Toast.makeText(getActivity(), "Error in your phone call"+e.getMessage(), Toast.LENGTH_LONG).show();
				}
			}
		});
		web_img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					Intent i = new Intent(Intent.ACTION_VIEW, 
							Uri.parse("http://www.hindustanwellness.com"));
					startActivity(i);
				} catch (ActivityNotFoundException e) {
				    Toast.makeText(getActivity(), "Error in your phone call"+e.getMessage(), Toast.LENGTH_LONG).show();
				}
			}
		});
		

		return android;
	}


}

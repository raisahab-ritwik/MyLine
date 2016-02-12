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
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class OfferMedicalIos extends Fragment {
	 @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
	 
	        View ios = inflater.inflate(R.layout.offer_medical_ios_frag, container, false);
	        ((TextView)ios.findViewById(R.id.textView)).setText("iOS");
	        
	       
	        
	        return ios;
}}

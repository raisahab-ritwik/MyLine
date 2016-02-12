package com.seva60plus.hum.specialoffers;


import com.seva60plus.hum.R;
import com.seva60plus.hum.R.id;
import com.seva60plus.hum.R.layout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class OfferMedicalWindows extends Fragment {
	 @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
	 
		    View windows = inflater.inflate(R.layout.offer_medical_windows_frag, container, false);
	       ((TextView)windows.findViewById(R.id.textView)).setText("Windows");
	        return windows;
}}

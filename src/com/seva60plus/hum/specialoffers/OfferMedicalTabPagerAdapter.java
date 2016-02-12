package com.seva60plus.hum.specialoffers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class OfferMedicalTabPagerAdapter extends FragmentStatePagerAdapter {
    public OfferMedicalTabPagerAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int i) {
		switch (i) {
        case 0:
            //Fragement for Android Tab
            return new OfferMedicalAndroid();
     /*  case 1:
           //Fragment for Ios Tab
            return new OfferMedicalIos();
        case 2:
            //Fragment for Windows Tab
            return new OfferMedicalWindows();*/
        }
		return null;
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 1; //No of Tabs
	}


    }
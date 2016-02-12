package com.seva60plus.hum.specialoffers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class OfferElderTabPagerAdapter extends FragmentStatePagerAdapter {
    public OfferElderTabPagerAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int i) {
		switch (i) {
        case 0:
            //Fragement for Android Tab
            return new OfferElderAndroid();
        case 1:
           //Fragment for Ios Tab
            return new OfferElderIos();
        case 2:
            //Fragment for Windows Tab
            return new OfferElderWindows();
        case 3:
            //Fragment for Blackberry Tab
            return new OfferElderBlackberry();
        }
		return null;
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 4; //No of Tabs
	}


    }
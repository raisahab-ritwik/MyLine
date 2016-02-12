package com.seva60plus.hum.utilities.weather;


import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Fragment;
import android.app.FragmentTransaction;

public class WeatherMyTabListener implements ActionBar.TabListener {

	private Fragment mFragment;
	private WeatherFirstActivity mActivity;
	private final String mTag;
	private final Class mClass;
	WeatherData data;
	Tab lastTab;
	FragmentTransaction lastFt;

	
	boolean isSelect =false;
	String greg = "MyTabListener";


	public WeatherMyTabListener(WeatherFirstActivity activity, WeatherData data, String tag, Class<?> clz) {
		WeatherMyLog.d("gerg", "constructor");
		mActivity = activity;
		mTag = tag;
		mClass = clz;
		this.data=data;

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {

		lastTab=tab;
		lastFt=ft;
		if (mFragment == null) {	
		WeatherMyLog.d("gerg", "onTabSelected "+mTag+ "\ntab:"+ tab+"\nft:"+ft);

			mFragment = Fragment.instantiate(mActivity, mClass.getName());
			ft.add(android.R.id.content, mFragment, mTag);

		} else {
			ft.attach(mFragment);
		}
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		if (mFragment != null) {
		WeatherMyLog.d("gerg", "onTabUnselected "+mTag+ "\ntab:"+ tab+"\nft:"+ft);
			ft.detach(mFragment);
		}
	}


	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		WeatherMyLog.d("gerg", "onTabReselected "+mTag+ "\ntab:"+ tab+"\nft:"+ft);
		onTabUnselected(tab,ft);
		onTabSelected(tab, ft);
	}
	
	

}

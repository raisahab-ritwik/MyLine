package com.seva60plus.hum.reminder;




import com.seva60plus.hum.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ImageView;


 
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
 
public class MainActivity extends FragmentActivity {
    private MyAdapter mAdapter;
    private ViewPager mPager;
    
 
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_reminder);
        mAdapter = new MyAdapter(getSupportFragmentManager());
 
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
    }
 
    public static class MyAdapter extends FragmentPagerAdapter {
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }
 
        @Override
        public int getCount() {
            return 1;
        }
 
        @Override
        public Fragment getItem(int position) {
            switch (position) {
            case 0:
                return new DayViewFragment();
          /*  case 1:
                return new TypeViewFragment();*/
              
            default:
                return null;
            }
        }
    }
}
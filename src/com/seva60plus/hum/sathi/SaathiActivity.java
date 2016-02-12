package com.seva60plus.hum.sathi;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.seva60plus.hum.R;
import com.seva60plus.hum.activity.BaseActivity;
import com.seva60plus.hum.activity.DashboardActivity;
import com.seva60plus.hum.activity.HumDetailsView;
import com.seva60plus.hum.activity.MenuLay;
import com.seva60plus.hum.custom.NoInternetPage;
import com.seva60plus.hum.custom.NoInternetPageAddSaathi;
import com.seva60plus.hum.db.DBConstants;
import com.seva60plus.hum.db.SaathiDB;
import com.seva60plus.hum.staticconstants.ConstantVO;
import com.seva60plus.hum.util.SocialNetworking;
import com.seva60plus.hum.util.Util;

public class SaathiActivity extends BaseActivity implements OnClickListener, FetchSaathiListListener, OnItemClickListener {

	private Context mContext;
	private Button btn_whatsapp, btn_fb, btn_twitter;
	private ImageView btn_back;
	private LinearLayout btn_home;
	private RelativeLayout settings;

	private RelativeLayout rlAddSaathi;
	private ListView lvSaathiList;
	private SaathiListAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_saathi2);
		mContext = SaathiActivity.this;
		initView();
	}

	private void initView() {

		btn_whatsapp = (Button) findViewById(R.id.whatsapp_btn);
		btn_fb = (Button) findViewById(R.id.facebook_btn);
		btn_twitter = (Button) findViewById(R.id.twitter_btn);
		btn_back = (ImageView) findViewById(R.id.iv_back);
		settings = (RelativeLayout) findViewById(R.id.back_settings);
		btn_home = (LinearLayout) findViewById(R.id.llMenu);

		lvSaathiList = (ListView) findViewById(R.id.lvSaathiList);

		rlAddSaathi = (RelativeLayout) findViewById(R.id.rlAddSaathi);

		btn_whatsapp.setOnClickListener(this);
		btn_fb.setOnClickListener(this);
		btn_twitter.setOnClickListener(this);
		btn_home.setOnClickListener(this);
		btn_back.setOnClickListener(this);

		settings.setOnClickListener(this);

		lvSaathiList.setOnItemClickListener(this);

		rlAddSaathi.setOnClickListener(this);

		if (Util.isInternetAvailable(mContext)) {
			try {
				String humPhone = Util.getSharedPreference(mContext).getString("humPhone", "");
				FetchSaathiList mTask = new FetchSaathiList(mContext, humPhone);
				mTask.mListener = this;
				mTask.execute(ConstantVO.FETCH_SAATHI_LIST);
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {
			//TODO Load from Database
			ArrayList<Saathi> saathiList = new ArrayList<Saathi>();
			saathiList = new SaathiDB().getSaathiList(mContext);

			if (saathiList.size() > 0) {

				mAdapter = new SaathiListAdapter(mContext, saathiList);
				lvSaathiList.setAdapter(mAdapter);

			} else {
				Intent intent_noInternet = new Intent(mContext, NoInternetPage.class);
				startActivity(intent_noInternet);
				overridePendingTransition(0, 0);
				finish();
			}

		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.whatsapp_btn:
			SocialNetworking.shareAppLinkViaWhatsApp(mContext);
			break;

		case R.id.facebook_btn:
			SocialNetworking.shareAppLinkViaFacebook(mContext);
			break;

		case R.id.twitter_btn:
			SocialNetworking.shareAppLinkViaTwitter(mContext);
			break;

		case R.id.iv_back:
			finish();
			break;

		case R.id.llMenu:
			Intent myIntent = new Intent(mContext, MenuLay.class);
			startActivity(myIntent);
			overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			finish();
			break;

		case R.id.back_settings:
			if (Util.isInternetAvailable(mContext)) {
				Intent i = new Intent(mContext, HumDetailsView.class);
				startActivity(i);
				overridePendingTransition(0, 0);
				finish();
			} else {
				Intent i = new Intent(mContext, NoInternetPage.class);
				startActivity(i);
				overridePendingTransition(0, 0);
			}
			break;

		case R.id.rlAddSaathi:

			if (Util.isInternetAvailable(mContext))

				addSaathi();
			else {
				Intent i = new Intent(mContext, NoInternetPageAddSaathi.class);
				startActivity(i);
				overridePendingTransition(0, 0);
			}
			break;

		default:
			break;
		}

	}

	@Override
	public void getSaathiList(ArrayList<Saathi> saathiList) {

		if (saathiList.size() > 0) {

			mAdapter = new SaathiListAdapter(mContext, saathiList);
			lvSaathiList.setAdapter(mAdapter);
			saveSaathi(saathiList);
		} else {
			Toast.makeText(mContext, "Connection could not be established. Please try again", Toast.LENGTH_LONG).show();
			finish();
		}

	}

	/** Add a new Saathi **/
	private void addSaathi() {

		startActivity(new Intent(mContext, AddSaathiAcivity.class));
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

	}

	private void saveSaathi(ArrayList<Saathi> saathiList) {

		new SaathiDB().deleteSaathiTable(mContext);

		//--- SAVE FORM SAATHI IN DATABASE
		for (int i = 0; i < saathiList.size(); i++) {
			Log.v("Khhdripur", "Country Code: " + saathiList.get(i).getName().trim());
			Log.v("Khhdripur", "Country Code: " + saathiList.get(i).getCountryCode().trim());
			ContentValues mValues = new ContentValues();
			mValues.put(DBConstants.SAATHI_NAME, saathiList.get(i).getName().trim());
			if (saathiList.get(i).getCountryCode() != null && !saathiList.get(i).getCountryCode().isEmpty())
				mValues.put(DBConstants.SAATHI_COUNTRY_CODE, saathiList.get(i).getCountryCode().trim());
			else
				mValues.put(DBConstants.SAATHI_COUNTRY_CODE, "");
			mValues.put(DBConstants.SAATHI_PHONE_NUMBER, saathiList.get(i).getPhoneNumber().trim());
			mValues.put(DBConstants.SAATHI_EMAIL, saathiList.get(i).getEmailId().trim());
			new SaathiDB().saveSaathi(mContext, mValues);

		}

	}

	public void onHomeClick(View v) {
		startActivity(new Intent(mContext, DashboardActivity.class));
		finish();
	}
}

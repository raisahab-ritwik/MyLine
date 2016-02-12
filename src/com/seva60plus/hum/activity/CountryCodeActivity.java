package com.seva60plus.hum.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;

import com.seva60plus.hum.R;
import com.seva60plus.hum.adapter.CountryListArrayAdapter;
import com.seva60plus.hum.model.Country;

public class CountryCodeActivity extends Activity {

	private ListView listView_country;
	public static String RESULT_CONTRYCODE = "countrycode";
	private String[] countrynames, countrycodes;
	private TypedArray imgs;
	private List<Country> countryList;
	AutoCompleteTextView act_country_list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_countrycode_layout);
		act_country_list = (AutoCompleteTextView) findViewById(R.id.act_country_list);
		listView_country = (ListView) findViewById(R.id.listView_countryList);
		populateCountryList();
		CountryListArrayAdapter adapter = new CountryListArrayAdapter(CountryCodeActivity.this, countryList);

		listView_country.setAdapter(adapter);

		listView_country.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Country c = countryList.get(position);
				Intent returnIntent = new Intent();
				returnIntent.putExtra(RESULT_CONTRYCODE, c.getCode());
				setResult(RESULT_OK, returnIntent);
				imgs.recycle();
				// recycle images
				finish();
			}
		});
		// Populate AutoComplete TextView
		ArrayAdapter<String> mAutoCompleteAdapter = new ArrayAdapter<String>(CountryCodeActivity.this, android.R.layout.simple_list_item_1, getResources()
				.getStringArray(R.array.autocomplete_country_names));
		act_country_list.setAdapter(mAutoCompleteAdapter);

		act_country_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Intent returnIntent = new Intent();
				returnIntent.putExtra(RESULT_CONTRYCODE, fetchPhoneCodeFromName(act_country_list.getText().toString().trim(), countryList));
				setResult(RESULT_OK, returnIntent);
				imgs.recycle();
				// recycle images
				finish();
			}
		});

	}

	private void populateCountryList() {
		countryList = new ArrayList<Country>();
		countrynames = getResources().getStringArray(R.array.country_names);
		countrycodes = getResources().getStringArray(R.array.country_codes);
		imgs = getResources().obtainTypedArray(R.array.country_flags);
		for (int i = 0; i < countrycodes.length; i++) {
			countryList.add(new Country(countrynames[i], countrycodes[i], imgs.getDrawable(i)));
		}

	}

	private String fetchPhoneCodeFromName(String countryName, List<Country> countryList) {
		System.out.println("Country Name: " + countryName);
		HashMap<String, String> countryCodeMap = new HashMap<String, String>();
		for (int i = 0; i < countryList.size(); i++) {
			String[] countryArray = countryList.get(i).getName().split("\\,");
			String countryNamefromMap = countryArray[1];

			countryCodeMap.put(countryNamefromMap, countryList.get(i).getCode());

		}
		Log.v("Hashmap", "" + countryCodeMap.toString());
		System.out.println("" + countryCodeMap.get(countryName));
		return countryCodeMap.get(countryName).toString();
	}

}

package com.seva60plus.hum.unused;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.seva60plus.hum.R;
import com.seva60plus.hum.nearby.MainActivityMap;

public class ChooseType extends Activity {
	ListView listView ;
	String area;
	
	public static final String MY_PREFS_NAME = "MyPrefsFile";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_type);
		
		Intent intObj = getIntent();
		area = intObj.getStringExtra("iarea");
		
		
		listView = (ListView) findViewById(R.id.list);
		String[] values = new String[] { "atm", "hospital", "police",
		  "bank", "food", "restaurant", "park" };

		// Define a new Adapter
		// First parameter - Context
		// Second parameter - Layout for the row
		// Third parameter - ID of the TextView to which the data is written
		// Forth - the Array of data

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		  android.R.layout.simple_list_item_1, android.R.id.text1, values);


		// Assign adapter to ListView
		listView.setAdapter(adapter); 
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			  @Override
			  public void onItemClick(AdapterView<?> parent, View view,
			    int position, long id) {
				
		       // ListView Clicked item index
			   int itemPosition     = position;
			   
			   // ListView Clicked item value
			   String  itemValue    = (String) listView.getItemAtPosition(position);
				  
			    // Show Alert 
			    Toast.makeText(getApplicationContext(),
			      itemValue , Toast.LENGTH_LONG)
			      .show();
			    
			    Intent intObj = new Intent(ChooseType.this,MainActivityMap.class);
			    intObj.putExtra("iChoice", itemValue);
			    intObj.putExtra("iarea", area);
			    startActivity(intObj);
			 
			  }

			
			}); 
	}

}
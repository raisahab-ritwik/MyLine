package com.seva60plus.hum.reminder;




import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import com.seva60plus.hum.R;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ExpandableListView;
import android.widget.TextView;
 
//
public class ExpandableListAdapter extends BaseExpandableListAdapter {
 
    private Context _context;
    private List<String> _listDataHeader; // header titles
    TextView txtremId;
    CheckBox chkDone;
    HashMap<String, String> newChild = new HashMap<String, String>();
   // ExpandableListView bkupexp;
    
    private HashMap<String, List<HashMap<String, String>>> _listDataChild;
    DBController db;
 
    public ExpandableListAdapter(Context context, List<String> listDataHeader,
    		HashMap<String, List<HashMap<String, String>>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        db = new DBController(context);
    }
 
     
    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }
    /*
    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        super.registerDataSetObserver(observer);    
    }
    */
     
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
  
    
    @Override
    public View getChildView(final int groupPosition, final int childPosition,
            boolean isLastChild, View convertView, ViewGroup parent) {
    	
        final HashMap<String, String> childText = (HashMap<String, String>) getChild(groupPosition, childPosition);
 
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item_reminder, null);
        }
 
        txtremId = (TextView) convertView
                .findViewById(R.id.remId);

         TextView txtListChild = ( TextView) convertView
                .findViewById(R.id.lblListItem);
         

         
         txtListChild.setText(childText.get("shortDesc"));
         txtremId.setText(childText.get("remId"));
         
         chkDone = (CheckBox) convertView.findViewById(R.id.chkDone);
         chkDone.setChecked(false);
         String HName = _listDataHeader.get(groupPosition);
      
         if (HName.equals("Daily")||HName.equals("Weekly")||HName.equals("Monthly")||HName.equals("Yearly")||HName.equals("No Repeat")||HName.equals("View All Reminders")||HName.equals("Completed Items"))
         {
        	 Log.i("expadap head name",_listDataHeader.get(groupPosition));
          chkDone.setVisibility(View.GONE);
         }
         else
         {
        	 chkDone.setVisibility(View.VISIBLE); 
         }
        
        
         
        chkDone.setOnCheckedChangeListener(new OnCheckedChangeListener() { 

            /* (non-Javadoc)
             * @see android.widget.CompoundButton.OnCheckedChangeListener#onCheckedChanged(android.widget.CompoundButton, boolean)
             */
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) { 
            	
            	if ( isChecked)
            	{
            		
            		List<HashMap<String, String>> child =
          		 	      _listDataChild.get(_listDataHeader.get(groupPosition));
            		
		           	 HashMap<String, String> queryValues =  new  HashMap<String, String>();
		           	String s_remDate;
		      	  SimpleDateFormat fmtDateAndTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		      		
		      		Calendar cal = Calendar.getInstance();
		      		
		      		
		      		s_remDate = fmtDateAndTime.format(cal.getTime());
		      		
		   		  String s_remId = child.get(childPosition).get("remId");
		   				  
		 		    queryValues.put("remDone", "YES");
		 		    queryValues.put("doneDate", s_remDate);
		 		   queryValues.put("remId", s_remId);
		 		   
		 		   Log.i("exp adap",s_remDate+" "+ s_remId);
		 		    db.updateDoneFlds(queryValues);
		 		    
		 		    Log.i("exp adap grp pos",Integer.toString(groupPosition));
		 		    
		 		   Log.i("exp adap grp pos",Integer.toString(childPosition));
		 		    
		 		 
		 		  newChild = new HashMap<String, String>();
		 		
		 		newChild.put("remId",s_remId);
		 		newChild.put("shortDesc",child.get(childPosition).get("shortDesc"));
		 		 
		 	Log.i("new child rem id",newChild.get("remId"));
		 		 Log.i("new child Short Desc",newChild.get("shortDesc"));
		 		  
		 		Log.i("child rem id",child.get(childPosition).get("remId"));
		 		 Log.i("child Short Desc",child.get(childPosition).get("shortDesc"));
		 		  
		 		   child.remove(childPosition);
                  
		 		//  notifyDataSetChanged();
		 		  
		 		  List<HashMap<String, String>> newCompList =
				 	      _listDataChild.get("Completed Items");
		 		  
		          int compsize = _listDataChild.get("Completed Items").size();
		          Log.i("comp size",Integer.toString(compsize));
		          
				  newCompList.add(compsize,newChild);
				  
				// _listDataChild.put("Completed Items", newCompList);
				 		   
				//  onGroupExpanded(5);

				  //   chkDone.setChecked(false);
				  //notifyDataSetInvalidated()
				  // to make 2 dynamic add values to get reflected in completed list
				  //bkupexp.expandGroup(5);
				//  bkupexp.clickGroup(5);
				//  bkupexp.collapseGroup(5);
				  
				 notifyDataSetChanged();
				  
            	}
            	 
            } 
        });


          
        return convertView;
    }
 
    /*
    public void chkDoneActivity(View v)
    {
    	Log.i("expadap","chkdone");
//    	//Toast.makeText(getActivity().getApplicationContext(), "chked : "  , //Toast.LENGTH_SHORT).show(); 
    }
    */
    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }
 
    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }
 
    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }
 
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
 
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
            View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group_reminder, null);
        }
 
        TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);
 
        return convertView;
    }
 
    @Override
    public boolean hasStableIds() {
        return false;
    }
 
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
package com.seva60plus.hum.sharelocation;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.seva60plus.hum.R;
import com.seva60plus.hum.R.id;
import com.seva60plus.hum.R.layout;
import com.seva60plus.hum.nearby.NearByMapListInfo;
import com.seva60plus.hum.util.ImageLoader;

public class MapListLazyAdapter extends BaseAdapter {
	
	public static final String MY_PREFS_NAME = "MyPrefsFile";
	Context context;
    List<MapListData> data;
    ArrayList<MapListData> objects;
    String prefMyLat,prefMyLng;
    
    
    public ImageLoader imageLoader; 
    public MapListLazyAdapter(Context context, List<MapListData> data) {
        this.context = context;
        this.data = data;
        imageLoader=new ImageLoader(context.getApplicationContext());
    }
     
    /*private view holder class*/
    private class ViewHolder {
        ImageView imageViewS;
        TextView userNameS, address;
        RelativeLayout distKM,moreBtn;
        
        TextView distText;
      
    }
 
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
         
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, context.MODE_PRIVATE); 
        prefMyLat  = prefs.getString("prefMyLate", "");
        prefMyLng  = prefs.getString("prefMyLang", "");
        
        
        LayoutInflater mInflater = (LayoutInflater) 
            context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.map_list_row, null);
            holder = new ViewHolder();
          
            holder.userNameS = (TextView) convertView.findViewById(R.id.locNameS);
            holder.address = (TextView) convertView.findViewById(R.id.locAdd);
            holder.imageViewS = (ImageView) convertView.findViewById(R.id.locImageS);
           // holder.imageViewS.setScaleType(ScaleType.CENTER_CROP);
            holder.distKM = (RelativeLayout) convertView.findViewById(R.id.dist_btn);
            
            holder.moreBtn = (RelativeLayout) convertView.findViewById(R.id.more_btn);
            holder.distText = (TextView) convertView.findViewById(R.id.dist_txt);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
         
        final MapListData datas = (MapListData) getItem(position);
        
        holder.userNameS.setText(datas.getFname());
        holder.address.setText(datas.getFuID());
        holder.distText.setText("Distance : "+datas.getDistanceKM()+" k.m");
        System.out.println("DATA---IMG URL "+datas.getFimageUrl());
       // imageLoader.DisplayImage(datas.getFimageUrl(), holder.imageViewS);
        holder.imageViewS.setBackgroundResource(datas.getImage());
        
        System.out.println("\n\n**** Adapter Maplist -"+datas.getFname());
       
        holder.moreBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				System.out.println("*******Info Intent"+prefMyLat+":"+prefMyLng+":da "+datas.getDestLate()+":"+datas.getDestLang());
				
				Intent intObj = new Intent(context,NearByMapListInfo.class);
				intObj.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intObj.putExtra("iMyLate",prefMyLat );
				intObj.putExtra("iMyLang", prefMyLng);
				intObj.putExtra("iDestLate",datas.getDestLate() );
				intObj.putExtra("iDestLang", datas.getDestLang());
				intObj.putExtra("iPlaceID", datas.getPlaceID());
				intObj.putExtra("iChoice", datas.getPlaceType());
				context.startActivity(intObj);
			}
		});
        
        return convertView;
    }
 
    @Override
    public int getCount() {     
        return data.size()-1; //----due to last item duplicate ---change 21.07.15
    }
 
    @Override
    public Object getItem(int position) {
        return data.get(position);
    }
 
    @Override
    public long getItemId(int position) {
        return data.indexOf(getItem(position));
    }

    
   
   
}
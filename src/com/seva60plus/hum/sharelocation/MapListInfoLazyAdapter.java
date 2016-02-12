package com.seva60plus.hum.sharelocation;

import java.util.ArrayList;
import java.util.List;

import com.seva60plus.hum.R;
import com.seva60plus.hum.R.id;
import com.seva60plus.hum.R.layout;
import com.seva60plus.hum.util.ImageLoader;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MapListInfoLazyAdapter extends BaseAdapter {
	

	Context context;
    List<MapListData> data;
    ArrayList<MapListData> objects;
    
    public ImageLoader imageLoader; 
    public MapListInfoLazyAdapter(Context context, List<MapListData> data) {
        this.context = context;
        this.data = data;
        imageLoader=new ImageLoader(context.getApplicationContext());
    }
     
    /*private view holder class*/
    private class ViewHolder {
        ImageView imageViewS;
        TextView userNameS, address;
      
    }
 
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
         
        LayoutInflater mInflater = (LayoutInflater) 
            context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.map_list_row, null);
            holder = new ViewHolder();
          
            holder.userNameS = (TextView) convertView.findViewById(R.id.locNameS);
            holder.address = (TextView) convertView.findViewById(R.id.locAdd);
            holder.imageViewS = (ImageView) convertView.findViewById(R.id.locImageS);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
         
        final MapListData datas = (MapListData) getItem(position);
        
        holder.userNameS.setText(datas.getFname());
        holder.address.setText(datas.getFuID());
        System.out.println("DATA---IMG URL "+datas.getFimageUrl());
       // imageLoader.DisplayImage(datas.getFimageUrl(), holder.imageViewS);
        holder.imageViewS.setBackgroundResource(datas.getImage());
       
        return convertView;
    }
 
    @Override
    public int getCount() {     
        return data.size();
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
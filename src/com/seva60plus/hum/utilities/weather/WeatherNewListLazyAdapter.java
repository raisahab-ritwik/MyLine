package com.seva60plus.hum.utilities.weather;

import java.util.ArrayList;
import java.util.List;

import com.seva60plus.hum.R;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

public class WeatherNewListLazyAdapter extends BaseAdapter {
	
	public static final String MY_PREFS_NAME = "MyPrefsFile";
	Context context;
    List<WeatherNewListData> data;
    ArrayList<WeatherNewListData> objects;
    public WeatherNewListLazyAdapter(Context context, List<WeatherNewListData> data) {
        this.context = context;
        this.data = data;
        
      }
     
    /*private view holder class*/
    private class ViewHolder {
        
        TextView dayName, temp, temp_min, temp_max;
        TextView temp_mor, temp_day, temp_eve, temp_night;
        
        TextView temp_cloud, temp_speed, temp_rain, temp_deg, temp_pressure, temp_humidity;
        TextView wName, wDescription;
        ImageView wImage;
      
    }
 
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
       
        LayoutInflater mInflater = (LayoutInflater) 
            context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.weather_new_list_item_day, null);
            holder = new ViewHolder();
          
            holder.dayName = (TextView) convertView.findViewById(R.id.tvDate);
            holder.temp = (TextView) convertView.findViewById(R.id.tvDayTemperature);
            /*
            holder.temp_min = (TextView) convertView.findViewById(R.id.temp_min);
            holder.temp_max = (TextView) convertView.findViewById(R.id.temp_max);
            
            holder.temp_mor = (TextView) convertView.findViewById(R.id.temp_morn);
            holder.temp_day = (TextView) convertView.findViewById(R.id.temp_day);
            holder.temp_eve = (TextView) convertView.findViewById(R.id.temp_even);
            holder.temp_night = (TextView) convertView.findViewById(R.id.temp_night);
            
            holder.temp_humidity = (TextView) convertView.findViewById(R.id.temp_humidity);
            holder.temp_pressure = (TextView) convertView.findViewById(R.id.temp_pressure);
            holder.temp_cloud = (TextView) convertView.findViewById(R.id.temp_cloud);
            holder.temp_rain = (TextView) convertView.findViewById(R.id.temp_rain);
            holder.temp_speed = (TextView) convertView.findViewById(R.id.temp_speed);
            holder.temp_deg = (TextView) convertView.findViewById(R.id.temp_deg);
           
            holder.wName = (TextView) convertView.findViewById(R.id.temp_wname);*/
            holder.wDescription = (TextView) convertView.findViewById(R.id.tvDescription);
            
            holder.wImage = (ImageView) convertView.findViewById(R.id.imDay);
            
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
         
        final WeatherNewListData datas = (WeatherNewListData) getItem(position);
        
        String wDay = datas.getDate();
        
        String cutWday = wDay.substring(0, wDay.indexOf("y")+1);
        String cutWday2 = wDay.substring(wDay.indexOf("y")+1, wDay.length());
        
        System.out.println("DAY:"+cutWday+"::"+cutWday2);
        
        holder.dayName.setText(cutWday+"\n"+cutWday2);
       // holder.temp_min.setText(datas.getTempMin());
       // holder.temp_max.setText(datas.getTempMax());
        holder.temp.setText(datas.getTempMax()+"/"+datas.getTempMin());
        System.out.println("IM: "+String.valueOf(datas.getWicon()));
       // holder.wName.setText(datas.getWname());
        holder.wDescription.setText(datas.getWdescription());
       holder.wImage.setImageResource(((Context) context).getResources().getIdentifier(
    		   String.valueOf(datas.getWicon()), "drawable",
				((Context) context).getPackageName()));
        
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
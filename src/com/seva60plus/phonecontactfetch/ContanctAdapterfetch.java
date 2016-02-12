package com.seva60plus.phonecontactfetch;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import com.seva60plus.hum.R;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ContanctAdapterfetch extends ArrayAdapter<ContactBeanfetch> {

	private Activity activity;
	private List<ContactBeanfetch> items;
	private int row;
	private ContactBeanfetch objBean;

	public ContanctAdapterfetch(Activity act, int row, List<ContactBeanfetch> items) {
		super(act, row, items);

		this.activity = act;
		this.row = row;
		this.items = items;

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		ViewHolder holder;
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(row, null);

			holder = new ViewHolder();
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		if ((items == null) || ((position + 1) > items.size()))
			return view;

		objBean = items.get(position);

		holder.tvname = (TextView) view.findViewById(R.id.cNameText);
		holder.tvPhoneNo = (TextView) view.findViewById(R.id.cNoText);
		holder.tvImage = (ImageView) view.findViewById(R.id.cUserImageS);
		
		

		if (holder.tvname != null && null != objBean.getName()
				&& objBean.getName().trim().length() > 0) {
			holder.tvname.setText(Html.fromHtml(objBean.getName()));
		}
		if (holder.tvPhoneNo != null && null != objBean.getPhoneNo()
				&& objBean.getPhoneNo().trim().length() > 0) {
			holder.tvPhoneNo.setText(Html.fromHtml(objBean.getPhoneNo()));
		}
		if (holder.tvImage != null && null != objBean.getPhotoUri()
				&& objBean.getPhotoUri().trim().length() > 0) {
			
			try {
			    Bitmap  bitmap = MediaStore.Images.Media
			        .getBitmap(getContext().getContentResolver(),
			          Uri.parse(objBean.getPhotoUri()));
			     
			    holder.tvImage.setImageBitmap(bitmap);

			     } catch (FileNotFoundException e) {
			      // TODO Auto-generated catch block
			      e.printStackTrace();
			     } catch (IOException e) {
			      // TODO Auto-generated catch block
			      e.printStackTrace();
			     }
			
		}
		else{
			holder.tvImage.setImageResource(R.drawable.icon);
		}
		
		final ContactBeanfetch cm = items.get(position);
		
		holder.sms_btn = (RelativeLayout) view.findViewById(R.id.contact_smsBtn);
		holder.sms_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				String phNo = cm.getPhoneNo();
				Toast.makeText(activity, "SMS"+phNo, Toast.LENGTH_LONG).show();
				
				Intent i = new Intent(android.content.Intent.ACTION_VIEW);
 	            i.putExtra("address", phNo);
 	            // here i can send message to emulator 5556,5558,5560
 	            // you can change in real device
 	            i.putExtra("sms_body", "");
 	            i.setType("vnd.android-dir/mms-sms");
 	           activity.startActivity(i);
			}
		});
		
		holder.call_btn = (RelativeLayout) view.findViewById(R.id.contact_callBtn);
		holder.call_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				String phNo = cm.getPhoneNo();
				
				Toast.makeText(activity, phNo, Toast.LENGTH_LONG).show();
				
				try {
				    Intent my_callIntent = new Intent(Intent.ACTION_CALL);
				    my_callIntent.setData(Uri.parse("tel:"+phNo));
				    //here the word 'tel' is important for making a call...
				    activity.startActivity(my_callIntent);
				} catch (ActivityNotFoundException e) {
				    Toast.makeText(activity, "Error in your phone call"+e.getMessage(), Toast.LENGTH_LONG).show();
				}
				
			}
		});


		return view;
	}

	public class ViewHolder {
		
		public TextView tvname, tvPhoneNo;
		public ImageView tvImage;
		public  RelativeLayout call_btn,sms_btn;
	}

}

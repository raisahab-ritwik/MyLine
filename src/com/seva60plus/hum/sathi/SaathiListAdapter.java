package com.seva60plus.hum.sathi;

import java.util.ArrayList;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.seva60plus.hum.R;
import com.seva60plus.hum.Listeners.AlertDialogCallBack;
import com.seva60plus.hum.Listeners.DeleteSaathiListener;
import com.seva60plus.hum.asynctask.DeleteSaathi;
import com.seva60plus.hum.db.SaathiDB;
import com.seva60plus.hum.util.Util;

public class SaathiListAdapter extends BaseAdapter implements DeleteSaathiListener {
	private ArrayList<Saathi> saathiList;
	private Context mContext;
	private LayoutInflater inflater;
	private ViewHolder viewHolder = null;

	public SaathiListAdapter(Context mContext, ArrayList<Saathi> saathiList) {

		this.mContext = mContext;
		this.saathiList = saathiList;
		inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {

		return saathiList.size();
	}

	@Override
	public Object getItem(int position) {

		return saathiList.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View row = convertView;
		if (row == null) {
			row = inflater.inflate(R.layout.adapter_saathi, null);
			viewHolder = new ViewHolder(row);
			row.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) row.getTag();
		}

		viewHolder.tvSaathiName.setText(saathiList.get(position).getName());
		viewHolder.tvSaathiPhone.setText(saathiList.get(position).getPhoneNumber());
		viewHolder.tvSaathiEmail.setText(saathiList.get(position).getEmailId());
		viewHolder.rlCall.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				try {

					Intent my_callIntent = new Intent(Intent.ACTION_CALL);
					my_callIntent.setData(Uri.parse("tel:" + saathiList.get(position).getPhoneNumber()));
					mContext.startActivity(my_callIntent);

				} catch (ActivityNotFoundException e) {

					Toast.makeText(mContext, "Error in your phone call" + e.getMessage(), Toast.LENGTH_LONG).show();
				}
			}
		});

		viewHolder.rlSendSms.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent intent_sendsms_saathi2 = new Intent(android.content.Intent.ACTION_VIEW);
				intent_sendsms_saathi2.putExtra("address", saathiList.get(position).getPhoneNumber());
				intent_sendsms_saathi2.putExtra("sms_body", "");
				intent_sendsms_saathi2.setType("vnd.android-dir/mms-sms");
				mContext.startActivity(intent_sendsms_saathi2);

			}
		});

		viewHolder.ibDeleteSaathi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Util.showCallBackMessageWithOkCancel(mContext, "Are you sure you want to remove this Saathi?", new AlertDialogCallBack() {
					@Override
					public void onSubmit() {
						if (Util.isInternetAvailable(mContext)) {
							if (saathiList.size() > 1) {
								DeleteSaathi deleteSaathi = new DeleteSaathi(mContext, saathiList.get(position), position);
								deleteSaathi.mListener = SaathiListAdapter.this;
								deleteSaathi.execute();
							} else if (saathiList.size() == 1) {
								Toast.makeText(mContext, "You cannot delete this saathi.", Toast.LENGTH_LONG).show();
							}
						} else {
							Toast.makeText(mContext, "Internet is required to delete Saathi.", Toast.LENGTH_LONG).show();
						}
					}

					@Override
					public void onCancel() {
					}
				});

			}
		});

		return row;
	}

	public class ViewHolder {
		public TextView tvSaathiName;
		public TextView tvSaathiPhone;
		public TextView tvSaathiEmail;
		public RelativeLayout rlSendSms;
		public RelativeLayout rlCall;
		public ImageButton ibDeleteSaathi;

		public ViewHolder(View row) {

			tvSaathiName = (TextView) row.findViewById(R.id.tvSaathiName);
			tvSaathiPhone = (TextView) row.findViewById(R.id.tvSaathiPhone);
			tvSaathiEmail = (TextView) row.findViewById(R.id.tvSaathiEmail);
			rlSendSms = (RelativeLayout) row.findViewById(R.id.rlSendSms);
			rlCall = (RelativeLayout) row.findViewById(R.id.rlCall);
			ibDeleteSaathi = (ImageButton) row.findViewById(R.id.ibDeleteSaathi);
		}
	}

	@Override
	public void onDeleteSaathi(String callBack, int position) {

		if (callBack.equalsIgnoreCase("1")) {

			Log.v("SaathiListAdapter", "callback: " + callBack);
			Toast.makeText(mContext, "Saathi Deleted Successfully.", Toast.LENGTH_SHORT).show();
			deleteSaathi(position);

			//TODO Delete Saathi from Database

		} else {
			if (!Util.isInternetAvailable(mContext))
				Toast.makeText(mContext, "No Internet Connectioin! ", Toast.LENGTH_SHORT).show();
			else
				Toast.makeText(mContext, "Network Error! ", Toast.LENGTH_SHORT).show();
		}

	}

	private void deleteSaathi(int position) {

		new SaathiDB().deleteSaathiRow(mContext, saathiList.get(position).getPhoneNumber());

		notifyDataSetChanged(position);

	}

	public void notifyDataSetChanged(int position) {

		this.saathiList.remove(position);

		notifyDataSetChanged();

	}

}

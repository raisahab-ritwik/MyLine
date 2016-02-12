package com.seva60plus.hum.adapter;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.seva60plus.hum.R;
import com.seva60plus.hum.model.Country;

public class CountryListArrayAdapter extends ArrayAdapter<Country> {

	private final List<Country> list;
	private final Activity context;

	static class ViewHolder {
		protected TextView name;
		protected ImageView flag;
		protected View mView;
	}

	public CountryListArrayAdapter(Activity context, List<Country> list) {
		super(context, R.layout.activity_countrycode_row, list);
		this.context = context;
		this.list = list;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;

		if (convertView == null) {
			LayoutInflater inflator = context.getLayoutInflater();
			view = inflator.inflate(R.layout.activity_countrycode_row, null);
			final ViewHolder viewHolder = new ViewHolder();
			viewHolder.name = (TextView) view.findViewById(R.id.name);
			viewHolder.flag = (ImageView) view.findViewById(R.id.flag);
			viewHolder.mView = (View) view.findViewById(R.id.mView_list_divider);
			view.setTag(viewHolder);
		} else {
			view = convertView;
		}

		ViewHolder holder = (ViewHolder) view.getTag();
		holder.name.setText(list.get(position).getName());
		holder.flag.setImageDrawable(list.get(position).getFlag());
		if (holder.name.getText().toString().trim().equalsIgnoreCase("+86,China")) {
			holder.mView.setVisibility(View.VISIBLE);
		} else {
			holder.mView.setVisibility(View.INVISIBLE);
		}
		return view;
	}
}
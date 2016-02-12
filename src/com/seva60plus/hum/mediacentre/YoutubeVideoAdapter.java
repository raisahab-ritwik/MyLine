package com.seva60plus.hum.mediacentre;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.seva60plus.hum.R;
import com.seva60plus.hum.model.Video;

public class YoutubeVideoAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;
	private ViewHolder viewHolder;
	ArrayList<Video> videoList;

	public YoutubeVideoAdapter(Context context, ArrayList<Video> videoList) {
		this.context = context;
		this.videoList = videoList;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return (videoList.size());
	}

	@Override
	public Object getItem(int arg0) {
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		if (row == null) {
			row = inflater.inflate(R.layout.youtube_paylist_items, null);
			viewHolder = new ViewHolder(row);

			row.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) row.getTag();
		}

		Typeface font = Typeface.createFromAsset(context.getAssets(), "openSansRegular.ttf");
		viewHolder.title.setTypeface(font);
		viewHolder.title.setText(videoList.get(position).getTitle());
		viewHolder.videoThumb.setImageDrawable(videoList.get(position).getthumbDrawable());
		// setBitmapImage((videoList.get(position).getUrl()));
		return row;

	}

	class ViewHolder {
		public TextView title;
		public ImageView videoThumb;

		public ViewHolder(View row) {

			title = (TextView) row.findViewById(R.id.video_title);
			videoThumb = (ImageView) row.findViewById(R.id.iv_video_thumb);

		}

	}

}

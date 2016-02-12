package com.seva60plus.hum.youtube;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.format.DateUtils;
import android.util.Log;

/**
 * This is class gets the videos from youtube and parses the result
 *
 */
public class RetrieveVideos {
	
	public static ReturnItem getVideos(String apiUrl, Context context){
		ArrayList<Video> videos = null;
		String pagetoken = null;
		// Making HTTP request
		StringBuffer chaine = new StringBuffer("");
		try {
			URL urlCon = new URL(apiUrl);
			HttpURLConnection connection = (HttpURLConnection) urlCon
					.openConnection();
			connection.setRequestProperty("User-Agent", "");
			connection.setRequestMethod("GET");
			connection.setDoInput(true);
			connection.connect();

			InputStream inputStream = connection.getInputStream();

			BufferedReader rd = new BufferedReader(new InputStreamReader(
					inputStream));
			String line = "";
			while ((line = rd.readLine()) != null) {
				chaine.append(line);
			}

			String jsonString = chaine.toString();
		
			JSONObject json = new JSONObject(jsonString);
			
			Log.v("INFO", "JSON Response: " + json.toString());
			
			if (json.getString("kind").contains("youtube")) {
				videos = new ArrayList<Video>();
			}
			
			try {
				pagetoken = json.getString("nextPageToken");
			} catch (JSONException e){
				Log.v("INFO", "JSONException: " + e);
			}
			
			JSONArray jsonArray = json.getJSONArray("items");
			
			// Create a list to store the videos in
			for (int i = 0; i < jsonArray.length(); i++) {
				try {
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					JSONObject jsonSnippet = jsonArray.getJSONObject(i).getJSONObject("snippet");
					String title = jsonSnippet.getString("title");
					String updated = formatData(jsonSnippet.getString("publishedAt"), context);
					String description = jsonSnippet.getString("description");
					String channel = jsonSnippet.getString("channelTitle");
					String id;
					try {
						id = jsonSnippet.getJSONObject("resourceId").getString("videoId");
					} catch (Exception e) {
						id = jsonObject.getJSONObject("id").getString("videoId");
					}
					// For a sharper thumbnail change sq to hq, this will make the app slower though
					String thumbUrl = jsonSnippet.getJSONObject("thumbnails").getJSONObject("medium").getString("url");
					String image = jsonSnippet.getJSONObject("thumbnails").getJSONObject("high").getString("url");

					// save the video to the list
					videos.add(new Video(title, id, updated, description, thumbUrl, image, channel));
				} catch (JSONException e){
					Log.v("INFO", "JSONException: " + e);
				}
			}						

		} catch (JSONException e) {
			Log.v("INFO", "JSONException: " + e);
		} catch (IOException e) {
			e.printStackTrace();
		}		
		return (new ReturnItem(videos, pagetoken));
	}
	
	@SuppressLint("SimpleDateFormat")
	private static String formatData(String data, Context context){
		SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
		Date date;
		String strData = "";
		try {
			date = parser.parse(data);
			strData = DateUtils.getRelativeDateTimeString(context,date.getTime(), DateUtils.SECOND_IN_MILLIS, DateUtils.WEEK_IN_MILLIS, DateUtils.FORMAT_ABBREV_ALL).toString();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return strData;
	}
	
}
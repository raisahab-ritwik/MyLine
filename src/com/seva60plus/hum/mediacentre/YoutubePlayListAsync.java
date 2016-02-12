package com.seva60plus.hum.mediacentre;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;

import com.seva60plus.hum.model.Video;
import com.seva60plus.hum.util.StreamUtils;

public class YoutubePlayListAsync extends AsyncTask<Void, Void, ArrayList<Video>> {

	private ProgressDialog progress;
	public YoutubePlayListListener mListener;
	private String playlistId;

	public YoutubePlayListAsync(Context context, String playlistId) {

		progress = new ProgressDialog(context);
		progress.setCancelable(true);
		progress.setMessage("Please wait..");
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		this.playlistId = playlistId;

	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (!progress.isShowing()) {

			progress.show();
		}
	}

	@Override
	protected ArrayList<Video> doInBackground(Void... params) {
		ArrayList<Video> videoList = new ArrayList<Video>();
		try {
			// Get a httpclient to talk to the internet
			HttpClient client = new DefaultHttpClient();
			// Perform a GET request to YouTube for a JSON list of all the videos by a specific user
			HttpUriRequest request = new HttpGet("https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId="
					+ playlistId + "&maxResults=25&key=AIzaSyAKgCx_f4jAfwjgrIbV5ZCXqyuwpk-oK5o");

			HttpResponse response = client.execute(request);
			String jsonString = StreamUtils.convertToString(response.getEntity().getContent());
			JSONObject json = new JSONObject(jsonString);

			JSONArray jsonArray = new JSONArray(json.optString("items"));
			for (int i = 0; i < jsonArray.length(); i++) {

				JSONObject jsonObject = jsonArray.getJSONObject(i);

				JSONObject snippetObj = new JSONObject(jsonObject.optString("snippet"));
				String title = snippetObj.optString("title");

				String description = snippetObj.optString("description");
				JSONObject thumbnailsObject = new JSONObject(snippetObj.optString("thumbnails"));

				JSONObject mediumObject = new JSONObject(thumbnailsObject.optString("medium"));

				String mediumThumbNailUrl = mediumObject.optString("url");

				JSONObject resourceIdObject = new JSONObject(snippetObj.optString("resourceId"));
				String videoId = resourceIdObject.optString("videoId");

				Drawable thumbDrawable = LoadImageFromWebOperations(mediumThumbNailUrl);
				Video video = new Video(title, videoId, thumbDrawable, description);
				videoList.add(video);

			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			Log.e("ClientProtocolException", "" + e);
		} catch (IOException e) {
			e.printStackTrace();
			Log.e("IOException", "" + e);
		} catch (JSONException e) {
			e.printStackTrace();
			Log.e("JSONException", "" + e);
		}
		return videoList;
	}

	@Override
	protected void onPostExecute(ArrayList<Video> result) {
		super.onPostExecute(result);
		if (progress.isShowing())
			progress.dismiss();
		mListener.videoPlaylistAsyncCallback(result);
	}

	public static Drawable LoadImageFromWebOperations(String url) {
		try {
			InputStream is = (InputStream) new URL(url).getContent();
			Drawable d = Drawable.createFromStream(is, url);
			return d;
		} catch (Exception e) {
			return null;
		}
	}
}

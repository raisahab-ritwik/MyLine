/*
 * Copyright 2012 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.seva60plus.hum.mediacentre;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.seva60plus.hum.R;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

/**
 * Sample activity showing how to properly enable custom fullscreen behavior.
 * <p>
 * This is the preferred way of handling fullscreen because the default
 * fullscreen implementation will cause re-buffering of the video.
 */
public class PlayVideoActivity extends YouTubeFailureRecoveryActivity implements View.OnClickListener,
		CompoundButton.OnCheckedChangeListener, YouTubePlayer.OnFullscreenListener {

	private static final int PORTRAIT_ORIENTATION = Build.VERSION.SDK_INT < 9 ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
			: ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT;

	private LinearLayout baseLayout;
	private YouTubePlayerView playerView;
	private YouTubePlayer player;
	private Button fullscreenButton;
	private View otherViews;
	private TextView tv_video_description;
	private boolean fullscreen;
	private String YOUTUBE_VIDEO_CODE = "";
	private String YOUTUBE_VIDEO_DESCRIPTION = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.fullscreen_demo);
		baseLayout = (LinearLayout) findViewById(R.id.layout);
		playerView = (YouTubePlayerView) findViewById(R.id.player);
		fullscreenButton = (Button) findViewById(R.id.fullscreen_button);
		tv_video_description = (TextView) findViewById(R.id.tv_video_description);
		otherViews = findViewById(R.id.other_views);

		// You can use your own button to switch to fullscreen too
		fullscreenButton.setOnClickListener(this);

		playerView.initialize(DeveloperKey.DEVELOPER_KEY, this);
		Intent intObj = getIntent();
		YOUTUBE_VIDEO_CODE = intObj.getStringExtra("videocode");
		YOUTUBE_VIDEO_DESCRIPTION = intObj.getStringExtra("videoDescription");

		doLayout();

	}

	@Override
	public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
		this.player = player;
		setControlsEnabled();

		player.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
		player.setOnFullscreenListener(this);
		if (!wasRestored) {
			player.cueVideo(YOUTUBE_VIDEO_CODE);
		}
	}

	@Override
	protected YouTubePlayer.Provider getYouTubePlayerProvider() {
		return playerView;
	}

	@Override
	public void onClick(View v) {
		player.setFullscreen(!fullscreen);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		int controlFlags = player.getFullscreenControlFlags();
		if (isChecked) {

			setRequestedOrientation(PORTRAIT_ORIENTATION);
			controlFlags |= YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE;
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
			controlFlags &= ~YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE;
		}
		player.setFullscreenControlFlags(controlFlags);
	}

	private void doLayout() {
		LinearLayout.LayoutParams playerParams = (LinearLayout.LayoutParams) playerView.getLayoutParams();
		if (fullscreen) {

			playerParams.width = LayoutParams.MATCH_PARENT;
			playerParams.height = LayoutParams.MATCH_PARENT;

			otherViews.setVisibility(View.GONE);
		} else {

			otherViews.setVisibility(View.VISIBLE);
			ViewGroup.LayoutParams otherViewsParams = otherViews.getLayoutParams();
			if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
				playerParams.width = otherViewsParams.width = 0;
				playerParams.height = WRAP_CONTENT;
				otherViewsParams.height = MATCH_PARENT;
				playerParams.weight = 1;
				baseLayout.setOrientation(LinearLayout.HORIZONTAL);
			} else {
				playerParams.width = otherViewsParams.width = MATCH_PARENT;
				playerParams.height = WRAP_CONTENT;
				playerParams.weight = 5;
				otherViewsParams.height = 0;
				baseLayout.setOrientation(LinearLayout.VERTICAL);
			}
			setControlsEnabled();
		}
	}

	private void setControlsEnabled() {

		fullscreenButton.setEnabled(player != null);
	}

	@Override
	public void onFullscreen(boolean isFullscreen) {
		fullscreen = isFullscreen;
		doLayout();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		doLayout();
	}

}

package com.seva60plus.hum.model;

import java.io.Serializable;

import android.graphics.drawable.Drawable;

public class Video implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// The title of the video
	private String title;
	// A link to the video on youtube
	private String url;
	// A link to a still image of the youtube video
	private Drawable thumbDrawable;
	//description of the video
	private String description;

	public Video(String title, String url, Drawable thumbDrawable, String description) {
		super();
		this.title = title;
		this.url = url;
		this.thumbDrawable = thumbDrawable;
		this.description = description;

	}

	/**
	 * @return the title of the video
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return the url to this video on youtube
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @return the thumbUrl of a still image representation of this video
	 */
	public Drawable getthumbDrawable() {
		return thumbDrawable;
	}

	/**
	 * @return the description of the video
	 */
	public String getVideoDescription() {
		return description;
	}
}

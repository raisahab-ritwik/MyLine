package com.seva60plus.hum.wellbeing;

import android.graphics.Color;

public class ColorTemplate {

	public static final int[] PALETTE_SLEEP = { Color.rgb(202, 26, 26), Color.rgb(39, 192, 22), Color.rgb(19, 146, 243), Color.rgb(178, 183, 177) };

	public static final int[] PALETTE_EXERCISE = { Color.rgb(39, 192, 22), Color.rgb(202, 26, 26), Color.rgb(178, 183, 177) };

	public static final int[] PALETTE_MOOD = { Color.rgb(39, 192, 22), Color.rgb(19, 146, 243), Color.rgb(202, 26, 26), Color.rgb(178, 183, 177) };

	/**
	 * Returns the Android ICS holo blue light color.
	 * 
	 * @return
	 */
	public static int getHoloBlue() {
		return Color.rgb(51, 181, 229);
	}
}

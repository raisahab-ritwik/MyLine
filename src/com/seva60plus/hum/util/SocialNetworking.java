package com.seva60plus.hum.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

public class SocialNetworking {
	public static void shareAppLinkViaFacebook(Context mContext) {
		Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/Seva60Plus"));
		mContext.startActivity(i);

	}

	public static void shareAppLinkViaTwitter(Context mContext) {
		String urlToShare = "Please spread the word : Seva60Plus HUM Download Link : https://play.google.com/store/apps/details?id=com.seva60plus.hum";

		try {
			Intent intent1 = new Intent();
			intent1.setClassName("com.twitter.android", "com.twitter.android.PostActivity");
			intent1.setAction("android.intent.action.SEND");
			intent1.setType("text/plain");
			intent1.putExtra("android.intent.extra.TEXT", urlToShare);
			mContext.startActivity(intent1);
		} catch (Exception e) {
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("text/plain");
			String sharerUrl = "https://twitter.com/intent/tweet?text=Please spread the word : Seva60Plus HUM";
			intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
			mContext.startActivity(intent);
		}

	}

	public static void shareAppLinkViaWhatsApp(Context mContext) {
		Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
		whatsappIntent.setType("text/plain");
		whatsappIntent.setPackage("com.whatsapp");
		whatsappIntent.putExtra(Intent.EXTRA_TEXT,
				"Please spread the word : Seva60Plus HUM Download Link : https://play.google.com/store/apps/details?id=com.seva60plus.hum");
		try {
			mContext.startActivity(whatsappIntent);
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(mContext, "Whatsapp have not been installed.", Toast.LENGTH_SHORT).show();
		}
	}

}

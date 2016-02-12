package com.seva60plus.hum.activity;

import java.util.List;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.seva60plus.hum.R;

public class AboutSeva60 extends Activity {

	Button close;

	TextView phoneText, phoneText2, webText, emailText, facebookText, twitterText, nameText;
	RelativeLayout phoneLay, phoneLay2, webLay, emailLay, facebookLay, twitterLay;
	Button callBtn, call2Btn, visitBtn, sendBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_seva60);

		Typeface font = Typeface.createFromAsset(getAssets(), "openSansRegular.ttf");

		phoneText = (TextView) findViewById(R.id.phone_text);
		phoneText.append("+91 9836301516");
		phoneText.setTypeface(font);
		phoneLay = (RelativeLayout) findViewById(R.id.phone_text_lay);
		callBtn = (Button) findViewById(R.id.call_icon2);

		phoneLay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				String phNo = "+919836301516";

				try {
					Intent my_callIntent = new Intent(Intent.ACTION_CALL);
					my_callIntent.setData(Uri.parse("tel:" + phNo));
					//here the word 'tel' is important for making a call...
					startActivity(my_callIntent);
				} catch (ActivityNotFoundException e) {
					Toast.makeText(getApplicationContext(), "Error in your phone call" + e.getMessage(), Toast.LENGTH_LONG).show();
				}

			}
		});
		callBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				String phNo = "+919836301516";

				try {
					Intent my_callIntent = new Intent(Intent.ACTION_CALL);
					my_callIntent.setData(Uri.parse("tel:" + phNo));
					//here the word 'tel' is important for making a call...
					startActivity(my_callIntent);
				} catch (ActivityNotFoundException e) {
					Toast.makeText(getApplicationContext(), "Error in your phone call" + e.getMessage(), Toast.LENGTH_LONG).show();
				}

			}
		});

		phoneText2 = (TextView) findViewById(R.id.phone_text4);
		phoneText2.append("+91 9717567645");
		phoneText2.setTypeface(font);
		phoneLay2 = (RelativeLayout) findViewById(R.id.phone_text_lay4);
		call2Btn = (Button) findViewById(R.id.call_icon4copy);

		phoneLay2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				String phNo = "+919717567645";

				try {
					Intent my_callIntent = new Intent(Intent.ACTION_CALL);
					my_callIntent.setData(Uri.parse("tel:" + phNo));
					//here the word 'tel' is important for making a call...
					startActivity(my_callIntent);
				} catch (ActivityNotFoundException e) {
					Toast.makeText(getApplicationContext(), "Error in your phone call" + e.getMessage(), Toast.LENGTH_LONG).show();
				}

			}
		});
		call2Btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				String phNo = "+919717567645";

				try {
					Intent my_callIntent = new Intent(Intent.ACTION_CALL);
					my_callIntent.setData(Uri.parse("tel:" + phNo));
					//here the word 'tel' is important for making a call...
					startActivity(my_callIntent);
				} catch (ActivityNotFoundException e) {
					Toast.makeText(getApplicationContext(), "Error in your phone call" + e.getMessage(), Toast.LENGTH_LONG).show();
				}

			}
		});

		webText = (TextView) findViewById(R.id.TextView01);
		webText.setText("www.seva60plus.co.in");
		webText.setTypeface(font);
		webLay = (RelativeLayout) findViewById(R.id.RelativeLayout01);
		visitBtn = (Button) findViewById(R.id.Button01);

		webLay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.seva60plus.co.in"));
				startActivity(i);

			}
		});
		visitBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.seva60plus.co.in"));
				startActivity(i);

			}
		});

		emailText = (TextView) findViewById(R.id.web_text);
		emailText.setText("support@seva60plus.co.in");
		emailText.setTypeface(font);
		emailLay = (RelativeLayout) findViewById(R.id.website_text_lay);
		sendBtn = (Button) findViewById(R.id.web_text_icon2);

		emailLay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
				intent.setType("text/plain");
				intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "support@seva60plus.co.in" });
				final PackageManager pm = getPackageManager();
				final List<ResolveInfo> matches = pm.queryIntentActivities(intent, 0);
				ResolveInfo best = null;
				for (final ResolveInfo info : matches)
					if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail"))
						best = info;
				if (best != null)
					intent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
				startActivity(intent);

			}
		});
		sendBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
				intent.setType("text/plain");
				intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "support@seva60plus.co.in" });
				final PackageManager pm = getPackageManager();
				final List<ResolveInfo> matches = pm.queryIntentActivities(intent, 0);
				ResolveInfo best = null;
				for (final ResolveInfo info : matches)
					if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail"))
						best = info;
				if (best != null)
					intent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
				startActivity(intent);

			}
		});

		facebookText = (TextView) findViewById(R.id.distance_text);
		facebookText.setText("www.facebook.com/Seva60Plus");
		facebookText.setTypeface(font);
		facebookLay = (RelativeLayout) findViewById(R.id.distance_text_lay);

		facebookLay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/Seva60Plus"));
				startActivity(i);

			}
		});

		twitterText = (TextView) findViewById(R.id.distance_text2);
		twitterText.setText("www.twitter.com/seva60plus");
		twitterText.setTypeface(font);
		twitterLay = (RelativeLayout) findViewById(R.id.distance_text_lay2);

		twitterLay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.twitter.com/seva60plus"));
				startActivity(i);

			}
		});

		nameText = (TextView) findViewById(R.id.name_text);
		nameText.setText("Seva60Plus is a mobile application focused towards making search easier for our Elders in India."
				+ "Seva60Plus is a mobile only initiative that helps find the right products and services elders would require in their daily lives, based on their geographical location.");
		/*
		 * TextView text = (TextView)findViewById(R.id.about_us);
		 * 
		 * TextView text2 = (TextView)findViewById(R.id.about_us_text);
		 * 
		 * text2.setText(Html.fromHtml("Email <br/> "+
		 * "<u><font color='blue'>support@seva60plus.co.in</u></font>"));
		 * text2.setMovementMethod(LinkMovementMethod.getInstance());
		 * text2.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub final Intent intent = new
		 * Intent(android.content.Intent.ACTION_SEND);
		 * intent.setType("text/plain"); intent.putExtra(Intent.EXTRA_EMAIL ,
		 * new String[]{"support@seva60plus.co.in"}); final PackageManager pm =
		 * getPackageManager(); final List<ResolveInfo> matches =
		 * pm.queryIntentActivities(intent, 0); ResolveInfo best = null; for
		 * (final ResolveInfo info : matches) if
		 * (info.activityInfo.packageName.endsWith(".gm") ||
		 * info.activityInfo.name.toLowerCase().contains("gmail")) best = info;
		 * if (best != null) intent.setClassName(best.activityInfo.packageName,
		 * best.activityInfo.name); startActivity(intent); } });
		 * 
		 * TextView text3 = (TextView)findViewById(R.id.about_us_text2);
		 * text3.setText(Html.fromHtml("<br/>Phone  <br/>" +
		 * "+91 98713 21775 <br/>+91 9831190761" +"<br/><br/>"+
		 * "Facebook  <br/>"+
		 * "<a href=\"https://www.facebook.com/Seva60Plus/\">https://www.facebook.com/Seva60Plus/</a>"
		 * +"<br/><br/>"+ "Twitter  <br/>"+
		 * "<a href=\"https://twitter.com/seva60plus/\">https://twitter.com/seva60plus/</a>"
		 * +"<br/><br/>"+ "<br/>"));
		 * text3.setMovementMethod(LinkMovementMethod.getInstance());
		 * 
		 * 
		 * 
		 * text.setText(Html.fromHtml(
		 * "Seva60Plus is a mobile application focused towards making search easier for our Elders in India."
		 * +
		 * "Seva60Plus is a mobile only initiative that helps find the right products and services elders would require in their daily lives, based on their geographical location."
		 * +"<br/><br/>"+ "Website  <br/>"+
		 * "<a href=\"http://seva60plus.co.in/\">http://seva60plus.co.in/</a>"
		 * +"<br/>"+ ""));
		 * 
		 * text.setMovementMethod(LinkMovementMethod.getInstance());
		 * 
		 * Typeface font = Typeface.createFromAsset(getAssets(),
		 * "openSansRegular.ttf"); text.setTypeface(font);
		 * text2.setTypeface(font); text3.setTypeface(font);
		 */
		close = (Button) findViewById(R.id.close);
		close.setTypeface(font);
		close.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// Take action.
				finish();

			}
		});

	}
}
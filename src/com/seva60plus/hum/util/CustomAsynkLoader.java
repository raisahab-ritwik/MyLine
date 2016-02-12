package com.seva60plus.hum.util;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

import com.seva60plus.hum.R;

/**
 * 
 * @author Ritwik Rai
 * 
 */
public class CustomAsynkLoader extends Dialog {

	Dialog mDialog;

	public CustomAsynkLoader(Context context) {
		super(context);

		mDialog = new Dialog(context);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		mDialog.setContentView(R.layout.custom_progress_dialog);
		mDialog.setCancelable(false);
		//		mDialog.show();
	}

	public void ShowDialog() {
		mDialog.show();
	}

	public void DismissDialog() {
		mDialog.dismiss();
	}

	public boolean isDialogShowing() {
		if (mDialog != null && mDialog.isShowing())
			return true;
		else
			return false;
	}
}

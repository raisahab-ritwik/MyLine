package com.seva60plus.hum.activity;

import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import android.app.Application;

import com.seva60plus.hum.R;

@ReportsCrashes(formKey = "", mailTo = "ritwikrai04@gmail.com", customReportContent = { ReportField.APP_VERSION_CODE, ReportField.APP_VERSION_NAME,
		ReportField.ANDROID_VERSION, ReportField.PHONE_MODEL, ReportField.CUSTOM_DATA, ReportField.STACK_TRACE, ReportField.LOGCAT }, forceCloseDialogAfterToast = false, mode = ReportingInteractionMode.TOAST, resToastText = R.string.crash_toast)
public class Seva60PlusHUMApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		//ACRA.init(this);
	}
}

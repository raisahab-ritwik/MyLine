package com.seva60plus.hum.wellbeing.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class WellBeingService extends Service {

	private static final Object sSyncAdapterLock = new Object();
	private static WellBeingSyncAdapter sSyncAdapter = null;

	@Override
	public void onCreate() {
		Log.v("WellBeingService", "WellBeing Service");
		synchronized (sSyncAdapterLock) {
			if (sSyncAdapter == null)
				sSyncAdapter = new WellBeingSyncAdapter(getApplicationContext(), true);
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return sSyncAdapter.getSyncAdapterBinder();
	}
}

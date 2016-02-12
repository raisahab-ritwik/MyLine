package com.seva60plus.hum.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

public class SpeedTestService extends Service {

	private static final int EXPECTED_SIZE_IN_BYTES = 1048576;// 1MB 1024*1024

	private static final double EDGE_THRESHOLD = 176.0;
	private static final double BYTE_TO_KILOBIT = 0.0078125;
	private static final double KILOBIT_TO_MEGABIT = 0.0009765625;

	private final int MSG_UPDATE_STATUS = 0;
	private final int MSG_UPDATE_CONNECTION_TIME = 1;
	private final int MSG_COMPLETE_STATUS = 2;

	private int UPDATE_THRESHOLD = 400;
	private static final String TAG = SpeedTestService.class.getSimpleName();

	public final static String MY_ACTION = "MY_ACTION";

	private DecimalFormat mDecimalFormater;

	private final int TIMEOUT = 8000;

	// private Context mContext;
	private Double speed = 0.00;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	// public SpeedTestService() {
	// // TODO Auto-generated constructor stub
	// }

	// public SpeedTestService(Context mContext) {
	// // TODO Auto-generated constructor stub
	//
	// this.mContext = mContext;
	// }

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// Toast.makeText(SpeedTestService.this, "onStartCommand called",
		// Toast.LENGTH_SHORT).show();

		mDecimalFormater = new DecimalFormat("##.##");
		new Thread(mWorker).start();

		return START_NOT_STICKY;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		// Toast.makeText(SpeedTestService.this, "onCreate called",
		// Toast.LENGTH_SHORT).show();
	}

	private void sendingBroadcast(boolean isStrongNetwork, String MSG) {
		Intent intent = new Intent();
		intent.setAction(MY_ACTION);

		intent.putExtra("NETWORK_CHECK", isStrongNetwork);
		intent.putExtra("MSG", MSG);
		sendBroadcast(intent);

		stopSelf();
	}

	private final Runnable mWorker = new Runnable() {

		@Override
		public void run() {
			InputStream stream = null;
			try {
				int bytesIn = 0;

				String downloadFileUrl = "http://seva60plus.co.in/seva60PlusAndroidAPI/register/checkUserRegistered/123";
				long startCon = System.currentTimeMillis();
				URL url = new URL(downloadFileUrl);
				URLConnection con = url.openConnection();
				con.setConnectTimeout(TIMEOUT);
				con.setReadTimeout(TIMEOUT);
				con.setUseCaches(false);
				long connectionLatency = System.currentTimeMillis() - startCon;
				stream = con.getInputStream();

				Message msgUpdateConnection = Message.obtain(mHandler, MSG_UPDATE_CONNECTION_TIME);
				msgUpdateConnection.arg1 = (int) connectionLatency;
				mHandler.sendMessage(msgUpdateConnection);

				long start = System.currentTimeMillis();
				int currentByte = 0;
				long updateStart = System.currentTimeMillis();
				long updateDelta = 0;
				int bytesInThreshold = 0;

				while ((currentByte = stream.read()) != -1) {
					bytesIn++;
					bytesInThreshold++;
					if (updateDelta >= UPDATE_THRESHOLD) {
						int progress = (int) ((bytesIn / (double) EXPECTED_SIZE_IN_BYTES) * 100);
						Message msg = Message.obtain(mHandler, MSG_UPDATE_STATUS, calculate(updateDelta, bytesInThreshold));
						msg.arg1 = progress;
						msg.arg2 = bytesIn;
						mHandler.sendMessage(msg);
						// Reset
						updateStart = System.currentTimeMillis();
						bytesInThreshold = 0;
					}
					updateDelta = System.currentTimeMillis() - updateStart;
				}

				long downloadTime = (System.currentTimeMillis() - start);

				// Prevent AritchmeticException
				if (downloadTime == 0) {
					downloadTime = 1;
				}

				Message msg = Message.obtain(mHandler, MSG_COMPLETE_STATUS, calculate(downloadTime, bytesIn));
				msg.arg1 = bytesIn;
				mHandler.sendMessage(msg);
			} catch (MalformedURLException e) {
				Log.e(TAG, "Malformed Url");
				sendingBroadcast(false, "Malformed Url");
			} catch (IOException e) {
				Log.e(TAG, "IOException");
				sendingBroadcast(false, "IOException");
			} finally {
				try {
					if (stream != null) {
						stream.close();
					}
				} catch (IOException e) {
					// Suppressed

				}
			}

		}
	};

	private final Handler mHandler = new Handler() {

		@Override
		public void handleMessage(final Message msg) {

			switch (msg.what) {

			case MSG_UPDATE_STATUS:
				final SpeedInfo info1 = (SpeedInfo) msg.obj;
				Log.d("Speed at start::", "" + mDecimalFormater.format(info1.kilobits));
				break;
			case MSG_UPDATE_CONNECTION_TIME:
				break;

			case MSG_COMPLETE_STATUS:

				final SpeedInfo info2 = (SpeedInfo) msg.obj;
				Log.d("Speed at completion::", "" + info2.kilobits);

				speed = info2.kilobits;

				Log.v("SPEED:", speed + "");

				if (speed >= 160) {
					sendingBroadcast(true, "Network connection is strong!");
				} else {
					sendingBroadcast(false, "Network connection is weak!");
				}

				break;
			default:
				super.handleMessage(msg);
			}
		}
	};

	private SpeedInfo calculate(final long downloadTime, final long bytesIn) {
		SpeedInfo info = new SpeedInfo();
		// from mil to sec
		long bytespersecond = (bytesIn / downloadTime) * 1000;
		double kilobits = bytespersecond * BYTE_TO_KILOBIT;
		double megabits = kilobits * KILOBIT_TO_MEGABIT;
		info.downspeed = bytespersecond;
		info.kilobits = kilobits;
		info.megabits = megabits;

		return info;
	}

	/**
	 * Transfer Object
	 * 
	 * @author devil
	 * 
	 */
	private static class SpeedInfo {
		public double kilobits = 0;
		public double megabits = 0;
		public double downspeed = 0;
	}

	public Double getSpeed() {

		Log.v("ServiceTest", "" + speed);
		if (speed != 0.00) {
			System.out.println("CONDITION TRUE");
			return speed;

		} else {
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					getSpeed();
				}
			}, 1000);

		}
		return speed;
	}

	private int networkType(final double kbps) {
		int type = 1;// 3G
		// Check if its EDGE
		if (kbps < EDGE_THRESHOLD) {
			type = 0;
		}
		return type;
	}

	@Override
	public void onDestroy() {
		// Toast.makeText(getApplicationContext(), "Service Stopped",
		// Toast.LENGTH_SHORT).show();
		stopSelf();
	}
}

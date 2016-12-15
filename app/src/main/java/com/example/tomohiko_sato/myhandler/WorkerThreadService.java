package com.example.tomohiko_sato.myhandler;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class WorkerThreadService extends Service {
	private final static String TAG = WorkerThreadService.class.getSimpleName();
	private ServiceBinder binder = new ServiceBinder();
	private Handler serviceHandler;

	private final static int MSG_HELLO = 123;
	private final static int MSG_BYE = 343;

	private final static String MSG_HELLO_OBJ = "konichiwa";
	private final static String MSG_BYE_OBJ = "sayonara";

	public WorkerThreadService() {
	}

	class ServiceBinder extends Binder {
		public WorkerThreadService getService() {
			return WorkerThreadService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		Log.d(TAG, "onBind");
		HandlerThread handlerThread = new HandlerThread("WorkerThreadService");
		handlerThread.start();

		serviceHandler = new Handler(handlerThread.getLooper()) {
			@Override
			public void handleMessage(Message msg) {
				Log.d(TAG, msg.toString());

				String messageObj = (String) msg.obj;
				switch (msg.what) {
					case MSG_HELLO:
						Toast.makeText(WorkerThreadService.this, messageObj, Toast.LENGTH_SHORT).show();
						break;
					case MSG_BYE:
						Toast.makeText(WorkerThreadService.this, messageObj, Toast.LENGTH_SHORT).show();
						break;
				}
			}
		};
		return binder;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		Log.d(TAG, "onUnbind");
		serviceHandler.getLooper().quit();
		return false;
	}

	public void hello() {
		serviceHandler.sendMessage(serviceHandler.obtainMessage(MSG_HELLO, MSG_HELLO_OBJ));
	}

	public void bye() {
		serviceHandler.sendMessage(serviceHandler.obtainMessage(MSG_BYE, MSG_BYE_OBJ));
	}
}

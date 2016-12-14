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
	private final Handler serviceHandler;

	private final static int MSG_HELLO = 123;
	private final static int MSG_BYE = 343;

	public WorkerThreadService() {
		HandlerThread handlerThread = new HandlerThread("WorkerThreadService");
		handlerThread.start();

		serviceHandler = new Handler(handlerThread.getLooper()) {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
					case MSG_HELLO:
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						Toast.makeText(WorkerThreadService.this, "hello", Toast.LENGTH_SHORT).show();
						break;
					case MSG_BYE:
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						Toast.makeText(WorkerThreadService.this, "bye", Toast.LENGTH_SHORT).show();
						break;
				}
			}
		};
	}

	class ServiceBinder extends Binder {
		public WorkerThreadService getService() {
			return WorkerThreadService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}

	public void hello() {
		serviceHandler.sendMessage(serviceHandler.obtainMessage(MSG_HELLO));
	}

	public void bye() {
		serviceHandler.sendMessage(serviceHandler.obtainMessage(MSG_BYE));
	}
}

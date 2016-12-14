package com.example.tomohiko_sato.myhandler;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

	private final String TAG = MainActivity.class.getSimpleName();
	private WorkerThreadService service;
	private boolean isBound;

	private final ServiceConnection conn = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder binder) {
			service = ((WorkerThreadService.ServiceBinder) binder).getService();
			Log.d(TAG, "service connected");
			isBound = true;
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			Log.d(TAG, "service disconnected");
			isBound = false;
			service = null;
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findViewById(R.id.button_hello).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isBound) {
					service.hello();
				}
			}
		});
		findViewById(R.id.button_bye).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isBound) {
					service.bye();
				}
			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.d(TAG, "bind service");
		bindService(new Intent(this, WorkerThreadService.class), conn, BIND_AUTO_CREATE);
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.d(TAG, "unbind service");
		unbindService(conn);
		isBound = false;
	}
}

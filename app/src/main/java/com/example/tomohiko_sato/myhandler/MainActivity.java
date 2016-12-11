package com.example.tomohiko_sato.myhandler;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
//		final TextView tv = (TextView) findViewById(R.id.text_view);

	}

	public class LooperThread extends Thread {
		Looper looper;

		@Override
		public void run() {
			Looper.prepare();
			looper = Looper.myLooper();
			synchronized (this) {
				notifyAll();
			}
			Looper.loop();
		}

		public Looper getLooper() {
			try {
				synchronized (this) {
					wait();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return looper;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		LooperThread looperThread = new LooperThread();
		looperThread.start();

		new Handler(looperThread.getLooper()).post(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(MainActivity.this, "fugafuga", Toast.LENGTH_SHORT).show();
			}
		});


	}
}

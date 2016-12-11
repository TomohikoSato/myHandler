package com.example.tomohiko_sato.myhandler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

	LooperThread looperThread;

	TextView textView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		textView = (TextView) findViewById(R.id.text_view);

		Button button = (Button) findViewById(R.id.button);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				looperThread.handler.sendMessage(Message.obtain(null, 345, "hogehoge"));
			}
		});

		looperThread = new LooperThread();
		looperThread.start();
	}


	class LooperThread extends Thread {
		public Handler handler;

		public void run() {
			Looper.prepare();

			handler = new Handler() {
				public void handleMessage(Message msg) {
					Log.d("handler", msg.toString());
					Toast.makeText(MainActivity.this, "handler", Toast.LENGTH_SHORT).show();
					textView.setText("hogehgoehoge");
				}
			};

			Looper.loop();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
}

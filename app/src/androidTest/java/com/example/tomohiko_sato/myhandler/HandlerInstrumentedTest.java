package com.example.tomohiko_sato.myhandler;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static android.os.Looper.getMainLooper;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@RunWith(AndroidJUnit4.class)
public class HandlerInstrumentedTest {

	@Test
	public void Handlerでメインスレッドに通知できる() {
		//テストはワーカースレッドで動作しているようなのでMainLooperを指定
		final Handler handler = new Handler(Looper.getMainLooper());

		new Thread(new Runnable() {
			@Override
			public void run() {
				handler.post(new Runnable() {
					@Override
					public void run() {
						assertEquals(Thread.currentThread(), getMainLooper().getThread());
					}
				});
			}
		}).start();
	}

	@Test
	public void 普通に生成したThreadにはLooperが付いていないのでHandlerが使えない() {
		final Runnable mockR = mock(Runnable.class);

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					new Handler().post(mockR);
				} catch (RuntimeException e) {
					assertEquals(e.getMessage(), "Can't create handler inside thread that has not called Looper.prepare()");
				}
			}
		}).start();

		verify(mockR, never()).run();
	}


	@Test
	public void HandlerThreadではHandlerが使える() {
		HandlerThread handlerThread = new HandlerThread("hoge");
		handlerThread.start();
		Runnable mockR = mock(Runnable.class);
		new Handler(handlerThread.getLooper()).post(mockR);

		verify(mockR, timeout(100).times(1)).run(); // 非同期にrun()が呼ばれるので少し遅延させる
	}

	private final static int MSG_HELLO = 123;
	private final static int MSG_BYE = 343;

	private final static String MSG_HELLO_OBJ = "konichiwa";
	private final static String MSG_BYE_OBJ = "sayonara";

	@Test
	public void sendMessageするとonHandleMessageが呼ばれる() {

		Handler handler = new Handler(getMainLooper()) {
			@Override
			public void handleMessage(Message msg) {
				String messageObj = (String) msg.obj;

				switch (msg.what) {
					case MSG_HELLO:
						assertEquals(MSG_HELLO_OBJ, messageObj);
						break;
					case MSG_BYE:
						assertEquals(MSG_BYE_OBJ, messageObj);
						break;
				}
			}
		};

		handler.sendMessage(handler.obtainMessage(MSG_HELLO, MSG_HELLO_OBJ));
		handler.sendMessage(handler.obtainMessage(MSG_BYE, MSG_BYE_OBJ));
	}
}

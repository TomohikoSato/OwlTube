package com.example.tomohiko_sato.owltube.presentation.player;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.tomohiko_sato.owltube.R;
import com.example.tomohiko_sato.owltube.config.Key;
import com.google.android.youtube.player.YouTubePlayerView;

/**
 * 他のアプリケーションの上でも表示できる動画再生プレイヤーの管理をするサービス
 */
public class ExternalPlayerService extends Service {
	private final static String TAG = ExternalPlayerService.class.getSimpleName();

	private WindowManager windowManager;
	private FrameLayout overlapView;
	private WindowManager.LayoutParams overlapViewParams;
	private YouTubePlayerView externalPlayerView;

	public ExternalPlayerService() {
	}

	public static void bindService (Context context, ServiceConnection connection) {
		Intent intent = new Intent(context, ExternalPlayerService.class);
		context.bindService(intent, connection, Context.BIND_AUTO_CREATE);
	}

	public static void startService(Context context) {
		Intent intent = new Intent(context, ExternalPlayerService.class);
		context.startService(intent);
	}

	//bindするのか
	public void addView (View view) {
		overlapView.addView(view);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(TAG, "onStartCommand Received start id " + startId + ": " + intent);
		Toast.makeText(this, "ExternalPlayerService#onStartCommand", Toast.LENGTH_SHORT).show();

		// nothing to do??

/*
		Button closeButton = (Button) layout.findViewById(R.id.button_close);
		closeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d(TAG, "onclick");
			}
		});
		overlapView.addView(layout);
*/

		return START_STICKY;
	}

	private final ExternalPlayerServiceBinder binder = new ExternalPlayerServiceBinder();
	public class ExternalPlayerServiceBinder extends Binder {
		public ExternalPlayerService getService() {
			return ExternalPlayerService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		overlapView = new FrameLayout(getApplicationContext());
		overlapViewParams = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,       // アプリケーションのTOPに配置
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |  // フォーカスを当てない(下の画面の操作がd系なくなるため)
						WindowManager.LayoutParams.FLAG_FULLSCREEN |        // OverlapするViewを全画面表示
						WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, // モーダル以外のタッチを背後のウィンドウへ送信
				PixelFormat.TRANSLUCENT);  // viewを透明にする

		windowManager.addView(overlapView, overlapViewParams);

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Log.d(TAG, "removeView");
				windowManager.removeView(overlapView);
				stopSelf();
			}
		}, 20 * 1000);

		return binder;
	}

	@Override
	public void onDestroy() {
		Log.i(TAG, "onDestroy");
		Toast.makeText(this, "ExternalPlayerService#onDestroy", Toast.LENGTH_SHORT).show();
	}
}

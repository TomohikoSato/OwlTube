package com.example.tomohiko_sato.owltube.presentation.player;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.tomohiko_sato.owltube.util.Logger;


/**
 * 他のアプリケーションの上でも表示できる動画再生プレイヤーの管理をするサービス
 */
public class ExternalPlayerService extends Service implements View.OnTouchListener {
	private WindowManager windowManager;
	private FrameLayout overlapView;
	private WindowManager.LayoutParams overlapViewParams;

	public ExternalPlayerService() {
	}

	public static void bindService(Context context, ServiceConnection connection) {
		Intent intent = new Intent(context, ExternalPlayerService.class);
		context.bindService(intent, connection, Context.BIND_AUTO_CREATE);
	}

	public static void startService(Context context) {
		Intent intent = new Intent(context, ExternalPlayerService.class);
		context.startService(intent);
	}

	//bindするのか
	public void addView(View view) {
		overlapView.addView(view);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Logger.i("onStartCommand Received start id " + startId + ": " + intent);
		Toast.makeText(this, "ExternalPlayerService#onStartCommand", Toast.LENGTH_SHORT).show();
		return START_STICKY;
	}

	private final ExternalPlayerServiceBinder binder = new ExternalPlayerServiceBinder();

	private float dX, dY;

	@Override
	public boolean onTouch(View view, MotionEvent event) {
		Logger.d(event.toString());
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				dX = view.getX() - event.getRawX();
				dY = view.getY() - event.getRawY();
				break;
			case MotionEvent.ACTION_MOVE:
				view.animate()
						.x(event.getRawX() + dX)
						.y(event.getRawY() + dY)
						.setDuration(0)
						.start();
				break;
			default:
				return false;
		}
		return true;
	}

	public class ExternalPlayerServiceBinder extends Binder {
		public ExternalPlayerService getService() {
			return ExternalPlayerService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		Logger.d("onBind " + intent.toString());
		windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		overlapView = new FrameLayout(getApplicationContext());
		overlapView.setOnTouchListener(this);
		overlapViewParams = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,       // アプリケーションのTOPに配置
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |  // フォーカスを当てない(下の画面の操作がd系なくなるため)
						WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, // モーダル以外のタッチを背後のウィンドウへ送信
				PixelFormat.TRANSLUCENT);  // viewを透明にする

		windowManager.addView(overlapView, overlapViewParams);

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Logger.d("delay time has come. removeView");
				windowManager.removeView(overlapView);
				stopSelf();
			}
		}, 30 * 1000);

		return binder;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		Logger.d("onUnbind");
		return super.onUnbind(intent);
	}

	@Override
	public void onDestroy() {
		Logger.i("onDestroy");
		Toast.makeText(this, "ExternalPlayerService#onDestroy", Toast.LENGTH_SHORT).show();
	}
}

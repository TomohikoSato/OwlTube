package com.example.tomohiko_sato.owltube.presentation.player;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.tomohiko_sato.owltube.R;
import com.example.tomohiko_sato.owltube.domain.data.Video;
import com.example.tomohiko_sato.owltube.util.Logger;

/**
 * WindowManager上で{@link PlayerView}を動かすためのサービス
 */
public class ExternalPlayerService extends Service {
	private static final String KEY_VIDEO = "KEY_VIDEO";
	private WindowManager windowManager;
	private PlayerView playerView;
	private TrashView trashView;

	/**
	 * サービスをスタートする。外部プレイヤーの再生を開始する。既に再生されている場合は新しいビデオの再生に切り替える。
	 *
	 * @param context
	 * @param video
	 */
	public static void startService(Context context, Video video) {
		Intent intent = new Intent(context, ExternalPlayerService.class);
		intent.putExtra(KEY_VIDEO, video);
		context.startService(intent);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Logger.i("startId:%d, intent:%s ", startId, intent);
		Video video = intent.getParcelableExtra(KEY_VIDEO);
		init(video);
		return START_STICKY;
	}

	private void init(Video video) {
		windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		this.setTheme(R.style.AppTheme);
		playerView = (PlayerView) LayoutInflater.from(this).inflate(R.layout.view_player, null);
		playerView.setVideo(video);

		trashView = (TrashView) LayoutInflater.from(this).inflate(R.layout.view_trash, null);
		ImageView image = (ImageView) trashView.findViewById(R.id.imageView);
		image.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.trash_vector));

		windowManager.addView(trashView, trashView.lp);

		new Handler().postDelayed(() -> {
			Logger.d("delay time has come. removeView");
			playerView.release();
			windowManager.removeView(playerView);
			playerView = null;
			stopSelf();
		}, 15 * 1000);
	}


	@Override
	public IBinder onBind(Intent intent) {
		throw new UnsupportedOperationException("only start service is available");
	}
}

package com.example.tomohiko_sato.owltube.presentation.player.external;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.IBinder;
import android.view.Gravity;

import com.example.tomohiko_sato.owltube.OwlTubeApp;
import com.example.tomohiko_sato.owltube.R;
import com.example.tomohiko_sato.owltube.common.util.Logger;
import com.example.tomohiko_sato.owltube.domain.data.Video;
import com.example.tomohiko_sato.owltube.domain.player.PlayerNotifier;

import javax.inject.Inject;


/**
 * WindowManager上で{@link ExternalPlayerView}と{@link TrashView}を動かすサービス
 */
public class ExternalPlayerService extends Service implements ExternalPlayerView.OnExternalPlayerViewMovedListener {
	private static final String KEY_VIDEO = "KEY_VIDEO";
	private static final int ONGOING_NOTIFICATION_ID = 234;

	private ExternalPlayerView externalPlayerView;
	private TrashView trashView;

	@Inject
	PlayerNotifier notifier;

	/**
	 * サービスをスタートする。外部プレイヤーの再生を開始する。既に再生されている場合は新しいビデオの再生に切り替える。
	 */
	public static void startService(Context context, Video video) {
		Intent intent = new Intent(context, ExternalPlayerService.class);
		intent.putExtra(KEY_VIDEO, video);
		context.startService(intent);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		((OwlTubeApp) getApplication()).getComponent().inject(this);
	}


	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Logger.i("startId:%d, intent:%s ", startId, intent);
		initView(intent.getParcelableExtra(KEY_VIDEO));
		startForeground(ONGOING_NOTIFICATION_ID, notifier.createForegroundNotification());
		return START_STICKY;
	}

	private void initView(Video video) {
		if (externalPlayerView != null) {
			return;
		}

		this.setTheme(R.style.AppTheme);
		externalPlayerView = ExternalPlayerView.initialize(this, video, this);
		trashView = TrashView.Initialize(this);
	}

	private void removeViews() {
		externalPlayerView.release();
		trashView.remove();
	}

	@Override
	public IBinder onBind(Intent intent) {
		throw new UnsupportedOperationException("only start service is available");
	}

	@Override
	public void OnPlayerPositionUpdated(Rect r, Status status) {
		switch (status) {
			case BEGIN_MOVE:
				trashView.appear();
				break;
			case MOVING:
				trashView.setIntersecting(isIntersectWithTrash());
				break;
			case END_MOVE:
				if (isIntersectWithTrash()) {
					Logger.e("intercect !!!!");
					Logger.d(r.toString());
					removeViews();
					stopSelf();
				} else {
					trashView.disappear();
				}
				break;
		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		externalPlayerView = null;
	}

	/**
	 * TrashViewと重なっているかチェックする
	 * 同じ基準点で{@link Rect}を扱うために、{@link #externalPlayerView}と{@link #trashView}の{@link Gravity}を揃える必要がある
	 *
	 * @return TrashViewと重なっている場合はtrue
	 */
	private boolean isIntersectWithTrash() {
		// 無効の場合は重なり判定を行わない
		if (!trashView.isTrashEnabled()) {
			return false;
		}

		// INFO:TrashViewとFloatingViewは同じGravityにする必要がある
		return Rect.intersects(externalPlayerView.getWindowDrawingRect(), trashView.getWindowDrawingRect());
	}
}

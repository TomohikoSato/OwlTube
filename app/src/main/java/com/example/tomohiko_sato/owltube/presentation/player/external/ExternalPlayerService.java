package com.example.tomohiko_sato.owltube.presentation.player.external;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.IBinder;
import android.view.Gravity;

import com.example.tomohiko_sato.owltube.OwlTubeApp;
import com.example.tomohiko_sato.owltube.R;
import com.example.tomohiko_sato.owltube.common.rx.RxBus;
import com.example.tomohiko_sato.owltube.common.util.Logger;
import com.example.tomohiko_sato.owltube.domain.data.Video;
import com.example.tomohiko_sato.owltube.domain.player.PlayerNotifier;
import com.example.tomohiko_sato.owltube.presentation.util.ServiceUtil;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

import static com.example.tomohiko_sato.owltube.domain.player.PlayerNotifier.State.PAUSING;
import static com.example.tomohiko_sato.owltube.domain.player.PlayerNotifier.State.PLAYING;


/**
 * WindowManager上で{@link ExternalPlayerView}と{@link TrashView}を動かすサービス
 */
public class ExternalPlayerService extends Service implements ExternalPlayerView.OnExternalPlayerViewMovedListener {
	private static final String KEY_VIDEO = "KEY_VIDEO";
	private static final int ONGOING_NOTIFICATION_ID = 234;

	private ExternalPlayerView externalPlayerView;
	private TrashView trashView;
	private boolean hasStarted = false;
	private CompositeDisposable disposer = new CompositeDisposable();

	@Inject
	PlayerNotifier notifier;

	@Inject
	RxBus rxBus;

	/**
	 * サービスをスタートする。外部プレイヤーの再生を開始する。既に再生されている場合は新しいビデオの再生に切り替える。
	 */
	public static void startService(Context context, Video video) {
		Intent intent = new Intent(context, ExternalPlayerService.class);
		intent.putExtra(KEY_VIDEO, video);
		context.startService(intent);
	}

	public static void stopIfRunning(Context context) {
		if (ServiceUtil.isServiceRunning(ExternalPlayerService.class, context)) {
			context.stopService(new Intent(context, ExternalPlayerService.class));
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
		((OwlTubeApp) getApplication()).getComponent().inject(this);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Logger.i("startId:%d, intent:%s ", startId, intent);
		Video video = intent.getParcelableExtra(KEY_VIDEO);

		if (hasStarted) {
			externalPlayerView.setVideo(video);
		} else {
			setupViews(video);
			disposer.add(notifier.createNotification(video).subscribe(
					(notification) -> startForeground(ONGOING_NOTIFICATION_ID, notification)));
			hasStarted = true;
		}

		disposer.add(rxBus.register(PlayerNotificationReceiver.PlayerNotificationStateChangedEvent.class, (event) -> {
			switch (event.getState()) {
				case PLAYING:
					disposer.add(notifier.createNotification(video, PLAYING).subscribe(
							(notification) -> startForeground(ONGOING_NOTIFICATION_ID, notification)));
					externalPlayerView.play();
					break;
				case PAUSING:
					disposer.add(notifier.createNotification(video, PAUSING).subscribe(
							(notification) -> startForeground(ONGOING_NOTIFICATION_ID, notification)));
					externalPlayerView.pause();
					break;
			}
		}));

		return START_STICKY;
	}

	private void setupViews(Video video) {
		this.setTheme(R.style.AppTheme);
		externalPlayerView = ExternalPlayerView.initialize(this, video, this);
		trashView = TrashView.Initialize(this);
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
		externalPlayerView.release();
		trashView.remove();
		disposer.dispose();
	}

	/**
	 * TrashViewと重なっているかチェックする
	 * 同じ基準点で{@link Rect}を扱うために、{@link #externalPlayerView}と{@link #trashView}の{@link Gravity}を揃える必要がある
	 *
	 * @return TrashViewと重なっている場合はtrue
	 */
	private boolean isIntersectWithTrash() {
		if (!trashView.isTrashEnabled()) {
			return false;
		}

		// INFO:TrashViewとFloatingViewは同じGravityにする必要がある
		return Rect.intersects(externalPlayerView.getWindowDrawingRect(), trashView.getWindowDrawingRect());
	}
}

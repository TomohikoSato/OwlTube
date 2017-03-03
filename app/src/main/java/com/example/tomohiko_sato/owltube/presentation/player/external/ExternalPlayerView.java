package com.example.tomohiko_sato.owltube.presentation.player.external;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.example.tomohiko_sato.owltube.R;
import com.example.tomohiko_sato.owltube.common.util.Logger;
import com.example.tomohiko_sato.owltube.domain.data.Video;
import com.example.tomohiko_sato.owltube.presentation.player.external.ExternalPlayerView.OnExternalPlayerViewMovedListener.Status;
import com.example.tomohiko_sato.owltube.presentation.player.external.TouchEventTranslater;
import com.pierfrancescosoffritti.youtubeplayer.AbstractYouTubeListener;
import com.pierfrancescosoffritti.youtubeplayer.YouTubePlayerView;

/**
 * プレイヤー用のビュー。
 * ドラッグできる。
 */
public class ExternalPlayerView extends FrameLayout {
	private final WindowManager wm;
	private final Rect playerRect = new Rect();
	private final Rect screenBoundsRect;
	private Video video;
	private OnExternalPlayerViewMovedListener listener;

	public ExternalPlayerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,    // アプリケーションのTOPに配置
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |  // フォーカスを当てない(下の画面の操作が出来なくなるため)
						WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, // モーダル以外のタッチを背後のウィンドウへ送信
				PixelFormat.TRANSLUCENT);
		lp.width = getResources().getDimensionPixelSize(R.dimen.player_float_width);
		lp.height = getResources().getDimensionPixelSize(R.dimen.player_float_height);
		lp.gravity = Gravity.START | Gravity.BOTTOM;

		wm.addView(this, lp);
		screenBoundsRect = getScreenRect();

		setOnTouchListener(new TouchEventTranslater(new TouchEventTranslater.OnMoveListener() {
			@Override
			public void onMoveStart() {
				listener.OnPlayerPositionUpdated(playerRect, Status.BEGIN_MOVE);
			}

			@Override
			public void onMoving(int dx, int dy) {
				updateLayout(dx, dy);
				listener.OnPlayerPositionUpdated(playerRect, Status.MOVING);
			}

			@Override
			public void onMoveEnd() {
				listener.OnPlayerPositionUpdated(playerRect, Status.END_MOVE);
			}
		},
				() -> {
					// TODO: たぶんいらない
					Logger.d("clicked");
				}));
	}

	private Rect getScreenRect() {
		Display display = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int w = size.x;
		int h = size.y;

		return new Rect(0, 0, w, h);
	}

	public void setVideo(Video video) {
		this.video = video;
	}

	public void setListener(OnExternalPlayerViewMovedListener l) {
		this.listener = l;
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		YouTubePlayerView player = (YouTubePlayerView) findViewById(R.id.youtube_player_view);
		player.initialize(new AbstractYouTubeListener() {
			@Override
			public void onReady() {
				player.loadVideo(video.videoId, 0);
			}
		}, true);
	}

	public void release() {
		((YouTubePlayerView) findViewById(R.id.youtube_player_view)).release();
		wm.removeView(this);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return true;
	}

	@Override
	public void onAttachedToWindow() {
		updateLayoutToCenter();
	}

	private Rect getCenterRect() {
		int w = getResources().getDimensionPixelSize(R.dimen.player_float_width);
		int h = getResources().getDimensionPixelSize(R.dimen.player_float_height);

		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int screenW = size.x;
		int screenH = size.y;
		int centerX = (screenW - w) / 2;
		int centerY = (screenH - h) / 2;

		return new Rect(centerX, centerY, centerX + w, centerY + h);
	}

	private void updateLayoutToCenter() {
		playerRect.set(getCenterRect());

		WindowManager.LayoutParams lp = (WindowManager.LayoutParams) getLayoutParams();
		lp.x = playerRect.left;
		lp.y = playerRect.top; // Gravity.Bottom なので y座標の方向が変わっている
		wm.updateViewLayout(this, lp);

		Logger.e(playerRect.toShortString());
	}

	private boolean keepPlayerInsideBounds() {
		Logger.e("player:" + playerRect.toShortString());
		Logger.e("bounds:" + screenBoundsRect.toShortString());
		if (playerRect.left < screenBoundsRect.left || playerRect.top < screenBoundsRect.top || screenBoundsRect.right < playerRect.right || screenBoundsRect.bottom < playerRect.bottom) {
			if (playerRect.left < screenBoundsRect.left) playerRect.left = screenBoundsRect.left;
			if (playerRect.top < screenBoundsRect.top) playerRect.top = screenBoundsRect.top;
			if (screenBoundsRect.right < playerRect.right)
				playerRect.right = screenBoundsRect.right;
			if (screenBoundsRect.bottom < playerRect.bottom)
				playerRect.bottom = screenBoundsRect.bottom;
			return true;
		}
		return false;
	}

	private void updateLayout(int dx, int dy) {
		if (!isAttachedToWindow()) {
			return;
		}
		if (keepPlayerInsideBounds()) {
			Logger.e("out of bounds!!");
			return;
		}

		WindowManager.LayoutParams lp = (WindowManager.LayoutParams) getLayoutParams();
		lp.x += dx;
		lp.y += dy;
		playerRect.offset(dx, dy);
		wm.updateViewLayout(this, lp);
	}

	public Rect getWindowDrawingRect() {
		Logger.e(playerRect.toShortString());
		return playerRect;
	}

	interface OnExternalPlayerViewMovedListener {
		void OnPlayerPositionUpdated(Rect r, Status status);

		enum Status {
			BEGIN_MOVE,
			MOVING,
			END_MOVE;
		}
	}
}

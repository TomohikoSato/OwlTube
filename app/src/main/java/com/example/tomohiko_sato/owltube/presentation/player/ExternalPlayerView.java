package com.example.tomohiko_sato.owltube.presentation.player;

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
import com.example.tomohiko_sato.owltube.domain.data.Video;
import com.example.tomohiko_sato.owltube.util.Logger;
import com.pierfrancescosoffritti.youtubeplayer.AbstractYouTubeListener;
import com.pierfrancescosoffritti.youtubeplayer.YouTubePlayerView;

/**
 * プレイヤー用のビュー。
 * ドラッグできる。
 */
public class ExternalPlayerView extends FrameLayout {
	private final WindowManager wm;
	private final Rect currentRect = new Rect();
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

		setOnTouchListener(new TouchEventTranslater(new TouchEventTranslater.OnMoveListener() {
			@Override
			public void onMoving(int dx, int dy) {
				updateLayout(dx, dy);
			}

			@Override
			public void onMoveEnd() {
				listener.OnPlayerPositionUpdated(currentRect);
			}
		},
				() -> {
					// TODO: たぶんいらない
					Logger.d("clicked");
				}));
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

	private void updateLayoutToCenter() {
		int w = getResources().getDimensionPixelSize(R.dimen.player_float_width);
		int h = getResources().getDimensionPixelSize(R.dimen.player_float_height);

		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int screenW = size.x;
		int screenH = size.y;
		int centerX = (screenW - w) / 2;
		int centerY = (screenH - h) / 2;

		WindowManager.LayoutParams lp = (WindowManager.LayoutParams) getLayoutParams();
		lp.x = centerX;
		lp.y = centerY; // Gravity.Bottom なので y座標の方向が変わっている
		wm.updateViewLayout(this, lp);

		currentRect.set(new Rect(centerX, centerY, centerX + w, centerY + h));
		logRect();
	}

	private void updateLayout(int dx, int dy) {
		if (!isAttachedToWindow()) {
			return;
		}
		WindowManager.LayoutParams lp = (WindowManager.LayoutParams) getLayoutParams();
		lp.x += dx;
		lp.y -= dy; // Gravity.Bottom なので y座標の方向が変わっている
		currentRect.offset(dx, dy);
		wm.updateViewLayout(this, lp);
	}

	public Rect getWindowDrawingRect() {
		logRect();
		return currentRect;
	}

	interface OnExternalPlayerViewMovedListener {
		void OnPlayerPositionUpdated(Rect r);
	}

	private void logRect() {
		Rect r = currentRect;
		Logger.e("[l:" + r.left + " t:" + r.top + " r:" + r.right + " b:" + r.bottom + "]");
	}
}

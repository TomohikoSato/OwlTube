package com.example.tomohiko_sato.owltube.presentation.player;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.util.AttributeSet;
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
	private final WindowManager windowManager;
	private final WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
			WindowManager.LayoutParams.WRAP_CONTENT,
			WindowManager.LayoutParams.WRAP_CONTENT,
			WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,    // アプリケーションのTOPに配置
			WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |  // フォーカスを当てない(下の画面の操作が出来なくなるため)
					WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, // モーダル以外のタッチを背後のウィンドウへ送信
			PixelFormat.TRANSLUCENT);  // viewを透明にする

	private Video video;
	private OnExternalPlayerViewMovedListener listener;
	private Rect currentRect;

	public ExternalPlayerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		lp.width = getResources().getDimensionPixelSize(R.dimen.player_float_width);
		lp.height = getResources().getDimensionPixelSize(R.dimen.player_float_height);
		windowManager.addView(this, lp);
		currentRect = new Rect(0, 0, lp.width, lp.height);

		setOnTouchListener(new TouchEventTranslater((dx, dy) -> {
			updateLayout(dx, dy);
		}, () -> {
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
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return true;
	}


	private void updateLayout(int dx, int dy) {
		if (!isAttachedToWindow()) {
			return;
		}
		WindowManager.LayoutParams lp = (WindowManager.LayoutParams) getLayoutParams();
		lp.x += dx;
		lp.y += dy;
		currentRect.offset(dx, dy);

		listener.OnPlayerPositionUpdated(MoveState.Moving, currentRect);
		windowManager.updateViewLayout(this, lp);
	}

	public Rect getWindowDrawingRect() {
		if (currentRect != null) {
			return currentRect;
		}

		int[] l = new int[2];
		getLocationInWindow(l);

		int x = l[0];
		int y = l[1];
		int w = getWidth();
		int h = getHeight();

		currentRect = new Rect(x, y, x + w, y + h);
		Logger.d(currentRect.toString());

		return currentRect;
	}

	enum MoveState {
		Moving,
		NotMoving;
	}

	interface OnExternalPlayerViewMovedListener {
		void OnPlayerPositionUpdated(MoveState state, Rect r);
	}
}

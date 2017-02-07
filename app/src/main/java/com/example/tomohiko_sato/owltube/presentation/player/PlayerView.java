package com.example.tomohiko_sato.owltube.presentation.player;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.example.tomohiko_sato.owltube.R;
import com.example.tomohiko_sato.owltube.common.util.Logger;
import com.pierfrancescosoffritti.youtubeplayer.AbstractYouTubeListener;
import com.pierfrancescosoffritti.youtubeplayer.YouTubePlayerView;

/**
 * プレイヤー用のビュー。
 * ２つのモード持っている
 * 1. ドラッグできる。
 * 2. ドラッグせずに関連動画を表示などもできる (fragment使うべきか？)
 */
public class PlayerView extends FrameLayout {
	private final WindowManager windowManager;
	private State currentState;
	private YouTubePlayerView player;

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		setCurrentState(State.FLOAT);
	}

	enum State {
		FLOAT,
		EXPAND
	}

	public PlayerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		setOnTouchListener(new TouchEventTranslater((dx, dy) -> {
			Logger.d("dx:%d, dy:%d", dx, dy);
			if (currentState == State.FLOAT) {
				updateLayout(dx, dy);
			}
		}, () -> {
			Logger.d("clicked");
			setCurrentState(currentState == State.EXPAND ? State.FLOAT : State.EXPAND);
		}));
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		player = (YouTubePlayerView) findViewById(R.id.youtube_player_view);
		player.initialize(new AbstractYouTubeListener() {
			@Override
			public void onReady() {
				player.loadVideo("6JYIGclVQdw", 0);
			}
		}, true);
	}

	public void release() {
		player.release();
	}

	private void setCurrentState(State state) {
		currentState = state;
		Logger.d(currentState.toString());
		switch (currentState) {
			case FLOAT:
				shrink();
				break;
			case EXPAND:
				fill();
				// 画面ぴったりにする
				break;
		}
	}

	private void shrink() {
		if (!isAttachedToWindow()) {
			Logger.e();
			return;
		}
		findViewById(R.id.for_expand).setVisibility(GONE);

		WindowManager.LayoutParams lp = (WindowManager.LayoutParams) getLayoutParams();
		lp.width = getResources().getDimensionPixelSize(R.dimen.player_float_width);
		lp.height = getResources().getDimensionPixelSize(R.dimen.player_float_height);
		windowManager.updateViewLayout(this, lp);

		ViewGroup.LayoutParams vglp = player.getLayoutParams();
		vglp.width = getResources().getDimensionPixelSize(R.dimen.player_float_width);
		vglp.height = getResources().getDimensionPixelSize(R.dimen.player_float_height);
		player.setLayoutParams(vglp);
		invalidate();
	}

	private void fill() {
		if (!isAttachedToWindow()) {
			Logger.e();
			return;
		}
		findViewById(R.id.for_expand).setVisibility(VISIBLE);

		Point size = new Point();
		windowManager.getDefaultDisplay().getSize(size);
		Logger.d(size.toString());
		int screenWidth = size.x;
		int screenHeight = size.y;
		WindowManager.LayoutParams lp = (WindowManager.LayoutParams) getLayoutParams();
		lp.width = screenWidth;
		lp.height = screenHeight;
		windowManager.updateViewLayout(this, lp);

		ViewGroup.LayoutParams vglp = player.getLayoutParams();
		vglp.width = screenWidth;
		vglp.height = 1000; //DEBUG:
		player.setLayoutParams(vglp);
		invalidate();
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
		windowManager.updateViewLayout(this, lp);
	}
}

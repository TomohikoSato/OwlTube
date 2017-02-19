package com.example.tomohiko_sato.owltube.presentation.player;

import android.view.MotionEvent;
import android.view.View;

import com.example.tomohiko_sato.owltube.common.util.Logger;

/**
 * タッチイベントを解釈し、{@link OnMoveListener}や{@link OnClickedListener} に伝える
 */
class TouchEventTranslater implements View.OnTouchListener {
	private final OnMoveListener move;
	private final OnClickedListener clicked;

	private int downX = 0;
	private int downY = 0;
	private int oldX = 0;
	private int oldY = 0;

	TouchEventTranslater(OnMoveListener move, OnClickedListener clicked) {
		this.move = move;
		this.clicked = clicked;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				downX = (int) event.getRawX();
				downY = (int) event.getRawY();
				oldX = downX;
				oldY = downY;
				break;
			case MotionEvent.ACTION_UP:
				int distance = (int) Math.sqrt(((event.getRawX() - downX) * (event.getRawX() - downX) + (event.getRawY() - downY) * (event.getRawY() - downY)));
				if (distance < 30) {
					clicked.onClicked();
				}
				move.onMoveEnd();
				break;
			default:
				Logger.e("rawX" + event.getRawX());
				Logger.e("X" + event.getX());
				int dx = (int) (event.getRawX() - oldX);
				int dy = (int) (event.getRawY() - oldY);
				oldX = (int) event.getRawX();
				oldY = (int) event.getRawY();
				move.onMoving(dx, (-1) * dy); // Gravity.Bottom なので y座標の方向が変わっている
				break;
		}

		return true;
	}

	interface OnMoveListener {
		void onMoving(int dx, int dy);

		void onMoveEnd();
	}

	interface OnClickedListener {
		void onClicked();
	}
}

package com.example.tomohiko_sato.owltube.presentation.player;

import android.view.MotionEvent;
import android.view.View;

/**
 * タッチイベントを解釈し、{@link OnMovedListener}や{@link OnClickedListener} に伝える
 */
class TouchEventTranslater implements View.OnTouchListener {
	private final OnMovedListener moved;
	private final OnClickedListener clicked;

	private int downX = 0;
	private int downY = 0;
	private int oldX = 0;
	private int oldY = 0;

	TouchEventTranslater(OnMovedListener moved, OnClickedListener clicked) {
		this.moved = moved;
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
				break;
			default:
				int dx = (int) (event.getRawX() - oldX);
				int dy = (int) (event.getRawY() - oldY);
				oldX = (int) event.getRawX();
				oldY = (int) event.getRawY();
				moved.onMoved(dx, dy);
				break;
		}

		return false;
	}

	interface OnMovedListener {
		void onMoved(int dx, int dy);
	}

	interface OnClickedListener {
		void onClicked();
	}
}

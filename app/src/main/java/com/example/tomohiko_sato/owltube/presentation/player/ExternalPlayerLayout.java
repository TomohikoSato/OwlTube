package com.example.tomohiko_sato.owltube.presentation.player;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * 外部プレイヤー用のレイアウトクラス
 */
public class ExternalPlayerLayout extends RelativeLayout {
	private float dX, dY;

	public ExternalPlayerLayout(Context context) {
		super(context);
	}

	public ExternalPlayerLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
/*
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// タッチに合わせてビューを動かす
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				dX = getX() - event.getRawX();
				dY = getY() - event.getRawY();
				break;
			case MotionEvent.ACTION_MOVE:
				animate()
						.x(event.getRawX() + dX)
						.y(event.getRawY() + dY)
						.setDuration(0)
						.start();
				break;
			default:
				return false;
		}
		return true;
	}*/
}

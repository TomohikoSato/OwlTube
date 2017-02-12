package com.example.tomohiko_sato.owltube.presentation.player;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.example.tomohiko_sato.owltube.util.Logger;

class TrashView extends RelativeLayout {
	private final WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
			WindowManager.LayoutParams.MATCH_PARENT,
			WindowManager.LayoutParams.WRAP_CONTENT,
			WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,    // アプリケーションのTOPに配置
			WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |  // フォーカスを当てない(下の画面の操作が出来なくなるため)
					WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, // モーダル以外のタッチを背後のウィンドウへ送信
			PixelFormat.TRANSLUCENT);  // viewを透明にする
	private Rect currentRect;

	public TrashView(Context context, AttributeSet attrs) {
		super(context, attrs);
		lp.gravity = Gravity.LEFT | Gravity.BOTTOM;
		lp.height = 400;
		((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).addView(this, lp);
	}

	public Rect getWindowDrawingRect() {
		if (currentRect != null) {
			Logger.d(currentRect.toString());
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
}

package com.example.tomohiko_sato.owltube.presentation.player;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.example.tomohiko_sato.owltube.R;

class TrashView extends RelativeLayout {
	private Rect currentRect;

	public TrashView(Context context, AttributeSet attrs) {
		super(context, attrs);

		WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,    // アプリケーションのTOPに配置
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |  // フォーカスを当てない(下の画面の操作が出来なくなるため)
						WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, // モーダル以外のタッチを背後のウィンドウへ送信
				PixelFormat.TRANSLUCENT);
		lp.height = getResources().getDimensionPixelSize(R.dimen.trash_height);
		lp.gravity = Gravity.START | Gravity.BOTTOM;
		((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).addView(this, lp);
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

		return currentRect;
	}
}

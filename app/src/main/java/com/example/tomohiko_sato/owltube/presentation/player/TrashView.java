package com.example.tomohiko_sato.owltube.presentation.player;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.RelativeLayout;

class TrashView extends RelativeLayout {
	final WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
			WindowManager.LayoutParams.MATCH_PARENT,
			WindowManager.LayoutParams.WRAP_CONTENT,
			WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,    // アプリケーションのTOPに配置
			WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |  // フォーカスを当てない(下の画面の操作が出来なくなるため)
					WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, // モーダル以外のタッチを背後のウィンドウへ送信
			PixelFormat.TRANSLUCENT);  // viewを透明にする


	public TrashView(Context context, AttributeSet attrs) {
		super(context, attrs);
		lp.gravity = Gravity.LEFT | Gravity.BOTTOM;
		lp.height = 400;

	}
}

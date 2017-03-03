package com.example.tomohiko_sato.owltube.presentation.player.external;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.tomohiko_sato.owltube.R;
import com.example.tomohiko_sato.owltube.common.util.Logger;

class TrashView extends RelativeLayout {
	private static final int ANIMATION_DURATION = 150;
	private final WindowManager wm;
	private final int centerYMargin;
	public boolean isTrashEnabled = false;
	private Rect currentRect;
	private ImageView trashIcon;

	public static TrashView Initialize(Context context) {
		return (TrashView) LayoutInflater.from(context).inflate(R.layout.view_trash, null);
	}

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
		wm = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE));
		wm.addView(this, lp);

		centerYMargin = getCenterYMargin();
	}

	public void onAttachedToWindow() {
		super.onAttachedToWindow();

		trashIcon = (ImageView) findViewById(R.id.trash_icon); // serviceでinflateしているからかLayoutでセットしても表示されないのでコード上でセットする
		trashIcon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.trash_vector));
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

	public void remove() {
		removeAllViews();
		wm.removeView(this);
	}


	public void appear() {
		Logger.d("appear");
		animate().alpha(1).setDuration(ANIMATION_DURATION).setInterpolator(new AccelerateDecelerateInterpolator());
		trashIcon.animate().translationYBy(-1 * centerYMargin);
		isTrashEnabled = true;
	}

	public void expand() {
		Logger.d("expand");
	}

	public void disappear() {
		Logger.d("disappear");
		animate().alpha(0).setDuration(ANIMATION_DURATION);
		trashIcon.animate().translationYBy(centerYMargin);
		isTrashEnabled = false;
	}

	private int getCenterYMargin() {
		int height = (int) getResources().getDimension(R.dimen.trash_height);
		int iconMargin = (int) getResources().getDimension(R.dimen.trash_icon_margin_top);
		int iconSize = (int) getResources().getDimension(R.dimen.trash_icon_size);
		return height - (iconMargin / 2) + iconSize;
	}
}

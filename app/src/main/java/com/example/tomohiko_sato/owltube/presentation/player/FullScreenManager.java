package com.example.tomohiko_sato.owltube.presentation.player;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;

class FullScreenManager {
	@NonNull
	private final Activity activity;

	FullScreenManager(@NonNull Activity activity) {
		this.activity = activity;
	}

	void enterFullScreen() {
		hideSystemUI(activity.getWindow().getDecorView());
	}

	void exitFullScreen() {
		showSystemUI(activity.getWindow().getDecorView());
	}

	// hides the system bars.
	private void hideSystemUI(View decorView) {
		decorView.setSystemUiVisibility(
				View.SYSTEM_UI_FLAG_LAYOUT_STABLE
						| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
						| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
						| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
						| View.SYSTEM_UI_FLAG_FULLSCREEN
						| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
	}

	// This snippet shows the system bars.
	private void showSystemUI(View mDecorView) {
		mDecorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
	}
}

package com.example.tomohiko_sato.owltube.domain.common;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import javax.inject.Inject;

/**
 * {@link android.Manifest.permission#SYSTEM_ALERT_WINDOW} 向けの権限ハンドリングクラス
 */
public class PermissionHandler {
	private final Context context;

	@Inject
	public PermissionHandler(Context context) {
		this.context = context;
	}

	public boolean hasPermission() {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
			return true;
		}

		return Settings.canDrawOverlays(context);
	}

	public void requestPermission() {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
			return;
		}


		Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
				Uri.parse("package:" + context.getPackageName()));
		context.startActivity(intent);
	}
}

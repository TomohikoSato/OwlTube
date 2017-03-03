package com.example.tomohiko_sato.owltube.domain.player;


import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.example.tomohiko_sato.owltube.R;
import com.example.tomohiko_sato.owltube.presentation.top.TopActivity;

import javax.inject.Inject;

public class PlayerNotifier {
	private final Context context;

	@Inject
	public PlayerNotifier(Context context) {
		this.context = context;
	}

	public Notification createForegroundNotification() {
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, TopActivity.class), 0);

		return new Notification.Builder(context)
				.setContentTitle("を再生中")
				.setContentText("content title")
				.setSmallIcon(R.drawable.trash_vector)
				.setContentIntent(pendingIntent)
				.build();
	}
}

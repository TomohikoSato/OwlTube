package com.example.tomohiko_sato.owltube.domain.player;


import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.NotificationCompat;

import com.example.tomohiko_sato.owltube.R;
import com.example.tomohiko_sato.owltube.domain.data.Video;
import com.example.tomohiko_sato.owltube.presentation.player.external.ExternalPlayerService;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PlayerNotifier {
	private final Context context;

	@Inject
	public PlayerNotifier(Context context) {
		this.context = context;
	}

	public Single<Notification> createNotification(Video video) {
		return Single.fromCallable(() -> create(video))
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io());
	}

	private Notification create(Video video) throws IOException {
		PendingIntent pi = PendingIntent.getActivity(context, 0, new Intent(context,
				ExternalPlayerService.class), 0);

		Bitmap artwork = Picasso.with(context).load(video.thumbnailUrl).get();

		NotificationCompat.Action playAction = new NotificationCompat.Action.Builder(R.drawable.notification_play, "再生", pi).build();
		NotificationCompat.Action nextAction = new NotificationCompat.Action.Builder(R.drawable.notification_next, "次へ", pi).build();
		NotificationCompat.Action prevAction = new NotificationCompat.Action.Builder(R.drawable.notification_prev, "戻る", pi).build();

		NotificationCompat.MediaStyle mediaStyle = new NotificationCompat.MediaStyle();
		mediaStyle.setShowActionsInCompactView(1);

		return new NotificationCompat.Builder(context)
				.setContentTitle(String.format("%sを再生中", video.title))
				.setContentText("content text")
				.setSmallIcon(R.drawable.trash_vector)
				.setLargeIcon(artwork)
				.setStyle(mediaStyle)
				.addAction(prevAction)
				.addAction(playAction)
				.addAction(nextAction)
				.build();
	}
}

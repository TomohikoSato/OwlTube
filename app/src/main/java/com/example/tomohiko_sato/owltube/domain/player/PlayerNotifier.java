package com.example.tomohiko_sato.owltube.domain.player;


import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.NotificationCompat;

import com.example.tomohiko_sato.owltube.R;
import com.example.tomohiko_sato.owltube.domain.data.Video;
import com.example.tomohiko_sato.owltube.presentation.player.external.PlayerNotificationReceiver;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PlayerNotifier {
	private static final int REQUEST_CODE_PLAYER_NOTIFICATION = 1;
	private final Context context;

	public enum State {
		PLAY, PAUSE;
		public static final String INTENT_KEY = "StateKey";
	}

	@Inject
	public PlayerNotifier(Context context) {
		this.context = context;
	}

	public Single<Notification> createNotification(Video video) {
		return Single.fromCallable(() -> create(video, State.PLAY))
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io());
	}

/*
	public void changeNotificationState(Notification notification, State state) {
		create()
	}
*/


	private Notification create(Video video, State state) throws IOException {
		PendingIntent pi = PendingIntent.getBroadcast(context, REQUEST_CODE_PLAYER_NOTIFICATION, new Intent(context,
				PlayerNotificationReceiver.class), PendingIntent.FLAG_UPDATE_CURRENT);

		Bitmap artwork = Picasso.with(context).load(video.thumbnailUrl).get();

		NotificationCompat.Action playAction = new NotificationCompat.Action.Builder(R.drawable.notification_play, "再生", pi).build();
		//NotificationCompat.Action pauseAction = new NotificationCompat.Action.Builder(R.drawable.notification_pause, "一時停止", pi).build();
//		NotificationCompat.Action nextAction = new NotificationCompat.Action.Builder(R.drawable.notification_next, "次へ", pi).build();
//		NotificationCompat.Action prevAction = new NotificationCompat.Action.Builder(R.drawable.notification_prev, "戻る", pi).build();

		NotificationCompat.MediaStyle mediaStyle = new NotificationCompat.MediaStyle();

		return new NotificationCompat.Builder(context)
				.setContentTitle(String.format("%sを再生中", video.title))
				.setContentText("content text")
				.setSmallIcon(R.drawable.trash_vector)
				.setLargeIcon(artwork)
				.setStyle(mediaStyle)
				.addAction(playAction)
				.build();
	}
}

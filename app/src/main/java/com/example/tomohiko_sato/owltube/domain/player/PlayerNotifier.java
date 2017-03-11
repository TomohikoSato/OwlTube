package com.example.tomohiko_sato.owltube.domain.player;


import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
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
		PLAY(R.drawable.notification_play, R.string.notification_play), PAUSE(R.drawable.notification_pause, R.string.notification_pause);
		public static final String INTENT_KEY = "StateKey";

		@DrawableRes
		private int icon;

		@StringRes
		private int message;

		State(int icon, int message) {
			this.icon = icon;
			this.message = message;
		}

		public NotificationCompat.Action createAction(Context context) {
			return new NotificationCompat.Action.Builder(icon, context.getString(message), createPendingIntent(context)).build();
		}

		State opposite(State state) {
			switch (state) {
				case PAUSE:
					return PLAY;
				case PLAY:
					return PAUSE;
				default:
					throw new IllegalArgumentException(state.toString());
			}
		}

		private PendingIntent createPendingIntent(Context context) {
			Intent intent = new Intent(context, PlayerNotificationReceiver.class);
			intent.putExtra(INTENT_KEY, opposite(this));

			return PendingIntent.getBroadcast(context, REQUEST_CODE_PLAYER_NOTIFICATION, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		}
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

	public Single<Notification> createNotification(Video video, State state) {
		return Single.fromCallable(() -> create(video, state))
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io());
	}


	private Notification create(Video video, State state) throws IOException {
		NotificationCompat.MediaStyle style = new NotificationCompat.MediaStyle();
		style.setShowActionsInCompactView(0);

		return new NotificationCompat.Builder(context)
				.setContentTitle(String.format("%sを再生中", video.title))
				.setContentText("content text")
				.setSmallIcon(R.drawable.trash_vector)
				.setLargeIcon(Picasso.with(context).load(video.thumbnailUrl).get())
				.setStyle(style)
				.addAction(state.createAction(context))
				.build();
	}


}

package com.example.tomohiko_sato.owltube.presentation.player.external;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.tomohiko_sato.owltube.OwlTubeApp;
import com.example.tomohiko_sato.owltube.common.rx.RxBus;
import com.example.tomohiko_sato.owltube.domain.player.PlayerNotifier;

import java.util.Objects;

import javax.inject.Inject;

import lombok.Getter;

public class PlayerNotificationReceiver extends BroadcastReceiver {

	@Inject
	RxBus rxBus;

	@Override
	public void onReceive(Context context, Intent intent) {
		((OwlTubeApp) context.getApplicationContext()).getComponent().inject(this);

		PlayerNotifier.State state = Objects.requireNonNull(
				(PlayerNotifier.State) intent.getSerializableExtra(PlayerNotifier.State.INTENT_KEY));
		rxBus.send(new PlayerStateChangeEvent(state));
	}

	public static class PlayerStateChangeEvent implements RxBus.Event {
		@Getter
		private PlayerNotifier.State state;

		public PlayerStateChangeEvent(PlayerNotifier.State state) {
			this.state = state;
		}
	}
}

package com.example.tomohiko_sato.owltube.presentation.player;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.example.tomohiko_sato.owltube.OwlTubeApp;
import com.example.tomohiko_sato.owltube.R;
import com.example.tomohiko_sato.owltube.common.rx.RxBus;
import com.example.tomohiko_sato.owltube.domain.data.Video;
import com.example.tomohiko_sato.owltube.domain.player.PlayerUseCase;
import com.example.tomohiko_sato.owltube.presentation.player.external.ExternalPlayerService;
import com.example.tomohiko_sato.owltube.presentation.player.external.PlayerNotificationReceiver;
import com.pierfrancescosoffritti.youtubeplayer.AbstractYouTubeListener;
import com.pierfrancescosoffritti.youtubeplayer.YouTubePlayerFullScreenListener;
import com.pierfrancescosoffritti.youtubeplayer.YouTubePlayerView;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import lombok.Getter;

import static java.util.Objects.requireNonNull;

public class PlayerActivity extends AppCompatActivity implements PlayerRelatedVideoAdapter.OnVideoItemSelectedListener {
	private static final String KEY_INTENT_EXTRA_VIDEO = "KEY_INTENT_EXTRA_VIDEO";
	@NonNull
	private final CompositeDisposable disposer = new CompositeDisposable();

	@Inject
	PlayerUseCase playerUseCase;

	@Inject
	RxBus rxBus;

	public static void startPlayerActivity(@NonNull Context context, @NonNull Video video) {
		ExternalPlayerService.stopIfRunning(context);

		Intent intent = new Intent(context, PlayerActivity.class);
		intent.putExtra(KEY_INTENT_EXTRA_VIDEO, video);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		((OwlTubeApp) getApplication()).getComponent().inject(this);
		setContentView(R.layout.activity_player);

		Video video = requireNonNull(getIntent().getParcelableExtra(KEY_INTENT_EXTRA_VIDEO));
		playerUseCase.addRecentlyWatched(video);
		disposer.add(playerUseCase.fetchRelatedVideo(video.videoId)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(relatedVideos -> ((RecyclerView) findViewById(R.id.related_videos))
								.setAdapter(new PlayerRelatedVideoAdapter(this, this, video, relatedVideos))
						, Throwable::printStackTrace
				));

		setupYoutubePlayer(video);

		findViewById(R.id.to_external)
				.setOnClickListener(v -> {
					ExternalPlayerService.startService(PlayerActivity.this, video);
					finish();
				});
	}

	public enum YoutubePlayerState {
		UNSTARTED(-1),
		ENDED(0),
		PLAYING(1),
		PAUSED(2),
		BUFFERING(3),
		VIDEO_CUED(5);

		private final int id;

		YoutubePlayerState(int id) {
			this.id = id;
		}

		static YoutubePlayerState from(int id) {
			for (YoutubePlayerState value : values()) {
				if (value.id == id) return value;
			}
			throw new IllegalArgumentException("illegal argument: " + Integer.toString(id));
		}
	}

	public static class PlayerViewStateChangedEvent implements RxBus.Event {
		@Getter
		private final YoutubePlayerState state;

		PlayerViewStateChangedEvent(YoutubePlayerState state) {
			this.state = state;
		}
	}

	private void setupYoutubePlayer(Video video) {
		YouTubePlayerView youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_player);
		youTubePlayerView.initialize(new AbstractYouTubeListener() {
			@Override
			public void onReady() {
				youTubePlayerView.loadVideo(video.videoId, 0);
			}

			@Override
			public void onStateChange(int state) {
				rxBus.send(new PlayerViewStateChangedEvent(YoutubePlayerState.from(state)));
			}
		}, true);

		youTubePlayerView.addFullScreenListener(new YouTubePlayerFullScreenListener() {
			private final FullScreenManager fullScreenManager = new FullScreenManager(PlayerActivity.this);

			@Override
			public void onYouTubePlayerEnterFullScreen() {
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
				fullScreenManager.enterFullScreen();
			}

			@Override
			public void onYouTubePlayerExitFullScreen() {
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
				fullScreenManager.exitFullScreen();
			}
		});
		rxBus.register(PlayerNotificationReceiver.PlayerNotificationStateChangedEvent.class, (event) -> {
			switch (event.getState()) {
				case PLAY:
					youTubePlayerView.playVideo();
					break;
				case PAUSE:
					youTubePlayerView.pauseVideo();
					break;
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		((YouTubePlayerView) findViewById(R.id.youtube_player)).release();
		disposer.dispose();
	}

	@Override
	public void onVideoItemSelected(Video item) {
		startPlayerActivity(this, item);
	}
}

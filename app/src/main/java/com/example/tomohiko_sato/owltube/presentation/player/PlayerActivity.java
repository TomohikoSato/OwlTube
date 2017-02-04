package com.example.tomohiko_sato.owltube.presentation.player;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.example.tomohiko_sato.owltube.OwlTubeApp;
import com.example.tomohiko_sato.owltube.R;
import com.example.tomohiko_sato.owltube.domain.data.Video;
import com.example.tomohiko_sato.owltube.domain.player.PlayerUseCase;
import com.pierfrancescosoffritti.youtubeplayer.AbstractYouTubeListener;
import com.pierfrancescosoffritti.youtubeplayer.YouTubePlayerView;

import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class PlayerActivity extends AppCompatActivity implements PlayerRecyclerViewAdapter.OnVideoItemSelectedListener {
	private static final String KEY_INTENT_EXTRA_VIDEO_ITEM = "VIDEO_ITEM";
	private static final int REQUEST_CODE_PLAYER_RECOVERY_DIALOG = 22;

	@NonNull
	private final CompositeDisposable disposables = new CompositeDisposable();
	private String videoId;
	private PlayerRecyclerViewAdapter adapter;
///	private ExternalPlayerService externalPlayerService;
//	private boolean isBound = false;

	@Inject
	PlayerUseCase playerUseCase;

/*
	private ServiceConnection connection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder binder) {
			Logger.d("onServiceConnected");
			isBound = true;
			externalPlayerService = ((ExternalPlayerService.ExternalPlayerServiceBinder) binder).getService();
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			Logger.d("onServiceDisconnected");
			isBound = false;
		}
	};
*/

	public static void startPlayerActivity(@NonNull Context context, @NonNull Video item) {
		Intent intent = new Intent(context, PlayerActivity.class);
		intent.putExtra(KEY_INTENT_EXTRA_VIDEO_ITEM, item);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getIntent() == null) {
			throw new RuntimeException("should start intent with key");
		}

		Video video = Objects.requireNonNull(getIntent().getParcelableExtra(KEY_INTENT_EXTRA_VIDEO_ITEM));
		((OwlTubeApp) getApplication()).getComponent().inject(this);
		setContentView(R.layout.activity_player);

		RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
		adapter = new PlayerRecyclerViewAdapter(this, this, video);
		recyclerView.setAdapter(adapter);
		videoId = video.videoId;
//		ExternalPlayerService.bindService(this, connection);

		playerUseCase.addRecentlyWatched(video);
		disposables.add(playerUseCase.fetchRelatedVideo(videoId)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(videos -> {
							adapter.setBodyItem(videos);
							adapter.notifyDataSetChanged();
						}, Throwable::printStackTrace
				));

		YouTubePlayerView youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_player);
		youTubePlayerView.initialize(new AbstractYouTubeListener() {
			@Override
			public void onReady() {
				youTubePlayerView.loadVideo(videoId, 0);
			}
		}, true);
//		playerView = (YouTubePlayerView) findViewById(R.id.youtube_player);
//		playerView.initialize(Api.Youtube.API_KEY, this);

/*
		Button external = (Button) findViewById(R.id.button_external);
		external.setOnClickListener(v -> {
			ExternalPlayerService.startService(PlayerActivity.this);
			ExternalPlayerLayout layout = (ExternalPlayerLayout) LayoutInflater.from(PlayerActivity.this).inflate(R.layout.view_external_player, null);
*/
/*
			Button closeButton = (Button) layout.findViewById(R.id.button_close);
			closeButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Logger.d( "onclick");
				}
			});
*//*

			externalPlayerView = (YouTubePlayerView) layout.findViewById(R.id.external_youtube_player);
			externalPlayerView.initialize(Api.API_KEY, PlayerActivity.this);
			externalPlayerService.addView(layout);
		});
*/
	}

	/*private YouTubePlayerView externalPlayerView;*/
/*
	private int toPixel(int dp) {
		final float scale = getResources().getDisplayMetrics().density;
		int pixel = (int) (dp * scale + 0.5f); //TODO: 0.5?
		return pixel;
	}
*/

/*	private YouTubePlayer currentPlayingPlayer;*/

	/*
		@Override
		public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
			Logger.d("onInitializationSuccess");
			if (currentPlayingPlayer != null) {
				currentPlayingPlayer.release();
				currentPlayingPlayer = null;
			}
			currentPlayingPlayer = player;
			Logger.d("provider equals external player service: " + provider.equals(externalPlayerView));

			player.setPlayerStyle(provider.equals(externalPlayerView) ? YouTubePlayer.PlayerStyle.CHROMELESS : YouTubePlayer.PlayerStyle.DEFAULT);
			if (!wasRestored) {
				player.loadVideo(videoId);
			}
		}

		@Override
		public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
			Logger.d("onInitializationFailure" + errorReason.toString());
			if (errorReason.isUserRecoverableError()) {
				errorReason.getErrorDialog(this, REQUEST_CODE_PLAYER_RECOVERY_DIALOG);
			}
		}

	*/
/*
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Logger.d("onActivityResult" + requestCode);
		if (REQUEST_CODE_PLAYER_RECOVERY_DIALOG == requestCode) {
			playerView.initialize(Api.API_KEY, this);
		}
	}

*/
	@Override
	protected void onStop() {
		super.onStop();
/*
		Logger.d("onStop");

		if (isBound) {
			unbindService(connection);
			isBound = false;
		}
*/
		disposables.dispose();
	}

	@Override
	public void onVideoItemSelected(Video item) {
		startPlayerActivity(this, item);
	}
}

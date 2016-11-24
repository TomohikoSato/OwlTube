package com.example.tomohiko_sato.owltube.presentation.player;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.example.tomohiko_sato.owltube.R;
import com.example.tomohiko_sato.owltube.config.Key;
import com.example.tomohiko_sato.owltube.di.DaggerSampleComponent;
import com.example.tomohiko_sato.owltube.di.SampleModule;
import com.example.tomohiko_sato.owltube.domain.callback.Callback;
import com.example.tomohiko_sato.owltube.domain.data.Video;
import com.example.tomohiko_sato.owltube.domain.player.PlayerUseCase;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.List;

import javax.inject.Inject;

public class PlayerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener, PlayerRecyclerViewAdapter.OnVideoItemSelectedListener {
	private static final String KEY_INTENT_EXTRA_VIDEO_ITEM = "VIDEO_ITEM";
	private static final int REQUEST_CODE_PLAYER_RECOVERY_DIALOG = 22;
	private static final String TAG = PlayerActivity.class.getSimpleName();
	private String videoId;
	private YouTubePlayerView playerView;

	private PlayerRecyclerViewAdapter adapter;
	ExternalPlayerService externalPlayerService;
	private boolean isBound = false;

	@Inject
	PlayerUseCase playerUseCase;

	private ServiceConnection connection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder binder) {
			Log.d(TAG, "onServiceConnected");
			isBound = true;
			externalPlayerService = ((ExternalPlayerService.ExternalPlayerServiceBinder) binder).getService();
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			Log.d(TAG, "onServiceDisconnected");
			isBound = false;
		}
	};

	public static void startPlayerActivity(Context context, Video item) {
		Intent intent = new Intent(context, PlayerActivity.class);
		intent.putExtra(KEY_INTENT_EXTRA_VIDEO_ITEM, item);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Video video = getIntent().getParcelableExtra(KEY_INTENT_EXTRA_VIDEO_ITEM);
		if (video == null) {
			throw new IllegalArgumentException("KEY_INTENT_EXTRA_VIDEO_ITEM must set");
		}

		DaggerSampleComponent.builder().sampleModule(new SampleModule(this)).build().inject(this);
		setContentView(R.layout.activity_player);

		RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
		adapter = new PlayerRecyclerViewAdapter(this, this, video);
		recyclerView.setAdapter(adapter);
		videoId = video.videoId;
		ExternalPlayerService.bindService(this, connection);

		playerUseCase.addRecentlyWatched(video);
		playerUseCase.fetchRelatedVideo(videoId, new Callback<List<Video>>() {
			public void onSuccess(List<Video> response) {
				adapter.setBodyItem(response);
				adapter.notifyDataSetChanged();
			}

			public void onFailure(Throwable t) {
				t.printStackTrace();
			}
		});

//		playerView = (YouTubePlayerView) findViewById(R.id.youtube_player);
//		playerView.initialize(Key.Youtube.API_KEY, this);

		Button external = (Button) findViewById(R.id.button_external);
		external.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ExternalPlayerService.startService(PlayerActivity.this);

				View layout = LayoutInflater.from(PlayerActivity.this).inflate(R.layout.view_external_player, null);
				Button closeButton = (Button) layout.findViewById(R.id.button_close);
				closeButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Log.d(TAG, "onclick");
					}
				});
				YouTubePlayerView externalPlayerView = (YouTubePlayerView) layout.findViewById(R.id.external_youtube_player);
				externalPlayerView.initialize(Key.Youtube.API_KEY, PlayerActivity.this);
				externalPlayerService.addView(layout);
			}
		});
	}

/*
	private int toPixel(int dp) {
		final float scale = getResources().getDisplayMetrics().density;
		int pixel = (int) (dp * scale + 0.5f); //TODO: 0.5?
		return pixel;
	}
*/

	private YouTubePlayer currentPlayingPlayer;

	@Override
	public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
		Log.d(TAG, "onInitializationSuccess");
		if (currentPlayingPlayer != null) {
			currentPlayingPlayer.release();
			currentPlayingPlayer = null;
		}
		currentPlayingPlayer = player;
		//TODO: 外部プレイヤーだったらCHROMELESSに

		player.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
		player.loadVideo(videoId);
	}

	@Override
	public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
		Log.d(TAG, "onInitializationFailure" + errorReason.toString());
		if (errorReason.isUserRecoverableError()) {
			errorReason.getErrorDialog(this, REQUEST_CODE_PLAYER_RECOVERY_DIALOG);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d(TAG, "onActivityResult" + requestCode);
		if (REQUEST_CODE_PLAYER_RECOVERY_DIALOG == requestCode) {
			playerView.initialize(Key.Youtube.API_KEY, this);
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.d(TAG, "onStop");

		if (isBound) {
			unbindService(connection);
			isBound = false;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy");
	}

	@Override
	public void onVideoItemSelected(Video item) {
		startPlayerActivity(this, item);
	}
}

package com.example.tomohiko_sato.mytube.presentation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.tomohiko_sato.mytube.R;
import com.example.tomohiko_sato.mytube.config.Key;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class PlayerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {
	private static final String KEY_INTENT_EXTRA_VIDEO_ID = "VIDEO_ID";
	private static final int REQUEST_CODE_RECOVERY_DIALOG = 22;
	private String videoId;
	private YouTubePlayerView playerView;

	public static void startPlayerActivity(Context context, String videoId) {
		Intent intent = new Intent(context, PlayerActivity.class);
		intent.putExtra(KEY_INTENT_EXTRA_VIDEO_ID, videoId);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player);
		videoId = getIntent().getStringExtra(KEY_INTENT_EXTRA_VIDEO_ID);
		if (videoId == null) {
			throw new IllegalArgumentException("KEY_INTENT_EXTRA_VIDEO_ID must set");
		}

		playerView = (YouTubePlayerView) findViewById(R.id.youtube_view);
		playerView.initialize(Key.Youtube.API_KEY, this);
	}

	@Override
	public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
		player.loadVideo(videoId);
	}

	@Override
	public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
		if (errorReason.isUserRecoverableError()) {
			errorReason.getErrorDialog(this, REQUEST_CODE_RECOVERY_DIALOG);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE_RECOVERY_DIALOG) {
			playerView.initialize(Key.Youtube.API_KEY, this);
		}
	}
}

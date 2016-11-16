package com.example.tomohiko_sato.mytube.presentation;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.tomohiko_sato.mytube.R;
import com.example.tomohiko_sato.mytube.config.Key;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class PlayerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {
	private static final String KEY_INTENT_EXTRA_VIDEO_ID = "VIDEO_ID";
	private static final int REQUEST_CODE_PLAYER_RECOVERY_DIALOG = 22;
	private static final int REQUEST_CODE_EXTERNAL_PLAYER_RECOVERY_DIALOG = 23;
	private String videoId;
	private YouTubePlayerView playerView;
	private YouTubePlayerView externalPlayerView;

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

		playerView = (YouTubePlayerView) findViewById(R.id.youtube_player);
		playerView.initialize(Key.Youtube.API_KEY, this);

		Button external = (Button) findViewById(R.id.button_external);
		external.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LayoutInflater layoutInflater = LayoutInflater.from(PlayerActivity.this);

				// 重ね合わせするViewの設定を行う
				WindowManager.LayoutParams params = new WindowManager.LayoutParams(
						WindowManager.LayoutParams.WRAP_CONTENT,
						WindowManager.LayoutParams.WRAP_CONTENT,
						WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
						WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
						PixelFormat.TRANSLUCENT);

				// WindowManagerを取得する
				WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

				// レイアウトファイルから重ね合わせするViewを作成する
				View view = layoutInflater.inflate(R.layout.external_player, null);
				externalPlayerView = (YouTubePlayerView) view.findViewById(R.id.external_youtube_player);
				externalPlayerView.initialize(Key.Youtube.API_KEY, PlayerActivity.this);

				// Viewを画面上に重ね合わせする
				wm.addView(view, params);
			}
		});
	}

	@Override
	public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
		player.loadVideo(videoId);
	}

	@Override
	public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
		if (errorReason.isUserRecoverableError()) {
			if (provider.equals(playerView)) {
				errorReason.getErrorDialog(this, REQUEST_CODE_PLAYER_RECOVERY_DIALOG);
			} else if (provider.equals(externalPlayerView)) {
				errorReason.getErrorDialog(this, REQUEST_CODE_EXTERNAL_PLAYER_RECOVERY_DIALOG);
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE_PLAYER_RECOVERY_DIALOG) {
			playerView.initialize(Key.Youtube.API_KEY, this);
		} else if (requestCode == REQUEST_CODE_EXTERNAL_PLAYER_RECOVERY_DIALOG) {
			externalPlayerView.initialize(Key.Youtube.API_KEY, this);
		}
	}
}

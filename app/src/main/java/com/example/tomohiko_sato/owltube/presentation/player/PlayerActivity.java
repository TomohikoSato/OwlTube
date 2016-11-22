package com.example.tomohiko_sato.owltube.presentation.player;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import com.example.tomohiko_sato.owltube.R;
import com.example.tomohiko_sato.owltube.config.Key;
import com.example.tomohiko_sato.owltube.di.DaggerSampleComponent;
import com.example.tomohiko_sato.owltube.di.SampleModule;
import com.example.tomohiko_sato.owltube.domain.callback.Callback;
import com.example.tomohiko_sato.owltube.domain.data.VideoItem;
import com.example.tomohiko_sato.owltube.domain.player.PlayerUseCase;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.List;

import javax.inject.Inject;

public class PlayerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {
	private static final String KEY_INTENT_EXTRA_VIDEO_ITEM = "VIDEO_ITEM";
	private static final int REQUEST_CODE_PLAYER_RECOVERY_DIALOG = 22;
	private static final int REQUEST_CODE_EXTERNAL_PLAYER_RECOVERY_DIALOG = 23;
	private static final String TAG = PlayerActivity.class.getSimpleName();
	private String videoId;
	private YouTubePlayerView playerView;
	private YouTubePlayerView externalPlayerView;

	@Inject
	PlayerUseCase playerUseCase;

	public static void startPlayerActivity(Context context, VideoItem item) {
		Intent intent = new Intent(context, PlayerActivity.class);
		intent.putExtra(KEY_INTENT_EXTRA_VIDEO_ITEM, item);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DaggerSampleComponent.builder().sampleModule(new SampleModule(this)).build().inject(this);


		setContentView(R.layout.activity_player);
		VideoItem videoItem = getIntent().getParcelableExtra(KEY_INTENT_EXTRA_VIDEO_ITEM);
		if (videoItem == null) {
			throw new IllegalArgumentException("KEY_INTENT_EXTRA_VIDEO_ITEM must set");
		}
		videoId = videoItem.videoId;
		playerUseCase.addRecentlyWatched(videoItem);
		playerUseCase.fetchRelatedVideo(videoId, new Callback<List<VideoItem>>() {
			public void onSuccess(List<VideoItem> response) {

			}

			public void onFailure(Throwable t) {
			}
		});

		playerView = (YouTubePlayerView) findViewById(R.id.youtube_player);
		playerView.initialize(Key.Youtube.API_KEY, this);

		Button external = (Button) findViewById(R.id.button_external);
		external.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LayoutInflater layoutInflater = LayoutInflater.from(PlayerActivity.this);
//				LinearLayout externalPlayerContainer = (LinearLayout) layoutInflater.inflate(R.layout.view_external_player, null);

				// 重ね合わせするViewの設定を行う
				WindowManager.LayoutParams params = new WindowManager.LayoutParams(
						toPixel(200),
						toPixel(110),
						WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
						WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
						PixelFormat.TRANSLUCENT);

				// WindowManagerを取得する
				WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
				ViewGroup parent = (ViewGroup) playerView.getParent();
				parent.removeView(playerView);

//				externalPlayerContainer.addView(playerView, toPixel(110), toPixel(200));
				wm.addView(playerView, params);
/*
				// レイアウトファイルから重ね合わせするViewを作成する

				externalPlayerView = (YouTubePlayerView) view.findViewById(R.id.external_youtube_player);
				externalPlayerView.initialize(Key.Youtube.API_KEY, PlayerActivity.this);

				// Viewを画面上に重ね合わせする
				wm.addView(view, params);
	*/
			}
		});
	}

	private int toPixel(int dp) {
		final float scale = getResources().getDisplayMetrics().density;
		int pixel = (int) (dp * scale + 0.5f); //0.5?
		return pixel;
	}

	@Override
	public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
		Log.d(TAG, "onInitializationSuccess");
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
}

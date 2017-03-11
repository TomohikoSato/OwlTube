package com.example.tomohiko_sato.owltube.presentation.player;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.tomohiko_sato.owltube.OwlTubeApp;
import com.example.tomohiko_sato.owltube.R;
import com.example.tomohiko_sato.owltube.common.rx.RxBus;
import com.example.tomohiko_sato.owltube.domain.common.PermissionHandler;
import com.example.tomohiko_sato.owltube.domain.data.Video;
import com.example.tomohiko_sato.owltube.domain.player.PlayerUseCase;
import com.example.tomohiko_sato.owltube.presentation.common_component.dialog.OkCancelDialogFragment;
import com.example.tomohiko_sato.owltube.presentation.player.external.ExternalPlayerService;
import com.pierfrancescosoffritti.youtubeplayer.AbstractYouTubeListener;
import com.pierfrancescosoffritti.youtubeplayer.YouTubePlayerFullScreenListener;
import com.pierfrancescosoffritti.youtubeplayer.YouTubePlayerView;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

import static java.util.Objects.requireNonNull;

public class PlayerActivity extends AppCompatActivity implements PlayerRelatedVideoAdapter.OnVideoItemSelectedListener, OkCancelDialogFragment.DialogInteractionListener {
	private static final String KEY_INTENT_EXTRA_VIDEO = "KEY_INTENT_EXTRA_VIDEO";
	@NonNull
	private final CompositeDisposable disposer = new CompositeDisposable();

	@Inject
	PlayerUseCase playerUseCase;

	@Inject
	RxBus rxBus;

	@Inject
	PermissionHandler permissionHandler;

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
		setupVideo(requireNonNull(getIntent().getParcelableExtra(KEY_INTENT_EXTRA_VIDEO)));
	}

	@Override
	public void onNewIntent(Intent intent) {
		setupVideo(requireNonNull(intent.getParcelableExtra(KEY_INTENT_EXTRA_VIDEO)));
	}

	private void setupVideo(Video video) {
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
					if (permissionHandler.hasPermission()) {
						ExternalPlayerService.startService(PlayerActivity.this, video);
						finish();
					} else {
						new OkCancelDialogFragment().show(getSupportFragmentManager(), "tag");
					}
				});
	}

	private void setupYoutubePlayer(Video video) {
		YouTubePlayerView youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_player);
		youTubePlayerView.initialize(new AbstractYouTubeListener() {
			@Override
			public void onReady() {
				youTubePlayerView.loadVideo(video.videoId, 0);
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

	@Override
	public void onOkClicked() {
		Toast.makeText(this, "okClicked", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onCancelClicked() {
		Toast.makeText(this, "onCancelClicked", Toast.LENGTH_SHORT).show();
	}
}

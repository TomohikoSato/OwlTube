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
import com.example.tomohiko_sato.owltube.domain.data.Video;
import com.example.tomohiko_sato.owltube.domain.player.PlayerUseCase;
import com.pierfrancescosoffritti.youtubeplayer.AbstractYouTubeListener;
import com.pierfrancescosoffritti.youtubeplayer.YouTubePlayerFullScreenListener;
import com.pierfrancescosoffritti.youtubeplayer.YouTubePlayerView;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

import static java.util.Objects.requireNonNull;

public class PlayerActivity extends AppCompatActivity implements PlayerRecyclerViewAdapter.OnVideoItemSelectedListener {
	private static final String KEY_INTENT_EXTRA_VIDEO_ITEM = "VIDEO_ITEM";

	@NonNull
	private final CompositeDisposable disposables = new CompositeDisposable();
	private YouTubePlayerView youTubePlayerView;

	@Inject
	PlayerUseCase playerUseCase;

	public static void startPlayerActivity(@NonNull Context context, @NonNull Video item) {
		Intent intent = new Intent(context, PlayerActivity.class);
		intent.putExtra(KEY_INTENT_EXTRA_VIDEO_ITEM, item);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Video video = requireNonNull(getIntent().getParcelableExtra(KEY_INTENT_EXTRA_VIDEO_ITEM));
		((OwlTubeApp) getApplication()).getComponent().inject(this);
		setContentView(R.layout.activity_player);

		playerUseCase.addRecentlyWatched(video);
		disposables.add(playerUseCase.fetchRelatedVideo(video.videoId)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(videos -> ((RecyclerView) findViewById(R.id.recycler_view))
								.setAdapter(new PlayerRecyclerViewAdapter(this, this, video, videos))
						, Throwable::printStackTrace
				));

		initYoutubePlayer(video);
	}

	private void initYoutubePlayer(Video video) {
		youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_player);
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
	protected void onStop() {
		super.onStop();
		disposables.dispose();
		youTubePlayerView.release();
	}

	@Override
	public void onVideoItemSelected(Video item) {
		startPlayerActivity(this, item);
	}
}

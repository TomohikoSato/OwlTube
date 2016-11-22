package com.example.tomohiko_sato.owltube.domain.player;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.example.tomohiko_sato.owltube.domain.data.VideoItem;
import com.example.tomohiko_sato.owltube.domain.util.Callback;
import com.example.tomohiko_sato.owltube.infra.api.youtube.YoutubeRequest;
import com.example.tomohiko_sato.owltube.infra.dao.RecentlyWatchedDao;

import java.util.List;

import javax.inject.Inject;

public class PlayerUseCase {
	private final RecentlyWatchedDao recentlyWatchedDao;
	private final YoutubeRequest youtubeRequest;

	@Inject
	public PlayerUseCase(RecentlyWatchedDao dao, YoutubeRequest request) {
		recentlyWatchedDao = dao;
		youtubeRequest = request;
	}

	public void addRecentlyWatched(@NonNull final VideoItem item) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				recentlyWatchedDao.insertOrUpdate(item);
			}
		}).start();
	}

	public void fetchRelatedVideo(final String videoId, final Callback<List<VideoItem>> callback) {
		final Handler handler = new Handler();
		new Thread(new Runnable() {
			@Override
			public void run() {
				final List<VideoItem> items = youtubeRequest.fetchRealtedToVideoId(videoId);
				handler.post(new Runnable() {
					@Override
					public void run() {
						callback.onSuccess(items);
					}
				});
			}
		}).start();
	}
}

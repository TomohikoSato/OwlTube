package com.example.tomohiko_sato.owltube.domain.player;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.example.tomohiko_sato.owltube.domain.data.VideoItem;
import com.example.tomohiko_sato.owltube.domain.callback.Callback;
import com.example.tomohiko_sato.owltube.infra.api.youtube.YoutubeRequest;
import com.example.tomohiko_sato.owltube.infra.dao.RecentlyWatchedDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

				List<String> videoIds = new ArrayList<>();
				for (VideoItem item : items) {
					videoIds.add(item.videoId);
				}

				Map<String, String> map = youtubeRequest.fetchStatistics(videoIds);

				for (VideoItem item : items) {
					item.viewCount = map.get(item.videoId);
				}

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

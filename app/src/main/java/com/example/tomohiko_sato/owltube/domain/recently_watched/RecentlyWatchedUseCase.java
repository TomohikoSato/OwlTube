package com.example.tomohiko_sato.owltube.domain.recently_watched;

import android.content.Context;
import android.os.Handler;

import com.example.tomohiko_sato.owltube.domain.callback.Callback;
import com.example.tomohiko_sato.owltube.domain.data.Video;
import com.example.tomohiko_sato.owltube.infra.api.youtube.YoutubeRequest;
import com.example.tomohiko_sato.owltube.infra.dao.RecentlyWatchedDao;

import java.util.List;

import javax.inject.Inject;

public class RecentlyWatchedUseCase {
	private final RecentlyWatchedDao recentlyWatchedDao;

	@Inject
	public RecentlyWatchedUseCase(RecentlyWatchedDao recentlyWatchedDao) {
		this.recentlyWatchedDao = recentlyWatchedDao;
	}

	public void fetchRecentlyWatched(final Callback<List<Video>> callback) {
		final Handler handler = new Handler();
		new Thread(new Runnable() {
			@Override
			public void run() {
				final List<Video> items= recentlyWatchedDao.selectAllOrderByRecentlyCreated(20);
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

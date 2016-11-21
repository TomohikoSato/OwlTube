package com.example.tomohiko_sato.mytube.domain.player;

import com.example.tomohiko_sato.mytube.domain.data.VideoItem;
import com.example.tomohiko_sato.mytube.infra.dao.RecentlyWatchedDao;

import javax.inject.Inject;

public class PlayerUseCase {
	RecentlyWatchedDao recentlyWatchedDao;

	@Inject
	public PlayerUseCase(RecentlyWatchedDao dao) {
		recentlyWatchedDao = dao;
	}

	public void addRecentlyWatched(final VideoItem item) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				recentlyWatchedDao.insertOrUpdate(item);
			}
		});
	}
}

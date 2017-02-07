package com.example.tomohiko_sato.owltube.domain.recently_watched;

import com.example.tomohiko_sato.owltube.domain.data.Video;
import com.example.tomohiko_sato.owltube.infra.dao.RecentlyWatchedDao;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RecentlyWatchedUseCase {
	private final RecentlyWatchedDao recentlyWatchedDao;
	private final int RECENTLY_WATCHED_COUNT = 20;

	@Inject
	public RecentlyWatchedUseCase(RecentlyWatchedDao recentlyWatchedDao) {
		this.recentlyWatchedDao = recentlyWatchedDao;
	}

	public Single<List<Video>> fetchRecentlyWatched() {
		return Single.fromCallable(() -> recentlyWatchedDao.selectAllOrderByRecentlyCreated(RECENTLY_WATCHED_COUNT))
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread());
	}
}

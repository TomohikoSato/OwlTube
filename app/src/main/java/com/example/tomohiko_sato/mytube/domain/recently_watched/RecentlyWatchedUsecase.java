package com.example.tomohiko_sato.mytube.domain.recently_watched;

import android.content.Context;

import com.example.tomohiko_sato.mytube.infra.api.youtube.YoutubeRequest;
import com.example.tomohiko_sato.mytube.infra.dao.RecentlyWatchedDao;
import com.example.tomohiko_sato.mytube.infra.dao.VideoItem;

import java.util.List;

import javax.inject.Inject;

public class RecentlyWatchedUseCase {
	private final YoutubeRequest youtubeRequest;
	private final Context context;
	private final RecentlyWatchedDao recentlyWatchedDao;

	@Inject
	RecentlyWatchedUseCase(YoutubeRequest youtubeRequest, Context context, RecentlyWatchedDao recentlyWatchedDao) {
		this.youtubeRequest = youtubeRequest;
		this.context = context;
		this.recentlyWatchedDao = recentlyWatchedDao;
	}

	public void fetchRecentlyWatched(RecentlyWatchedDao.Callback<List<VideoItem>> callback) {
		this.recentlyWatchedDao.selectOrderByRecentlyCreated(20, callback);

/*		SharedPreferences recentlyWatchedSP = context.getSharedPreferences(AppConst.Pref.NAME, 0);
		Set<String> set = recentlyWatchedSP.getStringSet(AppConst.Pref.KEY_RECENTLY_WATCHED, new HashSet<String>());

		final ArrayList<String> videoIdList = new ArrayList<>();
		videoIdList.addAll(set);
		youtubeRequest.fetch(videoIdList, callback);*/
	}
}

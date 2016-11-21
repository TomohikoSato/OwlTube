package com.example.tomohiko_sato.mytube.domain.recently_watched;

import android.content.Context;
import android.os.Handler;

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
	public RecentlyWatchedUseCase(YoutubeRequest youtubeRequest, Context context, RecentlyWatchedDao recentlyWatchedDao) {
		this.youtubeRequest = youtubeRequest;
		this.context = context;
		this.recentlyWatchedDao = recentlyWatchedDao;
	}

	public void fetchRecentlyWatched(final Callback<List<VideoItem>> callback) {
		final Handler handler = new Handler();
		new Thread(new Runnable() {
			@Override
			public void run() {
				final List<VideoItem> items= recentlyWatchedDao.selectAllOrderByRecentlyCreated(20);
				handler.post(new Runnable() {
					@Override
					public void run() {
						callback.onSuccess(items);
					}
				});
			}
		}).start();


/*		SharedPreferences recentlyWatchedSP = context.getSharedPreferences(AppConst.Pref.NAME, 0);
		Set<String> set = recentlyWatchedSP.getStringSet(AppConst.Pref.KEY_RECENTLY_WATCHED, new HashSet<String>());

		final ArrayList<String> videoIdList = new ArrayList<>();
		videoIdList.addAll(set);
		youtubeRequest.fetch(videoIdList, callback);*/
	}

		class Callback<T> {
		public void onSuccess(T items) {

		}

		public void onFailure() {

		}
	}
}

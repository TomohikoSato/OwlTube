package com.example.tomohiko_sato.owltube.domain.player;

import android.support.annotation.NonNull;

import com.example.tomohiko_sato.owltube.domain.data.Video;
import com.example.tomohiko_sato.owltube.infra.api.youtube.YoutubeRequest;
import com.example.tomohiko_sato.owltube.infra.dao.RecentlyWatchedDao;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class PlayerUseCase {
	private final RecentlyWatchedDao recentlyWatchedDao;
	private final YoutubeRequest youtubeRequest;

	@Inject
	public PlayerUseCase(RecentlyWatchedDao dao, YoutubeRequest request) {
		recentlyWatchedDao = dao;
		youtubeRequest = request;
	}

	public void addRecentlyWatched(@NonNull final Video item) {
		new Thread(() -> recentlyWatchedDao.add(item)).start();
	}

	public Observable<List<Video>> fetchRelatedVideo(@NonNull final String videoId) {
		return youtubeRequest.fetchRealtedToVideoId(videoId).map(videoResponse -> {
			final List<Video> videos = videoResponse.videos;
			List<String> videoIds = new ArrayList<>();
			for (Video video : videos) {
				videoIds.add(video.videoId);
			}

			youtubeRequest.fetchViewCount(videoIds).subscribe(map -> {
				for (Video video : videos) {
					video.viewCount = map.get(video.videoId);
				}
			});

			return videos;
		}).subscribeOn(Schedulers.io());
	}
}

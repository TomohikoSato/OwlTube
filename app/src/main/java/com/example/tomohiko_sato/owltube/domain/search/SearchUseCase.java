package com.example.tomohiko_sato.owltube.domain.search;

import android.os.Handler;
import android.support.annotation.Nullable;

import com.example.tomohiko_sato.owltube.domain.callback.Callback;
import com.example.tomohiko_sato.owltube.domain.data.Video;
import com.example.tomohiko_sato.owltube.domain.data.VideoResponse;
import com.example.tomohiko_sato.owltube.infra.api.google.GoogleRequest;
import com.example.tomohiko_sato.owltube.infra.api.youtube.YoutubeRequest;
import com.example.tomohiko_sato.owltube.infra.dao.SearchHistoryDao;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class SearchUseCase {
	private final YoutubeRequest youtubeRequest;
	private final GoogleRequest googleRequest;
	private final SearchHistoryDao dao;

	@Inject
	public SearchUseCase(YoutubeRequest youtubeRequest, GoogleRequest googleRequest, SearchHistoryDao dao) {
		this.youtubeRequest = youtubeRequest;
		this.googleRequest = googleRequest;
		this.dao = dao;
	}

	public Single<VideoResponse> search(final String query, @Nullable final String pageToken) {
		addSearchHistory(query);

		return youtubeRequest.search(query, pageToken).map(
				videoResponse -> {
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

					return videoResponse;
				}
		);
	}

	public Observable<List<String>> fetchSuggest(final String query) {
		return googleRequest.fetchSuggestKeywordForYoutube(query)
				.subscribeOn(Schedulers.io());
	}

	private void addSearchHistory(final String searchHistory) {
		new Thread(() -> {
			dao.addSearchHistory(searchHistory);
		}).start();
	}

	public Single<List<String>> fetchSearchHistories() {
		return dao.selectAllSearchHistories()
				.subscribeOn(Schedulers.io());
	}
}

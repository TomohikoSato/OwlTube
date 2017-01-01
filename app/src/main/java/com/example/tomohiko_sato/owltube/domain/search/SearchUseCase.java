package com.example.tomohiko_sato.owltube.domain.search;

import android.os.Handler;
import android.support.annotation.Nullable;

import com.example.tomohiko_sato.owltube.domain.data.Video;
import com.example.tomohiko_sato.owltube.domain.callback.Callback;
import com.example.tomohiko_sato.owltube.domain.data.VideoResponse;
import com.example.tomohiko_sato.owltube.infra.api.google.GoogleRequest;
import com.example.tomohiko_sato.owltube.infra.api.youtube.YoutubeRequest;
import com.example.tomohiko_sato.owltube.infra.dao.SearchHistoryDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

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

	public void search(final String query, @Nullable final String pageToken, final Callback<VideoResponse> callback) {
		addSearchHistory(query);

		final Handler handler = new Handler();
		new Thread(new Runnable() {
			@Override
			public void run() {
				final VideoResponse videoResponse= youtubeRequest.search(query, pageToken);
				final List<Video> videos = videoResponse.videos;
				handler.post(new Runnable() {
					@Override
					public void run() {
						callback.onSuccess(videoResponse);
					}
				});

				List<String> videoIds = new ArrayList<>();
				for (Video video : videos) {
					videoIds.add(video.videoId);
				}

				Map<String, String> map = youtubeRequest.fetchViewCount(videoIds);

				for (Video video : videos) {
					video.viewCount = map.get(video.videoId);
				}

				handler.post(new Runnable() {
					@Override
					public void run() {
						callback.onSuccess(videoResponse);
					}
				});
			}
		}).start();
	}

	public void fetchSuggest(final String query, final Callback<List<String>> callback) {
		final Handler handler = new Handler();

		new Thread(new Runnable() {
			@Override
			public void run() {
				final List<String> suggests = googleRequest.fetchSuggestKeywordForYoutube(query);
				handler.post(new Runnable() {
					@Override
					public void run() {
						callback.onSuccess(suggests);
					}
				});
			}
		}).start();
	}

	private void addSearchHistory(final String searchHistory) {
		new Thread(() -> {
			dao.addSearchHistory(searchHistory);
		}).start();
	}

	public void fetchSearchHistories(final Callback<List<String>> callback) {
		final Handler handler = new Handler();
		new Thread(new Runnable() {
			@Override
			public void run() {
				final List<String> searchHistories= dao.selectAllSearchHistories();
				handler.post(new Runnable() {
					@Override
					public void run() {
						callback.onSuccess(searchHistories);
					}
				});
			}
		}).start();
	}
}
